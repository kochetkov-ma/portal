package ru.iopump.portal;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import ru.iopump.portal.jira.configuration.JiraConfiguration;
import ru.iopump.portal.jira.retrofit.TokenWrapper;

import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Slf4j
@SpringBootApplication
public class PortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
    }

    /**
     * Получить новый {@link Gson} для работы с json.
     */
    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Scope(value = SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Bean
    public TokenWrapper authToken(JiraConfiguration jiraConfiguration) {
        log.info("[MESSAGE] TokenWrapper bean was created");
        return new TokenWrapper(Credentials.basic(jiraConfiguration.getUsername(), jiraConfiguration.getPassword()));
    }
}