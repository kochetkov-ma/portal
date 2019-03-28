package ru.iopump.portal.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
public class PortalUserRole {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 16, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<PortalUser> users;
}