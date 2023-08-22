package com.example.Backend.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a User of the system.
 * This Class contains Spring Boot Tags in order to specify the schema_manager_user Entity on DBSchemaManager Database
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "schema_manager_user")
public class DBSchemaManagerUser implements Serializable
{
    private static final long serialVersionUID = 953456789L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_schema_manager_user")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    /**
     * This password does not save regular user's passwords
     * It only exists to store fetcher database in order to establish authentication
     */
    private String password;

    /**
     * A user only has one role.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private DBSchemaManagerRole userRole;

    public DBSchemaManagerUser()
    {
        this.username = "";
        this.password = "";
        this.userRole = new DBSchemaManagerRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonBackReference
    public DBSchemaManagerRole getUserRole() {
        return userRole;
    }

    public void setUserRole(DBSchemaManagerRole userRole) {
        this.userRole = userRole;
    }
}