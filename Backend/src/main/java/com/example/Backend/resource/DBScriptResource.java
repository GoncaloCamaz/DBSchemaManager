package com.example.Backend.resource;

import com.example.Backend.dto.model.DBSchemaDTO;
import com.example.Backend.dto.model.DBScriptDTO;
import com.example.Backend.dto.model.ResultSetDTO;
import com.example.Backend.service.DBSchemaService;
import com.example.Backend.service.DBScriptService;
import com.example.Backend.utils.RequestConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * API endpoint to interact with dbscript entity.
 */
@RestController
@RequestMapping("/dbscript")
public class DBScriptResource
{
    @Autowired
    private DBScriptService scriptService;

    @Autowired
    private DBSchemaService dbSchemaService;

    /**
     * Sends a new or update version of a dbscript to the target database. If the operation is successful,
     * the record is saved on the dbschemamanager database
     * @param request
     * @return first 100 rows of the result of the view if the creation is successful
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("send")
    public ResponseEntity<?> checkAndSendDBScript(@RequestBody HashMap<String, String> request)
    {
        try{
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.dbscriptCheckAndSaveRequirements)))
                return new ResponseEntity<>("Required parameters not found", HttpStatus.BAD_REQUEST);

            boolean overrideVersion = Boolean.parseBoolean(request.get("sudo"));
            DBScriptDTO script = getDbScript(request);
            if(!validOptionalCredentials(request.get("username"), request.get("password")))
            {
                DBSchemaDTO schema = dbSchemaService.findDBSchemaByNameWithCredentials(script.getDbSchemaName());
                ResultSetDTO result = scriptService.sendScriptToTarget(script,request.get("definer"),schema.getUsername(), schema.getPassword(), overrideVersion);

                return new ResponseEntity<>(result, HttpStatus.CREATED);
            }
            else
            {
                ResultSetDTO result = scriptService.sendScriptToTarget(script,request.get("definer"),request.get("username"), request.get("password"), overrideVersion);

                return new ResponseEntity<>(result, HttpStatus.CREATED);
            }


        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Checks if user set another credentials to authenticate with schema
     * @param username name of the user
     * @param password password inserted
     * @return true if credentials where inserted and they are valid; false otherwise
     */
    private boolean validOptionalCredentials(String username, String password) {
        if(username != null && password != null)
        {
            return !username.isEmpty() && !password.isEmpty();
        }

        return false;
    }

    /**
     * Saves a new DbScript
     * this method is only used by fetcher if there is a new dbscript
     * @param request parameters
     * @return Response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("save")
    public ResponseEntity<?> saveDBScript(@RequestBody HashMap<String, String> request)
    {
        try{
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.dbscriptSaveRequirements)))
                return new ResponseEntity<>("Required parameters not found", HttpStatus.BAD_REQUEST);

            DBScriptDTO script = getDbScript(request);
            scriptService.saveScript(script);
            return new ResponseEntity<>("New script inserted", HttpStatus.CREATED);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Updates an dbscript
     * @param request parameters
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("update")
    public ResponseEntity<?> updateDBScript(@RequestBody HashMap<String, String> request)
    {
        try{
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.dbscriptSaveRequirements)))
                return new ResponseEntity<>("Required parameters not found", HttpStatus.BAD_REQUEST);

            DBScriptDTO script = getDbScript(request);
            scriptService.updateScript(script);
            return new ResponseEntity<>("Script Updated", HttpStatus.CREATED);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Transforms a request in DBScriptDTO
     * Note that date can be null if the object is not fetched from target database directly, for example, it will be
     * null if it is inserted from frontend application.
     * @param request parameters
     * @return DBScriptDTO
     */
    private DBScriptDTO getDbScript(HashMap<String, String> request) {
        DBScriptDTO script = new DBScriptDTO();
        script.setCode(request.get("code"));
        script.setDefiner(request.get("definer"));
        script.setDbObjectName(request.get("dbObjectName"));
        script.setDbSchemaName(request.get("dbSchemaName"));
        if(!request.containsKey("date"))
        {
            script.setDate(LocalDateTime.now());
        }
        else
        {
            script.setDate(LocalDateTime.parse(request.get("date")));
        }
        if(request.containsKey("description"))
            script.setDescription(request.get("description"));

        return script;
    }

