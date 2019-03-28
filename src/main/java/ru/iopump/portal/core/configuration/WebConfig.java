package ru.iopump.portal.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.iopump.portal.jira.dto.converter.VersionStatusConverter;

/**
 * Общая конфигурация сервлетов MVC.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Пользовательские конвертеры: запрос -> java object.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // конвертер для статуса версий для jira (VersionStatus.class)
        registry.addConverter(new VersionStatusConverter());
    }
}