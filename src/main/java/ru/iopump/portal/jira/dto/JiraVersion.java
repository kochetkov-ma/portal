package ru.iopump.portal.jira.dto;

import lombok.Data;

@Data
public class JiraVersion {

    public String id;
    public String description;
    public String name;
    public boolean archived;
    public boolean released;
    public String releaseDate;
    public boolean overdue;
    public String userReleaseDate;
    public long projectId;

    public static JiraVersion empty() {
        return new JiraVersion();
    }
}
