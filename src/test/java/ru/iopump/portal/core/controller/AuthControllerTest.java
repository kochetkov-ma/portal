package ru.iopump.portal.core.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateSessionAndDelete() {
        ResponseEntity<AuthController.AuthInformation> response;
        // авторизация, создание сессии и получение токена
        response = restTemplate
                .withBasicAuth("user", "greatfotest")
                .postForEntity("http://localhost:" + port + "/auth/session", null, AuthController.AuthInformation.class);

        // проверка ответа
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody())
                .isNotNull()
                .hasFieldOrPropertyWithValue("username", "user")
                .hasNoNullFieldsOrPropertiesExcept("sessionId", "isNew", "sessionCreationTime", "getLastAccessedTime");

        // добавление токена в заголовок
        final HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token", response.getBody().getSessionId());

        // выход по токену и завершение сессии
        response = restTemplate
                .exchange("http://localhost:" + port + "/auth/session",
                        HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        AuthController.AuthInformation.class);

        // проверка ответа о выходе
        assertThat(response.getStatusCodeValue()).isEqualTo(204);

        // повторная попытка удаления
        final ResponseEntity<AuthController.AuthInformation> responseTwo = restTemplate
                .exchange("http://localhost:" + port + "/auth/session",
                        HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        AuthController.AuthInformation.class);
        assertThat(responseTwo.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void testGetSession() {
        ResponseEntity<AuthController.AuthInformation> response;
        HttpHeaders headers;
        // попытка получение информации о сессии по несуществующему токену
        headers = new HttpHeaders();
        headers.add("X-Auth-Token", "ec7a8a16-e2da-4ecd-ba3b-a52aa9cdc020");
        response = restTemplate
                .exchange("http://localhost:" + port + "/auth/session",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        AuthController.AuthInformation.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(401);

        // авторизация, создание сессии и получение токена
        response = restTemplate
                .withBasicAuth("user", "greatfotest")
                .postForEntity("http://localhost:" + port + "/auth/session", null, AuthController.AuthInformation.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        // попытка получение информации о сессии по новому токену
        headers = new HttpHeaders();
        headers.add("X-Auth-Token", response.getBody().getSessionId());
        response = restTemplate
                .exchange("http://localhost:" + port + "/auth/session",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        AuthController.AuthInformation.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody())
                .isNotNull()
                .hasFieldOrPropertyWithValue("sessionId", response.getBody().getSessionId());

        // выход по токену и завершение сессии
        response = restTemplate
                .exchange("http://localhost:" + port + "/auth/session",
                        HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        AuthController.AuthInformation.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(204);

        // попытка получение информации о сессии, которая завершена
        response = restTemplate
                .exchange("http://localhost:" + port + "/auth/session",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        AuthController.AuthInformation.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }
}