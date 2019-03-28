package ru.iopump.portal.jira.dto;

import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.Credentials;
import org.springframework.util.StringUtils;

@Setter
@Accessors(chain = true)
public class JiraAuthMeta {
    private String username;
    private String password;
    private String basicToken;

    /**
     * Если заданы username и password, то сгенерировать токен.
     * Иначе если задан токен, то вернуть его. Если токен не начинается на 'Basic', то добавить 'Basic ' в начало.
     */
    public String calculateAuthToken() {
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            return Credentials.basic(username, password);
        }
        if (!StringUtils.isEmpty(basicToken)) {
            if (basicToken.startsWith("Basic ")) {
                return basicToken;
            } else {
                return "Basic " + basicToken;
            }
        }
        throw new IllegalArgumentException("Некорректные данные для авторизации в Jira. Не заданы username:password или basicToken");
    }
}