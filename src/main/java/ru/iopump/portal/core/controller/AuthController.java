package ru.iopump.portal.core.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpSession;

/**
 * Контроллер для создания сессии.<br/>
 * Созданная сессия возвращается в заголовке: X-Auth-Token.<br/>
 * Если получен токен X-Auth-Token, то рабоать с остальными сервисами можно через этот токен.<br/>
 * Для получения токена используется Basic авторизация через заголовок : Authorization = Basic MD5_username_password.<br/>
 * Также с сервисами можно работать, через Basic авторизацию - на каждый запрос будет создаваться новая сессия.<br/>
 */
@RestController
@Validated
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @PostMapping("/session")
    public AuthInformation login(HttpSession session, Authentication authentication) {
        return new AuthInformation(authentication.getName(),
                session.getId(),
                session.getCreationTime(),
                session.getLastAccessedTime(),
                session.isNew());
    }
    @GetMapping("/session")
    public AuthInformation information(HttpSession session, Authentication authentication) {
        return new AuthInformation(authentication.getName(),
                session.getId(),
                session.getCreationTime(),
                session.getLastAccessedTime(),
                session.isNew());
    }

    @DeleteMapping("/session")
    public ResponseEntity<String> logout(HttpSession session, Authentication authentication) {
        if (authentication == null || session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("not authenticated");
        }
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthInformation {
        private String username;
        private String sessionId;
        private long sessionCreationTime;
        private long getLastAccessedTime;
        private boolean isNew;
    }
}