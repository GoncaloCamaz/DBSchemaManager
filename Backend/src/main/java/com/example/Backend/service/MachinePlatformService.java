package com.example.Backend.service;

import com.example.Backend.dto.model.MachineDTO;
import com.example.Backend.dto.model.MachinePlatformDTO;
import com.example.Backend.dto.model.PlatformDTO;
import java.util.List;

public interface MachinePlatformService
{
    MachinePlatformDTO saveMachinePlatform(String machine, String platform);

    void deleteMachinePlatform(String machine, String platform);

    List<MachinePlatformDTO> findAll();

    List<PlatformDTO> findPlatformsByMachineName(String machinename);

    List<PlatformDTO> findPlatformsAvailableByMachineName(String machinename);

    List<MachinePlatformDTO> findMachinesByPlatformName(String platform_decoded);

    List<MachineDTO> findMachinesAvailableByPlatformName(String platform_decoded);
}
