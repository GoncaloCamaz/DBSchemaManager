package com.example.Backend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DBSchemaDTO {
    private String name;
    private String connectionstring;
    private String username;
    private String password;
    private String sqlservername;
    private LocalDateTime lastupdate;
    private String updateperiod;
    private String description;

    public DBSchemaDTO() {
    }

    public DBSchemaDTO(DBSchemaDTO dbSchemaDTO)
    {
        this.name = dbSchemaDTO.getName();
        this.connectionstring = dbSchemaDTO.getConnectionstring();
        this.username = dbSchemaDTO.getUsername();
        this.password = dbSchemaDTO.getPassword();
        this.sqlservername = dbSchemaDTO.getSqlservername();
        this.description = dbSchemaDTO.getDescription();
        this.updateperiod = dbSchemaDTO.getUpdateperiod();
        this.lastupdate = dbSchemaDTO.getLastupdate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnectionstring() {
        return connectionstring;
    }

    public void setConnectionstring(String connectionstring) {
        this.connectionstring = connectionstring;
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

    public String getSqlservername() {
        return sqlservername;
    }

    public void setSqlservername(String sqlservername) {
        this.sqlservername = sqlservername;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateperiod() {
        return updateperiod;
    }

    public void setUpdateperiod(String updateperiod) {
        this.updateperiod = updateperiod;
    }

    public LocalDateTime getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(LocalDateTime lastupdate) {
        this.lastupdate = lastupdate;
    }

    @Override
    public DBSchemaDTO clone()
    {
        return new DBSchemaDTO(this);
    }
}
