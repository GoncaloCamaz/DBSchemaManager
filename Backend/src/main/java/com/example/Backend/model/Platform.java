package com.example.Backend.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Platform.
 * This Class contains Spring Boot Tags in order to specify the platform Entity on DBSchemaManager Database
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "platform")
public class Platform implements Serializable
{
    private static final long serialVersionUID = 623456789L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_platform",updatable = false, nullable = false)
    private Long id;

    @Column(unique = true)
    private String url;

    private String username;

    private String password;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "platform",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DBSchema> dbSchemaSet;

    public Platform(Long id, String url, String name, String description, String username, String password, Set<DBSchema> dbSchemaSet) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.username = username;
        this.password = password;
        this.description = description;
        this.dbSchemaSet = dbSchemaSet;
    }

    public Platform() {
        this.url = "";
        this.name = "";
        this.username = "";
        this.password = "";
        this.description = "";
        this.dbSchemaSet = new HashSet<>();
    }

    public Platform(Platform p)
    {
        this.id = p.getId();
        this.url = p.getUrl();
        this.name = p.getName();
        this.username = p.getUsername();
        this.password = p.getPassword();
        this.description = p.getDescription();
        this.dbSchemaSet = p.getSchemaSet();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
    public Set<DBSchema> getSchemaSet() {
        return dbSchemaSet;
    }

    public void setSchemaSet(Set<DBSchema> dbSchemas) {
        this.dbSchemaSet = dbSchemas;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Platform clone()
    {
        return new Platform(this);
    }
}
