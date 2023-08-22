package com.example.Backend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DBSchemaManagerUserDTO {

    private String username;
    private String password;
    private String role;

    public DBSchemaManagerUserDTO() {
    }

    public DBSchemaManagerUserDTO(DBSchemaManagerUserDTO DBSchemaManagerUserDTO)
    {
        this.username = DBSchemaManagerUserDTO.getUsername();
        this.password = DBSchemaManagerUserDTO.getPassword();
        this.role = DBSchemaManagerUserDTO.getRole();
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DBSchemaManagerUserDTO clone()
    {
        return new DBSchemaManagerUserDTO(this);
    }
}
