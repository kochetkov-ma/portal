package ru.iopump.portal.jira.controller;

import com.google.gson.Gson;
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
import ru.iopump.portal.core.controller.AuthController;
import ru.iopump.portal.jira.dto.JiraAuthMeta;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class JiraControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Gson gson;

    @Test
    public void credentials() {
        ResponseEntity<AuthController.AuthInformation> response;
        String userToken;
        String adminToken;
        HttpHeaders headers;

        // авторизация user
        response = restTemplate
                .withBasicAuth("user", "greatfotest")
                .postForEntity("http://localhost:" + port + "/auth/session", null, AuthController.AuthInformation.class);
        assertThat(response.getBody()).isNotNull();

        userToken = response.getBody().getSessionId();

        // авторизация admin
        response = restTemplate
                .withBasicAuth("admin", "greatfotest")
                .postForEntity("http://localhost:" + port + "/auth/session", null, AuthController.AuthInformation.class);
        assertThat(response.getBody()).isNotNull();

        adminToken = response.getBody().getSessionId();

        ResponseEntity<String> jiraResponse;

        // получение сессии и проверка для user

        // изменение токена jira для user
        headers = new HttpHeaders();
        headers.add("X-Auth-Token", userToken);
        jiraResponse = restTemplate
                .exchange("http://localhost:" + port + "/jira/credentials",
                        HttpMethod.POST,
                        new HttpEntity<>(gson.toJson(new JiraAuthMeta().setBasicToken("userToken")), headers),
                        String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        // получение сессии и проверка для admin

        // изменение токена jira для admin
        headers = new HttpHeaders();
        headers.add("X-Auth-Token", adminToken);
        jiraResponse = restTemplate
                .exchange("http://localhost:" + port + "/jira/credentials",
                        HttpMethod.POST,
                        new HttpEntity<>(gson.toJson(new JiraAuthMeta().setBasicToken("adminToken")), headers),
                        String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        // получение сессии и проверка для user

        // получение сессии и проверка для admin

    }
}