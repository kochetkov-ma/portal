package ru.iopump.portal.jira.retrofit;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.iopump.portal.jira.configuration.JiraConfiguration;
import ru.iopump.portal.jira.configuration.RetrofitAuthenticationInterceptor;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


@Component
@Getter
@Slf4j
public class RetrofitExt {
    private Retrofit retrofit;
    private final JiraConfiguration jiraConfiguration;
    private final Gson gson;
    private final TokenWrapper authToken;

    @Autowired
    public RetrofitExt(JiraConfiguration jiraConfiguration, Gson gson, TokenWrapper authToken) {
        this.jiraConfiguration = jiraConfiguration;
        this.gson = gson;
        this.authToken = authToken;
        log.info("RetrofitExt created");
    }

    @PostConstruct
    private void postConstruct() {
        val httpClient = getUnsafeOkHttpClient();
        // logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // create credentials interceptor
        val authInterceptor = new RetrofitAuthenticationInterceptor(authToken);
        // add interceptors
        httpClient
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor);
        // create retrofit
        this.retrofit = new Retrofit.Builder()
                .baseUrl(jiraConfiguration.getUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
    }

    private static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}