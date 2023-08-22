package com.example.Backend.service.implementation;

import com.example.Backend.dto.mapper.MachineMapper;
import com.example.Backend.dto.mapper.MachinePlatformMapper;
import com.example.Backend.dto.mapper.PlatformMapper;
import com.example.Backend.dto.model.MachineDTO;
import com.example.Backend.dto.model.MachinePlatformDTO;
import com.example.Backend.dto.model.PlatformDTO;
import com.example.Backend.model.Machine;
import com.example.Backend.model.MachinePlatform;
import com.example.Backend.model.Platform;
import com.example.Backend.repository.MachinePlatformRepo;
import com.example.Backend.repository.MachineRepo;
import com.example.Backend.repository.PlatformRepo;
import com.example.Backend.service.MachinePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MachinePlatformServiceImplementation implements MachinePlatformService
{
    @Autowired
    private MachinePlatformRepo machinePlatformrepo;

    @Autowired
    private MachineRepo machinerepo;

    @Autowired
    private PlatformRepo platformrepo;

    @Override
    public MachinePlatformDTO saveMachinePlatform(String machine, String platform) {
        Machine m = machinerepo.findByName(machine);
        Platform p = platformrepo.findByName(platform);

        if(m != null && p != null)
        {
            MachinePlatform mp = new MachinePlatform();
            mp.setMachine(m);
            mp.setPlatform(p);

            try {
                MachinePlatformMapper.toMachinePlatformDTO(machinePlatformrepo.save(mp));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void deleteMachinePlatform(String machinename, String platformname) {
        MachinePlatform mp = machinePlatformrepo.findByMachineAndPlatform(machinename,platformname);
        if(mp != null)
        {
            try {
                machinePlatformrepo.delete(mp);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<MachinePlatformDTO> findAll() {
        List<MachinePlatformDTO> machinePlatformDTOS = new ArrayList<>();
        List<MachinePlatform> machinePlatforms = machinePlatformrepo.findAll();

        for(MachinePlatform mp : machinePlatforms)
        {
            machinePlatformDTOS.add(MachinePlatformMapper.toMachinePlatformDTO(mp));
        }

        return machinePlatformDTOS;
    }

    @Override
    public List<PlatformDTO> findPlatformsByMachineName(String machinename) {
        List<MachinePlatform> used = machinePlatformrepo.findAllByMachineName(machinename);
        List<Platform> usedPlatforms = used.stream().map(MachinePlatform::getPlatform).collect(Collectors.toList());

        List<PlatformDTO> finalList = new ArrayList<>();
        for(Platform m : usedPlatforms)
        {
            finalList.add(PlatformMapper.toPlatformDTO(m));
        }
        return finalList;
    }

    @Override
    public List<PlatformDTO> findPlatformsAvailableByMachineName(String machinename) {
        List<MachinePlatform> used = machinePlatformrepo.findAllByMachineName(machinename);
        return getPlatformDTOS(used);
    }

    @Override
    public List<MachinePlatformDTO> findMachinesByPlatformName(String platform_decoded) {
        List<MachinePlatformDTO> machinePlatformDTOS = new ArrayList<>();
        Platform platform = platformrepo.findByName(platform_decoded);
        List<MachinePlatform> machinePlatforms = machinePlatformrepo.findAllByPlatformName(platform.getName());

        for(MachinePlatform mp : machinePlatforms)
        {
            machinePlatformDTOS.add(MachinePlatformMapper.toMachinePlatformDTO(mp));
        }

        return machinePlatformDTOS;
    }

    @Override
    public List<MachineDTO> findMachinesAvailableByPlatformName(String platform_decoded) {
        List<MachinePlatform> used = machinePlatformrepo.findAllByPlatformName(platform_decoded);
        return getMachineDTOS(used);
    }

    private List<MachineDTO> getMachineDTOS(List<MachinePlatform> used) {
        List<Machine> usedmachines = used.stream().map(MachinePlatform::getMachine).collect(Collectors.toList());
        List<Machine> machines = machinerepo.findAll();

        List<MachineDTO> finalList = new ArrayList<>();
        for(Machine m : machines)
        {
            if(!usedmachines.contains(m))
            {
                finalList.add(MachineMapper.toMachineDTO(m));
            }
        }
        return finalList;
    }

    private List<PlatformDTO> getPlatformDTOS(List<MachinePlatform> used) {
        List<Platform> usedPlatforms = used.stream().map(MachinePlatform::getPlatform).collect(Collectors.toList());
        List<Platform> platforms = platformrepo.findAll();

        List<PlatformDTO> finalList = new ArrayList<>();
        for(Platform m : platforms)
        {
            if(!usedPlatforms.contains(m))
            {
                finalList.add(PlatformMapper.toPlatformDTO(m));
            }
        }
        return finalList;
    }
}
