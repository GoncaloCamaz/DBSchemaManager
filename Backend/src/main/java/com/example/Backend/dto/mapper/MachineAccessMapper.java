package com.example.Backend.dto.mapper;

import com.example.Backend.dto.model.MachineAccessDTO;
import com.example.Backend.model.MachineAccess;

/**
 * Transforms a MachineAccess in a MachineAccessDTO
 */
public class MachineAccessMapper {
    public static MachineAccessDTO toMachineAccessDTO(MachineAccess machineAccess)
    {
        MachineAccessDTO machineAccessDTO = new MachineAccessDTO();
        machineAccessDTO.setMachineName(machineAccess.getMachine().getName());
        machineAccessDTO.setUsername(machineAccess.getUsername());
        machineAccessDTO.setPassword(machineAccess.getPassword());
        machineAccessDTO.setDescription(machineAccess.getDescription());

        return machineAccessDTO;
    }
}
