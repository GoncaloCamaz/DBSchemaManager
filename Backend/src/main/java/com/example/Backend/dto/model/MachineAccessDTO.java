package com.example.Backend.dto.model;

public class MachineAccessDTO {
    private String machineName;
    private String username;
    private String password;
    private String description;


    public MachineAccessDTO() {
    }

    public MachineAccessDTO(String machineName, String username, String password, String description) {
        this.machineName = machineName;
        this.username = username;
        this.password = password;
        this.description = description;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