    /**
     * Deletes a dbscript
     * @param schema name of the schema
     * @param dbobject name of the object
     * @param date of the script
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("delete/{dbSchemaName}/{dbObjectName}/{date}")
    public ResponseEntity<?> deleteDBScript(@PathVariable("dbSchemaName") String schema
            , @PathVariable("dbObjectName") String dbobject, @PathVariable("date") String date)
    {
        try{
            DBScriptDTO script = getDbScriptForDelete(schema, dbobject, date);
            scriptService.deleteScript(script);
            return new ResponseEntity<>("New script deleted", HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Transforms a request on a dbscript. Note that it must have certain information about a dbscript.
     * @param schema name of the schema
     * @param dbobject name of the dbobject
     * @param date date time of the script
     * @return
     */
    private DBScriptDTO getDbScriptForDelete(String schema, String dbobject, String date) {
        DBScriptDTO script = new DBScriptDTO();
        script.setDate(LocalDateTime.parse(date));
        script.setDbObjectName(dbobject);
        script.setDbSchemaName(schema);

        return script;
    }

    /**
     * Lists all dbscripts available
     * @return List of dbscripts
     */
    @GetMapping("")
    public List<DBScriptDTO> getAllDBScripts()
    {
        return this.scriptService.findAll();
    }

    /**
     * Lists dbscript by schema name dbschemaname and dbobject name dbObjectName
     * @param schema name of the schema
     * @param dbObject name of the object
     * @return List of dbscripts
     */
    @GetMapping("dbschema/{dbSchemaName}/dbobject/{dbObjectName}")
    public List<DBScriptDTO> getAllRecordsFromDBObject(@PathVariable("dbSchemaName") String schema, @PathVariable("dbObjectName") String dbObject)
    {
        try{
            String schema_decoded = URLDecoder.decode(schema, StandardCharsets.UTF_8);
            String object_decoded = URLDecoder.decode(dbObject, StandardCharsets.UTF_8);
            return this.scriptService.findAllOldestByDBObject(schema_decoded, object_decoded);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Lists all dbscripts but only shows the most recent record from each script associated object
     * @return list of dbscripts
     */
    @GetMapping("latest")
    public List<DBScriptDTO> getAllByLatest()
    {
        return this.scriptService.findAllLatest();
    }

    /**
     * Gets the most recent script from an dbobject dbObjectName of a schema dbschemaname
     * @param name name of the schema
     * @param dbo_name name of the dbobject
     * @return dbscript
     */
    @GetMapping("latest/{dbSchemaName}/{dbObjectName}")
    public DBScriptDTO getMostRecentDBScriptByDBObjectID(@PathVariable("dbSchemaName") String name, @PathVariable("dbObjectName") String dbo_name)
    {
        try{
            String schema_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
            String object_decoded = URLDecoder.decode(dbo_name, StandardCharsets.UTF_8);
            return this.scriptService.findLatestScript(schema_decoded, object_decoded);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lists all latest scripts by type
     * @param type of the object (view or procedure)
     * @return list of scripts
     */
    @GetMapping("latest/type/{type}")
    public List<DBScriptDTO> getAllByLatestByType(@PathVariable("type") String type)
    {
        try{
            String type_decoded = URLDecoder.decode(type, StandardCharsets.UTF_8);
            return this.scriptService.findAllLatestByType(type_decoded);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Lists all latest scripts by type
     * @param type of the object (view or procedure)
     * @return list of scripts
     */
    @GetMapping("latest/type/{type}/dbschema/{dbschema}")
    public List<DBScriptDTO> getAllByLatestByTypeBySchema(@PathVariable("type") String type, @PathVariable("dbschema") String schema)
    {
        try{
            String type_decoded = URLDecoder.decode(type, StandardCharsets.UTF_8);
            String schema_decoded = URLDecoder.decode(schema, StandardCharsets.UTF_8);

            return this.scriptService.findAllLatestByTypeBySchema(type_decoded, schema_decoded);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * List all latest scripts by type, schemaname and dbobjectname
     * @param type of the object
     * @param name of the schema
     * @param dbo_name of the dbobject
     * @return list of scripts
     */
    @GetMapping("latest/type/{type}/{dbSchemaName}/{dbObjectName}")
    public List<DBScriptDTO> getMostRecentDBScriptByDBObjectIDByType(@PathVariable("type") String type, @PathVariable("dbSchemaName") String name, @PathVariable("dbObjectName") String dbo_name)
    {
        try{
            String schema_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
            String object_decoded = URLDecoder.decode(dbo_name, StandardCharsets.UTF_8);
            String type_decoded = URLDecoder.decode(type, StandardCharsets.UTF_8);
            return this.scriptService.findLatestScriptByType(type_decoded, schema_decoded, object_decoded);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
