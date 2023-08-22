package com.example.Backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the role of a user.
 * This Class contains Spring Boot Tags in order to specify the schema_manager_role Entity on DBSchemaManager Database
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "schema_manager_role")
public class DBSchemaManagerRole implements Serializable
{
    private static final long serialVersionUID = 943456789L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_schema_manager_role")
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "userRole", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DBSchemaManagerUser> userSet;

    public DBSchemaManagerRole() {
        this.name = "";
        this.description = "";
        this.userSet = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonManagedReference
    public Set<DBSchemaManagerUser> getUserSet() {
        return userSet;
    }

    public void setUserRoleSet(Set<DBSchemaManagerUser> userRoleSet) {
        this.userSet = userRoleSet;
    }
}