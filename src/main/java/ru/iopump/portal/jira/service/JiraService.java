package ru.iopump.portal.jira.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.iopump.portal.jira.dto.JiraProject;
import ru.iopump.portal.jira.dto.JiraVersion;
import ru.iopump.portal.jira.retrofit.JiraProjectResource;
import ru.iopump.portal.jira.retrofit.JiraVersionResource;
import ru.iopump.portal.jira.retrofit.RetrofitExt;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@SuppressWarnings({"WeakerAccess", "unused"})
@Service
@Slf4j
public class JiraService {

    private final RetrofitExt retrofit;
    private JiraProjectResource jiraProjectResource;
    private JiraVersionResource jiraVersionResource;

    @Autowired
    public JiraService(@NotNull RetrofitExt retrofit) {
        this.retrofit = retrofit;
    }

    @PostConstruct
    private void postConstruct() {
        this.jiraProjectResource = retrofit.getRetrofit().create(JiraProjectResource.class);
        this.jiraVersionResource = retrofit.getRetrofit().create(JiraVersionResource.class);
    }

    @NonNull
    public Collection<JiraProject> getAllProjects() {
        return JiraServiceUtil.collect(JiraServiceUtil.execute(jiraProjectResource.getAllProjects()));
    }

    @NonNull
    public Collection<JiraVersion> getAllVersionsByProjectId(String idOrKey) {
        return JiraServiceUtil.collect(JiraServiceUtil.execute(jiraProjectResource.getAllVersionsById(idOrKey)));
    }

    @Nullable
    public JiraVersion getVersionById(String id) {
        return JiraServiceUtil.body(JiraServiceUtil.execute(jiraVersionResource.getVersionById(id)), JiraVersion.empty());
    }

    @Nullable
    public JiraVersion updateVersionById(String id, JiraVersion jiraVersion) {
        return JiraServiceUtil.body(JiraServiceUtil.execute(jiraVersionResource.updateVersionById(id, jiraVersion)));
    }

    public void changeAuthToken(@NonNull String authToken) {
        retrofit.getAuthToken().setToken(authToken);
    }
}