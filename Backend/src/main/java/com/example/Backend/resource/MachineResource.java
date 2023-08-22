package com.example.Backend.resource;

import com.example.Backend.dto.model.MachineDTO;

import com.example.Backend.service.MachineService;
import com.example.Backend.utils.RequestConstants;
import com.example.Backend.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/machine")
public class MachineResource
{
    @Autowired
    private MachineService machineService;

    /**
     * Method to save a new machine. ResponseOBject is used to send messages to frontend
     * @param request request parameters
     * @return ResponseObject
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("save")
    public ResponseObject<MachineDTO> saveMachine(@RequestBody HashMap<String,String> request)
    {
        try{
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.machinerequirements)))
                return new ResponseObject<>(400, "", "Something went wrong!", "Required parameters not found", null);

            MachineDTO machine = getMachine(request);
            if(this.machineService.findMachineByName(machine.getName()) == null)
            {
                machineService.saveMachine(machine);
                return new ResponseObject<>(201, "New machine inserted", "", "", null);
            }
            else
            {
                return new ResponseObject<>(409, "", "Something went wrong!", "Machine " + machine.getName() + " already exists!", null);

            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ResponseObject<>(500, "", "Something went wrong!", "Server error!", null);
    }

    /**
     * Transforms a request on a machine
     * @param request parameters
     * @return MachineDTO
     */
    private MachineDTO getMachine(HashMap<String, String> request) {
        MachineDTO machine = new MachineDTO();
        machine.setName(request.get("name"));
        machine.setObservations(request.get("observations"));
        machine.setServiceip(request.get("serviceip"));
        machine.setManagementip(request.get("managementip"));
        machine.setOperativesystem(request.get("operativesystem"));
        machine.setMachinetype(request.get("machinetype"));
        machine.setDescription(request.get("description"));

        return machine;
    }

    /**
     * Entry point to update a machine
     * @param request parameters
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("update")
    public ResponseObject<?> updateMachine(@RequestBody HashMap<String,String> request)
    {
        try{
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.machinerequirements)))
                return new ResponseObject<MachineDTO>(400,"","Something went wrong!","Required parameters not found", null);

            MachineDTO machine = getMachine(request);
            machineService.updateMachine(machine);

            return new ResponseObject<MachineDTO>(200,"Machine updated!","","", null);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ResponseObject<MachineDTO>(500,"","Something went wrong!","Server error!", null);
    }

    /**
     * Entry point to delete a machine. Please note that machine names may contain spaces, so the request must be
     * decoded
     * @param machinename name of the machine
     * @return Response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @RequestMapping(value = "delete/{machinename}", method = RequestMethod.DELETE)
    public ResponseObject<?> deleteMachine(@PathVariable("machinename") String machinename)
    {
        try{
            String machine_decoded = URLDecoder.decode(machinename, StandardCharsets.UTF_8);
            MachineDTO machine = new MachineDTO();
            machine.setName(machine_decoded);
            machineService.deleteMachine(machine);

            return new ResponseObject<MachineDTO>(200,"Machine deleted","","",null);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ResponseObject<MachineDTO>(500,"","Something went wrong!","Server error!", null);
    }

    /**
     * Gets all machines
     * @return all machines available
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @GetMapping("")
    public List<MachineDTO> getMachineList()
    {
        return this.machineService.findAll();
    }

}
