package ru.iopump.portal.jira.service;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.springframework.context.annotation.Bean;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * Настройка тестовых бинов.
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class TestConfiguration {

    /**
     * Бин для junit rule заглушки. Нужно, что бы при создании заглушки подгружалась конфигурация в соответсвие с профилем.
     */
    @Bean
    public WireMockRule wireMockRule(WiremockConfiguration wiremockConfiguration) {
        return new WireMockRule(options().
                httpsPort(wiremockConfiguration.getHttpsPort()).notifier(new Slf4jNotifier(true)), false
        );
    }
}
