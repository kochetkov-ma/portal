package ru.iopump.portal.jira.retrofit;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TokenWrapper implements Serializable {

    private String token;
}