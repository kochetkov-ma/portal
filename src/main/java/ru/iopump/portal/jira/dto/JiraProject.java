package ru.iopump.portal.jira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JiraProject {
    private long id;
    private String key;
    private String name;
}