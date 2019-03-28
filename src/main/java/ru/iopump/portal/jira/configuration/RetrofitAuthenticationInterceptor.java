package ru.iopump.portal.jira.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.iopump.portal.jira.retrofit.TokenWrapper;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class RetrofitAuthenticationInterceptor implements Interceptor {

    private final TokenWrapper authToken;

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request original = chain.request();
        final Request.Builder builder = original.newBuilder()
                .header("Authorization", authToken.getToken());
        final Request request = builder.build();
        return chain.proceed(request);
    }
}