package ru.iopump.portal.jira.service;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки для сервера заглушки.
 */
@ConfigurationProperties("wiremock")
@Setter
public class WiremockConfiguration {
    private Port port = new Port();

    int getHttpsPort() {
        return port.https;
    }

    @SuppressWarnings("unused")
    int getHttpPort() {
        return port.http;
    }

    @Setter
    @NoArgsConstructor // для spring
    private static class Port {
        private int http = 8081;
        private int https = 442;
    }
}