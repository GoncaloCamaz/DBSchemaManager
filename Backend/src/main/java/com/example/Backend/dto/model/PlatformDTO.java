package com.example.Backend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatformDTO {
    private String url;
    private String username;
    private String password;
    private String name;
    private String description;

    public PlatformDTO() {
    }

    public PlatformDTO(PlatformDTO platformDTO)
    {
        this.url = platformDTO.getUrl();
        this.username = platformDTO.getUsername();
        this.password = platformDTO.getPassword();
        this.name = platformDTO.getName();
        this.description = platformDTO.getDescription();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public PlatformDTO clone()
    {
        return new PlatformDTO(this);
    }
}
