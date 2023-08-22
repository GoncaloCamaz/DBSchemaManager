package com.example.Backend.dto.mapper;

import com.example.Backend.dto.model.MachineDTO;
import com.example.Backend.model.Machine;

/**
 * Transforms a Machine in a MachineDTO
 */
public class MachineMapper {
    public static MachineDTO toMachineDTO(Machine m)
    {
        MachineDTO machineDTO = new MachineDTO();
        machineDTO.setName(m.getName());
        machineDTO.setServiceip(m.getServiceip());
        machineDTO.setManagementip(m.getManagementip());
        machineDTO.setOperativesystem(m.getOperativesystem());
        machineDTO.setObservations(m.getObservations());
        machineDTO.setMachinetype(m.getMachinetype());
        machineDTO.setDescription(m.getDescription());

        return machineDTO;
    }
}
