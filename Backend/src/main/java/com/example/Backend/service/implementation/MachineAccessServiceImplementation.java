package com.example.Backend.service.implementation;

import com.example.Backend.dto.mapper.MachineAccessMapper;
import com.example.Backend.dto.model.MachineAccessDTO;
import com.example.Backend.model.Machine;
import com.example.Backend.model.MachineAccess;
import com.example.Backend.repository.MachineAccessRepo;
import com.example.Backend.repository.MachineRepo;
import com.example.Backend.service.MachineAccessService;
import com.example.Backend.utils.PasswordEncryptor;
import com.example.Backend.utils.PasswordEncryptorImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Since this entity is only accessable by the admin, all methods decrypt the password that is stored encrypted on the
 * database
 */
@Service
@Transactional
public class MachineAccessServiceImplementation implements MachineAccessService {

    @Autowired
    private MachineRepo machineRepo;

    @Autowired
    private MachineAccessRepo machineAccessRepo;

    private PasswordEncryptor encryptor;

    @Override
    public MachineAccessDTO saveMachineAccess(MachineAccessDTO machineAccessDTO) {
        Machine machine = machineRepo.findByName(machineAccessDTO.getMachineName());
        if(machine != null)
        {
            encryptor = new PasswordEncryptorImplementation();
            MachineAccess machineAccess = new MachineAccess();
            machineAccess.setMachine(machine);
            machineAccess.setUsername(machineAccessDTO.getUsername());
            machineAccess.setPassword(encryptor.encryptDataBaseObjectPassword(machineAccessDTO.getPassword()));
            machineAccess.setDescription(machineAccessDTO.getDescription());

            try {
                return MachineAccessMapper.toMachineAccessDTO(machineAccessRepo.save(machineAccess));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void updateMachineAccess(MachineAccessDTO machineAccessDTO) {
        MachineAccess machineAccess = machineAccessRepo.findMachineAccessByMachineNameAndByUsername(machineAccessDTO.getMachineName(),
                machineAccessDTO.getUsername());
        if(machineAccess != null)
        {
            encryptor = new PasswordEncryptorImplementation();
            machineAccess.setPassword(encryptor.encryptDataBaseObjectPassword(machineAccessDTO.getPassword()));
            machineAccess.setDescription(machineAccessDTO.getDescription());

            try {
                machineAccessRepo.save(machineAccess);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteMachineAccess(MachineAccessDTO machineAccessDTO) {
        MachineAccess machineAccess = machineAccessRepo.findMachineAccessByMachineNameAndByUsername(machineAccessDTO.getMachineName(),
                machineAccessDTO.getUsername());
        if(machineAccess != null)
        {
            try {
                machineAccessRepo.delete(machineAccess);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MachineAccessDTO findByUsernameAndMachine(String machinename, String username)
    {
        try
        {
            MachineAccess machineAccess = this.machineAccessRepo.findMachineAccessByMachineNameAndByUsername(machinename,username);
            if(machineAccess != null)
            {
                encryptor = new PasswordEncryptorImplementation();
                MachineAccessDTO machineAccessDTO = MachineAccessMapper.toMachineAccessDTO(machineAccess);
                machineAccessDTO.setPassword(encryptor.decryptDataBaseObjectPassword(machineAccess.getPassword()));
                return machineAccessDTO;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MachineAccessDTO> findAll() {
        List<MachineAccess> list = machineAccessRepo.findAll();

        return getMachineAccessDTOS(list);
    }

    @Override
    public List<MachineAccessDTO> findAllByMachineName(String name) {
        List<MachineAccess> list = machineAccessRepo.findMachineAccessByMachineName(name);

        return getMachineAccessDTOS(list);
    }

    /**
     * Extracted method that returs a list with passwords unencrypted
     * @param list machineAccess with encryptd passwords
     * @return list with machineAccessDTO with passwords unencrypted
     */
    private List<MachineAccessDTO> getMachineAccessDTOS(List<MachineAccess> list) {
        List<MachineAccessDTO> result = new ArrayList<>();

        for (MachineAccess machineAccess : list) {
            MachineAccessDTO machineAccessDTO = MachineAccessMapper.toMachineAccessDTO(machineAccess);
            encryptor = new PasswordEncryptorImplementation();
            machineAccessDTO.setPassword(encryptor.decryptDataBaseObjectPassword(machineAccess.getPassword()));
            result.add(machineAccessDTO);
        }
        return result;
    }
}
