package com.example.Backend.service.implementation;

import com.example.Backend.dto.mapper.MachineMapper;
import com.example.Backend.dto.model.MachineDTO;
import com.example.Backend.model.Machine;
import com.example.Backend.repository.MachineRepo;
import com.example.Backend.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MachineServiceImplementation implements MachineService {

    @Autowired
    private MachineRepo machineRepo;

    @Override
    public MachineDTO saveMachine(MachineDTO machineDTO) {
        Machine machine = new Machine();
        machine.setName(machineDTO.getName());
        machine.setServiceip(machineDTO.getServiceip());
        machine.setManagementip(machineDTO.getManagementip());
        machine.setMachinetype(machineDTO.getMachinetype());
        machine.setOperativesystem(machineDTO.getOperativesystem());
        machine.setObservations(machineDTO.getObservations());
        machine.setDescription(machineDTO.getDescription());

        try {
            return MachineMapper.toMachineDTO(machineRepo.save(machine));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteMachine(MachineDTO machineDTO) {
        try{
            Machine machine = machineRepo.findByName(machineDTO.getName());
            machineRepo.delete(machine);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMachine(MachineDTO machineDTO) {
        Machine machine = machineRepo.findByName(machineDTO.getName());

        machine.setServiceip(machineDTO.getServiceip());
        machine.setMachinetype(machineDTO.getMachinetype());
        machine.setManagementip(machineDTO.getManagementip());
        machine.setOperativesystem(machineDTO.getOperativesystem());
        machine.setObservations(machineDTO.getObservations());
        machine.setDescription(machineDTO.getDescription());

        try {
            machineRepo.save(machine);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public MachineDTO findMachineByName(String name) {
        Machine machine = machineRepo.findByName(name);

        return machine != null ? MachineMapper.toMachineDTO(machine) : null;
    }

    @Override
    public List<MachineDTO> findAll() {
        List<MachineDTO> machineDTOS = new ArrayList<>();
        List<Machine> machines = machineRepo.findAll();

        for(Machine m : machines)
        {
            machineDTOS.add(MachineMapper.toMachineDTO(m));
        }

        return machineDTOS;
    }
}
