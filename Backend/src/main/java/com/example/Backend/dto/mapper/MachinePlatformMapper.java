package com.example.Backend.dto.mapper;

import com.example.Backend.dto.model.MachinePlatformDTO;
import com.example.Backend.model.MachinePlatform;

/**
 * Transforms a MachinePlatform in a MachinePlatformDTO
 */
public class MachinePlatformMapper {
    public static MachinePlatformDTO toMachinePlatformDTO(MachinePlatform mp)
    {
        MachinePlatformDTO machinePlatformDTO = new MachinePlatformDTO();
        machinePlatformDTO.setPlatformname(mp.getPlatform().getName());
        machinePlatformDTO.setPlatformURL(mp.getPlatform().getUrl());
        machinePlatformDTO.setMachinename(mp.getMachine().getName());
        machinePlatformDTO.setMachinetype(mp.getMachine().getMachinetype());
        machinePlatformDTO.setMachineserviceip(mp.getMachine().getServiceip());
        machinePlatformDTO.setMachinemanagementip(mp.getMachine().getManagementip());

        return machinePlatformDTO;
    }
}
