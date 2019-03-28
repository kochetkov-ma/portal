package ru.iopump.portal.jira.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("jira")
public class JiraConfiguration {
    private String username;
    private String password;
    private String url;
}
