package com.example.Backend.resource;

import com.example.Backend.dto.model.MachineAccessDTO;
import com.example.Backend.service.MachineAccessService;
import com.example.Backend.utils.RequestConstants;
import com.example.Backend.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/machineaccess")
public class MachineAccessResource {

    @Autowired
    private MachineAccessService machineAccessService;

    /**
     * Method to save a new access.
     * It may only be accessed by users with ADMIN role
     * @param request parameters
     * @return save a new user
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("save")
    public ResponseObject<MachineAccessDTO> saveMachineAccess(@RequestBody HashMap<String, String> request)
    {
        if(!request.keySet().containsAll(Arrays.asList(RequestConstants.machineAccessRequirements)))
        {
            return new ResponseObject<>(400, "", "", "Required parameters not found!", null);
        }

        MachineAccessDTO machineAccessDTO = getMachineAccessDTO(request);
        if(machineAccessService.findByUsernameAndMachine(machineAccessDTO.getMachineName(), machineAccessDTO.getUsername()) == null)
        {
            machineAccessService.saveMachineAccess(machineAccessDTO);
            return new ResponseObject<>(201, "New machine access stored!", "", "", null);
        }
        else
        {
            return new ResponseObject<>(409, "", "", "Username already exists!", null);
        }
    }

    /**
     * Transforms request parameters to a MachineAccessDTO
     * @param request parameters
     * @return machineAccessDTO
     */
    private MachineAccessDTO getMachineAccessDTO(HashMap<String, String> request) {
        MachineAccessDTO machineAccessDTO = new MachineAccessDTO();
        machineAccessDTO.setMachineName(request.get("machinename"));
        machineAccessDTO.setUsername(request.get("username"));
        machineAccessDTO.setPassword(request.get("password"));
        machineAccessDTO.setDescription(request.get("description"));

        return machineAccessDTO;
    }

    /**
     * Update request
     * Updates only password or description.
     * @param request parameters
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("update")
    public ResponseEntity<?> updateMachineAccess(@RequestBody HashMap<String, String> request)
    {
        if(!request.keySet().containsAll(Arrays.asList(RequestConstants.machineAccessRequirements)))
        {
            return new ResponseEntity<>("Request requirements not met", HttpStatus.BAD_REQUEST);
        }

        MachineAccessDTO machineAccessDTO = getMachineAccessDTO(request);
        machineAccessService.updateMachineAccess(machineAccessDTO);
        return new ResponseEntity<>("Machine access updated", HttpStatus.CREATED);
    }

    /**
     * Deletes an access
     * @param machine name of the machine
     * @param username name of the user
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("delete/{machinename}/{username}")
    public ResponseEntity<?> deleteMachineAccess(@PathVariable("machinename") String machine, @PathVariable("username") String username)
    {
        String machinename_decoded = URLDecoder.decode(machine, StandardCharsets.UTF_8);
        String username_decoded = URLDecoder.decode(username, StandardCharsets.UTF_8);
        MachineAccessDTO machineAccessDTO = new MachineAccessDTO(machinename_decoded,username_decoded,"","");
        machineAccessService.deleteMachineAccess(machineAccessDTO);
        return new ResponseEntity<>("Machine access saved", HttpStatus.CREATED);
    }

    /**
     * Lists all access by machine
     * @param name of the machine
     * @return list with MachineAccessDTO
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("machine/{machine_name}")
    public List<MachineAccessDTO> findAllByMachine(@PathVariable("machine_name") String name)
    {
        String machinename_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
        return machineAccessService.findAllByMachineName(machinename_decoded);
    }
}
