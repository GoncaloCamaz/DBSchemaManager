package com.example.Backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DBAccess implements Serializable {

    private static final long serialVersionUID = 32333233L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_dbaccess", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, name="dbSchemaManagerUsername")
    private String dbSchemaManagerUsername;

    @Column(nullable = false, name="dbUsername")
    private String dbUsername;

    @Column(nullable = false, name="dbObjectName")
    private String dbObjectName;

    /**
     * Grant / Revoke
     */
    @Column(nullable = false, name="permission")
    private String permission;

    @Column(nullable = false, name="dbSchemaName")
    private String dbSchemaName;

    /**
     * Select, Execute, All, Remove, ...
     */
    @Column(nullable = false, name="privilege")
    private String privilege;

    @Column(nullable = false, name="createtime")
    private LocalDateTime timestamp;

    private String description;

    public DBAccess(DBAccess access) {
        this.id = access.getId();
        this.privilege = access.getPrivilege();
        this.dbObjectName = access.getDbObjectName();
        this.dbUsername = access.getDbUsername();
        this.dbSchemaName = access.getDbSchemaName();
        this.description = access.getDescription();
        this.permission = access.getPermission();
        this.timestamp = access.getTimestamp();
        this.dbSchemaManagerUsername = access.getDbSchemaManagerUsername();
    }

    public DBAccess() {
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDbSchemaManagerUsername() {
        return dbSchemaManagerUsername;
    }

    public void setDbSchemaManagerUsername(String username) {
        this.dbSchemaManagerUsername = username;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbObjectName() {
        return dbObjectName;
    }

    public void setDbObjectName(String dbObjectName) {
        this.dbObjectName = dbObjectName;
    }

    public String getDbSchemaName() {
        return dbSchemaName;
    }

    public void setDbSchemaName(String dbSchemaName) {
        this.dbSchemaName = dbSchemaName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }
}
