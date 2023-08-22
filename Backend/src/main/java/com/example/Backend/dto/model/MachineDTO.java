package com.example.Backend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineDTO {
    private String name;
    private String serviceip;
    private String managementip;
    private String operativesystem;
    private String machinetype;
    private String description;
    private String observations;

    public MachineDTO() {
    }

    public MachineDTO(MachineDTO machineDTO)
    {
        this.name = machineDTO.getName();
        this.serviceip = machineDTO.getServiceip();
        this.managementip = machineDTO.getManagementip();
        this.operativesystem = machineDTO.getOperativesystem();
        this.observations = machineDTO.getObservations();
        this.machinetype = machineDTO.getMachinetype();
        this.description = machineDTO.getDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceip() {
        return serviceip;
    }

    public void setServiceip(String serviceip) {
        this.serviceip = serviceip;
    }

    public String getManagementip() {
        return managementip;
    }

    public void setManagementip(String managementip) {
        this.managementip = managementip;
    }

    public String getOperativesystem() {
        return operativesystem;
    }

    public void setOperativesystem(String operativesystem) {
        this.operativesystem = operativesystem;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getMachinetype() {
        return machinetype;
    }

    public void setMachinetype(String machinetype) {
        this.machinetype = machinetype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MachineDTO clone()
    {
        return new MachineDTO(this);
    }
}
