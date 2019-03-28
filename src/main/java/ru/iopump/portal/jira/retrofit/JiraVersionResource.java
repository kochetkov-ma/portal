package ru.iopump.portal.jira.retrofit;

import retrofit2.Call;
import retrofit2.http.*;
import ru.iopump.portal.jira.dto.JiraVersion;

public interface JiraVersionResource {

    @GET("version/{id}")
    Call<JiraVersion> getVersionById(@Path("id") String id, @Query("expand") String... expands);

    @PUT("version/{id}")
    Call<JiraVersion> updateVersionById(@Path("id") String id, @Body JiraVersion jiraVersion, @Query("expand") String... expands);
}