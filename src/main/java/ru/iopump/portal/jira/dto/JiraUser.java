package ru.iopump.portal.jira.dto;

import lombok.Data;

@Data
public class JiraUser {
    private String username;
    private String password;
}