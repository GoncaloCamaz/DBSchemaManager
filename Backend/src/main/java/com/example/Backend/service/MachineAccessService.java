package com.example.Backend.service;

import com.example.Backend.dto.model.MachineAccessDTO;

import java.util.List;

public interface MachineAccessService
{
    MachineAccessDTO saveMachineAccess(MachineAccessDTO machineAccessDTO);

    void updateMachineAccess(MachineAccessDTO machineAccessDTO);

    void deleteMachineAccess(MachineAccessDTO machineAccessDTO);

    MachineAccessDTO findByUsernameAndMachine(String machinename, String username);

    List<MachineAccessDTO> findAll();

    List<MachineAccessDTO> findAllByMachineName(String name);
}
