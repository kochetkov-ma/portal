package ru.iopump.portal.jira.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.iopump.portal.core.exceptions.BadRequestPathException;
import ru.iopump.portal.jira.dto.JiraAuthMeta;
import ru.iopump.portal.jira.dto.JiraProject;
import ru.iopump.portal.jira.dto.JiraVersion;
import ru.iopump.portal.jira.dto.VersionStatus;
import ru.iopump.portal.jira.service.JiraService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.iopump.portal.core.configuration.WebSecurityConfig.ADMIN_ROLE;
import static ru.iopump.portal.core.configuration.WebSecurityConfig.USER_ROLE;

@RestController
@Validated
@RequestMapping("/jira")
@Secured({USER_ROLE, ADMIN_ROLE})
public class JiraController {

    private final JiraService jiraService;

    @Autowired
    public JiraController(JiraService jiraService) {
        this.jiraService = jiraService;
    }

    @GetMapping("/project")
    public Collection<JiraProject> getProjects() {
        return jiraService.getAllProjects();
    }

    @GetMapping("/project/{idOrKey}/versions")
    public Collection<JiraVersion> getVersionsByPattern(
            @PathVariable("idOrKey") String idOrKey,
            @RequestParam(value = "pattern", required = false, defaultValue = "") String pattern,
            @RequestParam(value = "match", required = false, defaultValue = "true") boolean match
    ) {
        val versions = jiraService.getAllVersionsByProjectId(idOrKey);
        if (StringUtils.isEmpty(pattern)) {
            return versions;
        } else {
            return versions.stream().filter(v -> match == v.getName().matches(pattern)).collect(Collectors.toList());
        }
    }

    @PostMapping("/project/{idOrKey}/versions/{status}")
    @ResponseBody
    public Collection<JiraVersion> setStatusVersionsByPattern(
            @PathVariable("idOrKey") String idOrKey,
            @PathVariable("status") VersionStatus versionStatus,
            @RequestParam("pattern") String pattern,
            @RequestParam(value = "match", required = false, defaultValue = "true") boolean match
    ) {
        val versions = jiraService.getAllVersionsByProjectId(idOrKey);
        if (versionStatus == null) {
            throw new BadRequestPathException("Incorrect status. Must be in " + Arrays.toString(VersionStatus.values()));
        }
        return versions.stream()
                .filter(v -> match == v.getName().matches(pattern))
                .peek(v -> {
                    switch (versionStatus) {
                        case ARCHIVED:
                            v.setArchived(true);
                            break;
                        case RELEASED:
                            v.setReleased(true);
                            break;
                        case UNRELEASED:
                            v.setReleased(false);
                            v.setArchived(false);
                            break;
                    }
                })
                .peek(v -> jiraService.updateVersionById(v.getId(), v))
                .collect(Collectors.toList());
    }

    @PostMapping("/credentials")
    public String credentials(@Valid @RequestBody JiraAuthMeta jiraAuthMeta) {
        jiraService.changeAuthToken(jiraAuthMeta.calculateAuthToken());
        return "Token changed successfully. Try to execute any request for check";
    }
}