package ru.iopump.portal.jira.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iopump.portal.jira.dto.JiraProject;
import ru.iopump.portal.PortalApplication;

import java.util.Collection;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PortalApplication.class, TestConfiguration.class})
@ActiveProfiles("test")
@EnableConfigurationProperties(WiremockConfiguration.class)
public class JiraServiceTest {

    @Rule
    @Autowired
    public WireMockRule wireMockRule;
    @Autowired
    private Gson gson;
    @Autowired
    private JiraService service;

    @Test
    public void testGetAllProjects() {
        // тестовый ответ
        final Collection<JiraProject> body = Lists.newArrayList(new JiraProject(1, "test", "test"));

        // настройка заглушки на тест
        wireMockRule.stubFor(get(urlMatching(".*/project"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withBody(gson.toJson(body))));

        // проверка
        Assertions.assertThat(service.getAllProjects()).isEqualTo(body);
    }

}