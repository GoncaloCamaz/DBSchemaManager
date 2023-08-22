package com.example.Backend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachinePlatformDTO {
    private String platformname;
    private String platformURL;
    private String machinename;
    private String machinetype;
    private String machineserviceip;
    private String machinemanagementip;


    public MachinePlatformDTO() {
    }

    public MachinePlatformDTO(MachinePlatformDTO machinePlatformDTO)
    {
        this.platformname = machinePlatformDTO.getPlatformname();
        this.platformURL = machinePlatformDTO.getPlatformURL();
        this.machinename = machinePlatformDTO.getMachinename();
        this.machineserviceip = machinePlatformDTO.getMachineserviceip();
        this.machinemanagementip = machinePlatformDTO.getMachinemanagementip();
        this.machinetype = machinePlatformDTO.getMachinetype();
    }

    public String getPlatformname() {
        return platformname;
    }

    public String getPlatformURL() {
        return platformURL;
    }

    public void setPlatformURL(String platformURL) {
        this.platformURL = platformURL;
    }

    public String getMachinename() {
        return machinename;
    }

    public void setMachinename(String machinename) {
        this.machinename = machinename;
    }

    public String getMachinetype() {
        return machinetype;
    }

    public void setMachinetype(String machinetype) {
        this.machinetype = machinetype;
    }

    public String getMachineserviceip() {
        return machineserviceip;
    }

    public void setMachineserviceip(String machineserviceip) {
        this.machineserviceip = machineserviceip;
    }

    public String getMachinemanagementip() {
        return machinemanagementip;
    }

    public void setMachinemanagementip(String machinemanagementip) {
        this.machinemanagementip = machinemanagementip;
    }

    public void setPlatformname(String platformname) {
        this.platformname = platformname;
    }

    public MachinePlatformDTO clone()
    {
        return new MachinePlatformDTO(this);
    }
}
