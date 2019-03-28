package ru.iopump.portal.jira.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.iopump.portal.jira.dto.JiraProject;
import ru.iopump.portal.jira.dto.JiraVersion;

import java.util.Collection;

public interface JiraProjectResource {

    @GET("project")
    Call<Collection<JiraProject>> getAllProjects(@Query("expand") String ... expands);

    @GET("project/{keyOrId}")
    Call<JiraProject> getProjectById(@Path("keyOrId")String keyOrId, @Query("expand") String ... expands);

    @GET("project/{keyOrId}/versions")
    Call<Collection<JiraVersion>> getAllVersionsById(@Path("keyOrId")String keyOrId, @Query("expand") String ... expands);
}