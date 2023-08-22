package com.example.Backend.resource;

import com.example.Backend.dto.model.DBObjectDTO;
import com.example.Backend.service.DBObjectService;
import com.example.Backend.utils.RequestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * API endpoint to interact with dbobject entity.
 */
@RestController
@RequestMapping("/dbobject")
public class DBObjectResource
{
    @Autowired
    private DBObjectService dbObjectService;

    /**
     * Saves a new dbobject
     * @param request parameters
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("save")
    public ResponseEntity<?> saveDBObject(@RequestBody HashMap<String,String> request)
    {
        try{
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.dbobjectSaveRequirements)))
            {
                return new ResponseEntity<>("Required parameters not found", HttpStatus.BAD_REQUEST);
            }
            else
            {
                if(this.dbObjectService.findDBObjectByNameBySchemaName(request.get("dbSchemaName"),request.get("name")) == null)
                {
                    DBObjectDTO dbobject = getDbObject(request);
                    dbObjectService.saveDBObject(dbobject);
                    return new ResponseEntity<>("New dbobject inserted.", HttpStatus.CREATED);
                }
                else
                {
                    return new ResponseEntity<>("DBObject already exists on schema.", HttpStatus.CONFLICT);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong while inserting DBObject", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Transforms an HTTP Request in a DBOBjectDTO
     * @param request parameters
     * @return DBObjectDTO
     */
    private DBObjectDTO getDbObject(HashMap<String, String> request) {
        DBObjectDTO dbobject = new DBObjectDTO();
        dbobject.setName(request.get("name"));
        dbobject.setDbSchemaName(request.get("dbSchemaName"));
        dbobject.setStatus(request.get("status"));
        dbobject.setType(request.get("type"));
        if(request.containsKey("description"))
            dbobject.setDescription(request.get("description"));

        return dbobject;
    }

    /**
     * Updates a dbobject
     * @param request parameters
     * @return Response with the result of the operation
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("update")
    public ResponseEntity<?> updateDBObject(@RequestBody HashMap<String, String> request){
        try{
            if(request.keySet().containsAll(Arrays.asList(RequestConstants.dbobjectSaveRequirements)))
            {
                DBObjectDTO dbObjectDTO = getDbObject(request);
                dbObjectService.updateDBObject(dbObjectDTO);

                return new ResponseEntity<>("Object updated", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("Required parameters not found", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong while inserting DBObject", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Deletes a dbobject
     * @param schema name of the schema
     * @param dbobject name of the dbobject
     * @return 200 if everything went ok and 500 otherwise
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("delete/{dbschema_name}/{dbobject_name}")
    public ResponseEntity<?> deleteDBObject(@PathVariable("dbschema_name") String schema, @PathVariable("dbobject_name") String dbobject)
    {
        try{
            String schema_decoded = URLDecoder.decode(schema, StandardCharsets.UTF_8);
            String dbobject_decoded = URLDecoder.decode(dbobject, StandardCharsets.UTF_8);
            DBObjectDTO dbObjectDTO = this.dbObjectService.findDBObjectByNameBySchemaName(schema_decoded,dbobject_decoded);
            this.dbObjectService.deleteDBObject(dbObjectDTO);

            return new ResponseEntity<>("DBObject deleted", HttpStatus.OK);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Something went wrong while deleting file", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Lists all dbobjects available
     * @return list with all objects
     */
    @GetMapping("")
    public List<DBObjectDTO> getAllDBObjects()
    {
        return this.dbObjectService.findAll();
    }

    /**
     * Lists all dbobjects by schema name dbSchemaName
     * @param schemaname name of the schema
     * @return list of dbobjects
     */
    @GetMapping("/dbschema/{dbSchemaName}")
    public List<DBObjectDTO> getDBObjectsBySchemaName(@PathVariable("dbSchemaName") String schemaname)
    {
        try{
            String schema_decoded = URLDecoder.decode(schemaname, StandardCharsets.UTF_8);
            return this.dbObjectService.findDBObjectsBySchemaName(schema_decoded);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Lists all dbobjects by schema name dbSchemaName and dbobject type (table/view/procedure) type
     * @param schemaname name of the object
     * @param dbo_type type of object
     * @return list of dbobjects
     */
    @GetMapping("/dbschema/{dbSchemaName}/type/{type}")
    public List<DBObjectDTO> getDBObjectsByTypeBySchemaName(@PathVariable("dbSchemaName") String schemaname,
                                                            @PathVariable("type") String dbo_type)
    {
        try{
            String schema_decoded = URLDecoder.decode(schemaname, StandardCharsets.UTF_8);
            String type_decoded = URLDecoder.decode(dbo_type, StandardCharsets.UTF_8);
            return this.dbObjectService.findDBObjectsByTypeBySchemaName(schema_decoded, type_decoded);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Finds dbobject by name and schema
     * @param schema name of the schema
     * @param dbobject name of the dbobject
     * @return DBObjectDTO
     */
    @GetMapping("/dbschema/{dbSchemaName}/dbobject/{dbobjectname}")
    public DBObjectDTO getDBObjectByName(@PathVariable("dbSchemaName") String schema, @PathVariable("dbobjectname") String dbobject)
    {
        try{
            String schema_decoded = URLDecoder.decode(schema, StandardCharsets.UTF_8);
            String dbobject_decoded = URLDecoder.decode(dbobject, StandardCharsets.UTF_8);
            return this.dbObjectService.findDBObjectByNameBySchemaName(schema_decoded,dbobject_decoded);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lists dbobjects by their type (view/table/procedure)
     * @param type type of object
     * @return list of dbobjects
     */
    @GetMapping("/type/{type}")
    public List<DBObjectDTO> getDBObjectsByType(@PathVariable("type") String type)
    {
        try{
            String type_decoded = URLDecoder.decode(type, StandardCharsets.UTF_8);
            return this.dbObjectService.findDBObjectsByType(type_decoded);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
