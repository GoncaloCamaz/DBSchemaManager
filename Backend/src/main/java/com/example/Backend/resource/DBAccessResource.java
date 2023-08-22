package com.example.Backend.resource;

import com.example.Backend.dto.model.DBAccessDTO;
import com.example.Backend.dto.model.ResultSetDTO;
import com.example.Backend.service.DBAccessService;
import com.example.Backend.utils.RequestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * API endpoint for send access's to target databases
 * All information about accesses are obtained directly via
 * SQL Queries;
 */
@RestController
@RequestMapping("/dbaccess")
public class DBAccessResource {

    @Autowired
    private DBAccessService dbAccessService;

    /**
     * Sends new access to target database
     * @param request parameters
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("save")
    public ResponseEntity<?> sendAccess(@RequestBody HashMap<String,String> request)
    {
        try
        {
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.dbaccessSaveRequirements)))
                return new ResponseEntity<>("Required parameters not found", HttpStatus.BAD_REQUEST);

            DBAccessDTO dto = getDBAccess(request);

            ResultSetDTO resultSetDTO = dbAccessService.sendAccessToTarget(dto);

            return new ResponseEntity<>(resultSetDTO, HttpStatus.CREATED);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Transforms a request on a DBAccess DTO
     * @param request parameters
     * @return DBAccessDTO
     */
    private DBAccessDTO getDBAccess(HashMap<String, String> request)
    {
        DBAccessDTO dba = new DBAccessDTO();
        dba.setDbSchemaName(request.get("dbSchemaName"));
        dba.setDbObjectName(request.get("dbObjectName"));
        dba.setDbUsername(request.get("dbUsername"));
        dba.setPrivilege(request.get("privilege"));
        dba.setPermission(request.get("permission"));
        dba.setDbSchemaManagerUsername(request.get("dbSchemaManagerUsername"));
        dba.setDescription(request.get("description"));
        if(!request.containsKey("timestamp"))
        {
            dba.setTimestamp(LocalDateTime.now());
        }
        else
        {
            dba.setTimestamp(LocalDateTime.parse(request.get("timestamp")));
        }

        return dba;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("update")
    public ResponseEntity<?> updateDBAccess(@RequestBody HashMap<String, String> request)
    {
        if(!request.keySet().containsAll(Arrays.asList(RequestConstants.dbaccessSaveRequirements)))
            return new ResponseEntity<>("Required parameters not found", HttpStatus.BAD_REQUEST);
        try
        {
            DBAccessDTO dbAccessDTO = getDBAccess(request);
            this.dbAccessService.updateDBAccess(dbAccessDTO);
            return new ResponseEntity<>("",HttpStatus.OK);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Something went wrong!",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("delete/{schema_name}/{dbobject_name}/{dbusername}/{privilege}/{timestamp}")
    public ResponseEntity<?> deleteDBAccess(@PathVariable("schema_name") String schema,
                                            @PathVariable("dbobject_name") String dbobject,
                                            @PathVariable("dbusername") String username,
                                            @PathVariable("privilege") String privilege,
                                            @PathVariable("timestamp") String timestamp)
    {
        String schema_decoded = URLDecoder.decode(schema, StandardCharsets.UTF_8);
        String object_decoded = URLDecoder.decode(dbobject, StandardCharsets.UTF_8);
        String username_decoded = URLDecoder.decode(username, StandardCharsets.UTF_8);
        String privilege_decoded = URLDecoder.decode(privilege, StandardCharsets.UTF_8);
        LocalDateTime timestamp_decoded = LocalDateTime.parse(URLDecoder.decode(String.valueOf(timestamp), StandardCharsets.UTF_8));

        dbAccessService.deleteDBAccess(schema_decoded, object_decoded, username_decoded, privilege_decoded, timestamp_decoded);
        return new ResponseEntity<>("Access deleted", HttpStatus.OK);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @GetMapping("")
    public List<DBAccessDTO> getAllDBAccesses()
    {
        return this.dbAccessService.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @GetMapping("/dbschema/{dbschema}")
    public List<DBAccessDTO> getAllDBAccessesBySchema(@PathVariable("dbschema") String dbschema)
    {
        String schema_decoded = URLDecoder.decode(dbschema, StandardCharsets.UTF_8);
        return this.dbAccessService.findAllBySchema(schema_decoded);
    }
}
