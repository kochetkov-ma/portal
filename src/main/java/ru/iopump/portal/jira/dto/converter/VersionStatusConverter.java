package ru.iopump.portal.jira.dto.converter;

import com.google.common.base.Enums;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import ru.iopump.portal.jira.dto.VersionStatus;

/**
 * Конвертор переменной из path запроса на {@link VersionStatus}.
 */
public class VersionStatusConverter implements Converter<String, VersionStatus> {

    @Override
    public VersionStatus convert(@Nullable String source) {
        if (source == null) {
            return null;
        }
        return Enums.getIfPresent(VersionStatus.class, source.toUpperCase()).orNull();
    }
}