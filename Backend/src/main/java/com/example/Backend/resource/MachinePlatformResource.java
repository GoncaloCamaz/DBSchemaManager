package com.example.Backend.resource;

import com.example.Backend.dto.model.MachineDTO;
import com.example.Backend.dto.model.MachinePlatformDTO;
import com.example.Backend.dto.model.PlatformDTO;
import com.example.Backend.service.MachinePlatformService;
import com.example.Backend.utils.RequestConstants;
import com.example.Backend.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/machineplatform")
public class MachinePlatformResource {

    @Autowired
    private MachinePlatformService machinePlatformService;

    /**
     * Saves relations between machines and platforms
     * @param request parameters
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("save")
    public ResponseObject<?> saveMachinePlatform(@RequestBody HashMap<String,String> request)
    {
        try{
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.machineplataformrequirements)))
                return new ResponseObject<PlatformDTO>(400,"","Something went wrong!","Required parameters not found", null);

            machinePlatformService.saveMachinePlatform(request.get("machinename"),request.get("platformname"));

            return new ResponseObject<PlatformDTO>(201,"New relation inserted","","", null);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ResponseObject<PlatformDTO>(500,"","Something went wrong!","Server error!", null);
    }

    /**
     * Deletes relations between machines and platforms
     * @param machine name of the machine
     * @param platform name of the platform
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @DeleteMapping("delete/{machinename}/{platformname}")
    public ResponseObject<?> deleteMachinePlatform(@PathVariable("machinename") String machine, @PathVariable("platformname") String platform)
    {
        try{
            String machine_decoded = URLDecoder.decode(machine, StandardCharsets.UTF_8);
            String platform_decoded = URLDecoder.decode(platform, StandardCharsets.UTF_8);
            this.machinePlatformService.deleteMachinePlatform(machine_decoded, platform_decoded);
            return new ResponseObject<PlatformDTO>(200,"Platform deleted","","",null);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseObject<PlatformDTO>(500,"","Something went wrong!","Server error!", null);
    }

    /**
     * Lists all machines used by a given platoform
     * @param platform name of the platform
     * @return list with relations
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @GetMapping("machines/platform/{platformname}")
    public List<MachinePlatformDTO> machinesByPlatformName(@PathVariable("platformname") String platform)
    {
        try{
            String platform_decoded = URLDecoder.decode(platform, StandardCharsets.UTF_8);
            return machinePlatformService.findMachinesByPlatformName(platform_decoded);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Lists all machines that are not in use by a given platform
     * @param platform name of the platform
     * @return list of machines not in use by platformURL given
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @GetMapping("machines/available/{platformname}")
    public List<MachineDTO> machinesAvailableByPlatformName(@PathVariable("platformname") String platform)
    {
        try{
            String platform_decoded = URLDecoder.decode(platform, StandardCharsets.UTF_8);
            return machinePlatformService.findMachinesAvailableByPlatformName(platform_decoded);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * list all platforms by a given machine
     * @param machinename name of the machine
     * @return list with platforms
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @GetMapping("platforms/machine/{machinename}")
    public List<PlatformDTO> platformsByMachine(@PathVariable("machinename") String machinename)
    {
        try{
            String machine_decoded = URLDecoder.decode(machinename, StandardCharsets.UTF_8);
            return machinePlatformService.findPlatformsByMachineName(machine_decoded);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * List all platforms that are not supported by a given machine
     * @param machinename name of the machine
     * @return list of platforms
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @GetMapping("platforms/available/{machinename}")
    public List<PlatformDTO> platformsAvailableByMachine(@PathVariable("machinename") String machinename)
    {
        try{
            String machine_decoded = URLDecoder.decode(machinename, StandardCharsets.UTF_8);
            return machinePlatformService.findPlatformsAvailableByMachineName(machine_decoded);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
