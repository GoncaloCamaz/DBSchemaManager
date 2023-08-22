package com.example.Backend.service;

import com.example.Backend.dto.model.MachineDTO;
import java.util.List;

public interface MachineService
{
    MachineDTO saveMachine(MachineDTO machine);

    void deleteMachine(MachineDTO machine);

    void updateMachine(MachineDTO machine);

    MachineDTO findMachineByName(String name);

    List<MachineDTO> findAll();
}
