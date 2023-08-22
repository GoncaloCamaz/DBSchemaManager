package com.example.Backend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * This is the representation of an DBAccessDTO. This entity is used to create the access on the target database. Then, if
 * the operation is successful, the information about the access granted/revoked will be stored on dbschemamanager database.
 * The records stored are only used to keep a history of the records given. Fetcher cannot update access's so, the
 * records on dbschemamanager database may not say if the access is still valid
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DBAccessDTO {
    private String dbSchemaManagerUsername;
    private String dbUsername;
    private String description;
    private String dbObjectName;
    private String permission;
    private String dbSchemaName;
    private String privilege;
    private LocalDateTime timestamp;

    public DBAccessDTO() {
    }

    public DBAccessDTO(DBAccessDTO dbAccessDTO) {
        this.dbSchemaManagerUsername = dbAccessDTO.getDbSchemaManagerUsername();
        this.dbUsername = dbAccessDTO.getDbUsername();
        this.description = dbAccessDTO.getDescription();
        this.dbSchemaName = dbAccessDTO.getDbSchemaName();
        this.dbObjectName = dbAccessDTO.getDbObjectName();
        this.privilege = dbAccessDTO.getPrivilege();
        this.permission = dbAccessDTO.getPermission();
        this.timestamp = dbAccessDTO.getTimestamp();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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

    public String getDbSchemaManagerUsername() {
        return dbSchemaManagerUsername;
    }

    public void setDbSchemaManagerUsername(String dbSchemaManagerUsername) {
        this.dbSchemaManagerUsername = dbSchemaManagerUsername;
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

    @Override
    public DBAccessDTO clone()
    {
        return new DBAccessDTO(this);
    }
}