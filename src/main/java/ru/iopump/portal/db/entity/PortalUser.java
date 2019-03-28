package ru.iopump.portal.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
public class PortalUser {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "full_name", length = 64, nullable = false)
    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "portal_user_portal_user_role",
            joinColumns = @JoinColumn(name = "portal_user_id"),
            inverseJoinColumns = @JoinColumn(name = "portal_user_role_id")
    )
    @Column(name = "roles")
    private Collection<PortalUserRole> roles;

    @Column(name = "username", length = 32, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "jira_basic_token", length = 128, nullable = false)
    private String jiraBasicToken;

    @Column(name = "enabled", columnDefinition="boolean default true")
    private Boolean enabled;

    @Column(name = "locked", columnDefinition="boolean default false")
    private Boolean locked;

    @Column(name = "expired", columnDefinition="boolean default false")
    private Boolean expired;
}