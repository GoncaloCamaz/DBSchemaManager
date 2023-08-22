package com.example.Backend.resource;

import com.example.Backend.dto.model.DBSchemaDTO;
import com.example.Backend.service.DBSchemaService;
import com.example.Backend.sql.ConnectionController;
import com.example.Backend.utils.RequestConstants;
import com.example.Backend.utils.ResponseObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * DBSchema Resource
 * This class provides the endpoints to interact with schemas
 */
@RestController
@RequestMapping("/dbschema")
public class DBSchemaResource
{
    @Autowired
    private DBSchemaService dbSchemaService;

    /**
     * Saves a new Schema and sends an HTTP Request to Fetcher in order to get the Metadata
     * ResponseObject allows to send multiple messages to frontend user
     * The schema name and connectionstring must be unique
     * @param request parameters of the request
     * @return response object containing information about the request
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("save")
    public ResponseObject<?> saveDBSchema(@RequestBody HashMap<String, String> request)
    {
        try{
               if(request.keySet().containsAll(Arrays.asList(RequestConstants.dbschemaSaverequirements)))
               {
                   DBSchemaDTO schemaDTO = getDbSchema(request);
                   if(dbSchemaService.findDBSchemaByName(schemaDTO.getName()) != null || dbSchemaService.findDBSchemaByConnectionString(schemaDTO.getConnectionstring()) != null)
                   {
                       return new ResponseObject<DBSchemaDTO>(409,"",
                               "Something went wrong!"
                               ,"Schema name or connection string already in use.",null);
                   }
                   else
                   {
                       if(checkDBConnection(schemaDTO) == 1)
                       {
                           dbSchemaService.saveDBSchema(schemaDTO);
                           sendUpdateRequest(schemaDTO);
                           return new ResponseObject<DBSchemaDTO>(201,"New schema inserted","","",null);
                       }
                       else
                       {
                           return new ResponseObject<DBSchemaDTO>(409, ""
                                   ,"Connection not established!"
                                   ,"Connection string, username or password may be wrong."
                                   ,null);
                       }
                   }
               }
               else
               {
                   return new ResponseObject<DBSchemaDTO>(400,"","Something went wrong!","Required parameters not found", null);

               }
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseObject<DBSchemaDTO>(500,"","Something went wrong!","Server error!", null);
    }

    /**
     * Sends HTTP Request to Fetcher in order to update schema
     * @param schemaDTO information about the schema
     * @throws JSONException error on json
     * @throws IOException error on html connection
     */
    private void sendUpdateRequest(DBSchemaDTO schemaDTO) throws JSONException, IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost updaterequest = new HttpPost(RequestConstants.fetcherURL+"/update/schema");
        updaterequest.addHeader("content-type", "application/json");

        JSONObject json = new JSONObject();
        json.put("name", schemaDTO.getName());
        json.put("connectionstring", schemaDTO.getConnectionstring());
        json.put("username", schemaDTO.getUsername());
        json.put("password", schemaDTO.getPassword());
        json.put("sqlservername", schemaDTO.getSqlservername());
        StringEntity params = new StringEntity(json.toString());
        updaterequest.setEntity(params);
        httpClient.execute(updaterequest);
    }

    /**
     * Transforms HashMap<String, String> in Schema DTO
     * @param request parameters of the request to be converted to SchemaDTO
     * @return schemaDTO
     */
    private DBSchemaDTO getDbSchema(HashMap<String, String> request) {
        DBSchemaDTO schema = new DBSchemaDTO();
        schema.setName(request.get("name"));
        schema.setSqlservername(request.get("sqlservername"));
        schema.setUsername(request.get("username"));
        schema.setPassword(request.get("password"));
        schema.setUpdateperiod(request.get("updateperiod"));
        schema.setConnectionstring(request.get("connectionstring"));
        if(request.containsKey("description"))
            schema.setDescription(request.get("description"));

        return schema;
    }

    /**
     * Establishes connection with the target Database in order to check if the schema information
     * (connection string, username and password) is valid
     * @param dto dbschemadto to check the connection
     * @return 1 if everything good; 0 otherwise
     */
    private int checkDBConnection(DBSchemaDTO dto)
    {
        String connectionstring = dto.getConnectionstring();
        String username = dto.getUsername();
        String password = dto.getPassword();
        int connectionvalidation = 0;
        ConnectionController connectionController = new ConnectionController(dto.getSqlservername(),connectionstring,username,password);

        if(connectionController.establishConnection() == 1)
        {
            connectionController.closeConnection();
            connectionvalidation = 1;
        }

        return connectionvalidation;
    }

    /**
     * Deletes a DBSchema from the database
     * @param dbschema name
     * @return Object confirming the result of the operation
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("delete/{dbschema}")
    public ResponseObject<?> deleteDBSchema(@PathVariable("dbschema") String dbschema){
        try{
            String schema_decoded = URLDecoder.decode(dbschema, StandardCharsets.UTF_8);
            DBSchemaDTO schemaDTO = this.dbSchemaService.findDBSchemaByName(schema_decoded);
            if(schemaDTO != null)
            {
                this.dbSchemaService.deleteDBSchema(schemaDTO.getConnectionstring());
                return new ResponseObject<DBSchemaDTO>(200,"Schema deleted!","","", null);
            }

            return new ResponseObject<DBSchemaDTO>(409,"","Something went wrong!",
                    "Schema " + schema_decoded + " not found!", null);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseObject<DBSchemaDTO>(500,"","Something went wrong!","Server error!", null);
    }

    /**
     * Updates a DBSchema
     * @param request
     * @return response with status code or error message
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("update")
    public ResponseObject<?> updateDBSchema(@RequestBody HashMap<String, String> request)
    {
        try{
            if(request.keySet().containsAll(Arrays.asList(RequestConstants.dbschemaUpdaterequirements)))
            {
                if(dbSchemaService.findDBSchemaByConnectionString(request.get("connectionstring")) != null)
                {
                    DBSchemaDTO schemaDTO = getDbSchema(request);
                    dbSchemaService.updateDBSchema(schemaDTO);
                    return new ResponseObject<DBSchemaDTO>(200,"Schema updated!","","", null);
                }
                return new ResponseObject<DBSchemaDTO>(409,"","Something went wrong!",
                        "Schema "+ request.get("name") + " not found!", null);
            }
            else
            {
                return new ResponseObject<DBSchemaDTO>(400,"","Something went wrong!","Required parameters not found", null);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ResponseObject<DBSchemaDTO>(500,"","Something went wrong!","Server error!", null);
    }

    /**
     * Updates a DBSchema credentials
     * @param request parameters
     * @return result of the update
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("update/credentials")
    public ResponseObject<?> updateDBSchemaCredentials(@RequestBody HashMap<String, String> request)
    {
        try{
            if(request.keySet().containsAll(Arrays.asList(RequestConstants.dbschemaUpdateCredentialsrequirements)))
            {
                if(dbSchemaService.findDBSchemaByName(request.get("name")) != null)
                {
                    DBSchemaDTO schemaDTO = getDbSchema(request);
                    if(checkDBConnection(schemaDTO) == 1)
                    {
                        dbSchemaService.updateDBSchemaCredentials(schemaDTO);
                        return new ResponseObject<DBSchemaDTO>(200, "Schema connection credentials updated","","",null);
                    }
                    else
                    {
                        return new ResponseObject<DBSchemaDTO>(409, ""
                                ,"Connection not established!"
                                ,"Connection String, username or password may be wrong."
                                ,null);                    }
                }

                return new ResponseObject<DBSchemaDTO>(409,"","Something went wrong!",
                        "Schema "+request.get("name") + " not found!",null);
            }
            else
            {
                return new ResponseObject<DBSchemaDTO>(400,"","Something went wrong!","Required parameters not found", null);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ResponseObject<DBSchemaDTO>(500,"","Something went wrong!","Server error!", null);
    }

    /**
     * Method to update schema content (sends request to fetcher)
     * @param request name of the schema
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("update/content")
    public ResponseEntity<?> updateDBSchemaContent(@RequestBody HashMap<String, String> request)
    {
        try{
            if(request.containsKey("name"))
            {
                String schemaname = request.get("name");
                DBSchemaDTO schemaDTO = this.dbSchemaService.findDBSchemaByNameWithCredentials(schemaname);
                if(schemaDTO != null)
                {
                    sendUpdateRequest(schemaDTO);
                    return new ResponseEntity<>("Schema updated", HttpStatus.OK);
                }
            }
            else
            {
                return new ResponseEntity<>("Required parameters not found!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Updates a DBSchema Last update time (accessed by fetcher in order to confirm that the update was successful)
     * @param request parameters
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("update/updatetime")
    public ResponseEntity<?> updateSchemaLastUpdatetime(@RequestBody HashMap<String, String> request)
    {
        if(request.keySet().containsAll(Arrays.asList(RequestConstants.dbschemaUpdateLastUpdateTime)))
        {
            try{
                DBSchemaDTO schemaDTO = this.dbSchemaService.findDBSchemaByConnectionString(request.get("connectionstring"));
                schemaDTO.setLastupdate(LocalDateTime.now());
                dbSchemaService.updateDBSchemaLastUpdate(schemaDTO);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }
        else return new ResponseEntity<>("Something failed",HttpStatus.CONFLICT);
    }

    /**
     * Returns a list with all dbschemas on DbSchema Manager database
     * @return list of all dbschemas
     */
    @GetMapping("")
    public List<DBSchemaDTO> findAllDBSchemas()
    {
        return this.dbSchemaService.findAll();
    }

    /**
     * Finds a Schema by its name
     * @param name name of the schema
     * @return Schema with name provided
     */
    @GetMapping("name/{name}")
    public DBSchemaDTO findSchemaByName(@PathVariable("name") String name)
    {
        try{
            String schema_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
            return this.dbSchemaService.findDBSchemaByName(schema_decoded);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Finds a Schema by its name showing its credentials
     * @param name name of the schema
     * @return schema with credentials not encrypted
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("name/{name}/credentials")
    public DBSchemaDTO findSchemaByNameWithCredentials(@PathVariable("name") String name)
    {
        try{
            String schema_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
            return this.dbSchemaService.findDBSchemaByNameWithCredentials(schema_decoded);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all schemas by update period
     * @param updateperiod period of update (daily, weekly, monthly)
     * @return List<DBSchemaDTO> list of schemas with a certain update period
     */
    @GetMapping("updateperiod/{updateperiod}")
    public List<DBSchemaDTO> findSchemasByUpdatePeriod(@PathVariable("updateperiod") String updateperiod)
    {
        return this.dbSchemaService.findDBSchemasByUpdatePeriod(updateperiod);
    }

    /**
     * Gets all schemas by update period with credentials visible
     * (Used by Fetcher to get schemas to update)
     * @param updateperiod update period
     * @return List with schemas with credentials not encrypted
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("updateperiod/{updateperiod}/credentials")
    public List<DBSchemaDTO> findSchemasByUpdatePeriodWithCredentials(@PathVariable("updateperiod") String updateperiod)
    {
        return this.dbSchemaService.findDBSchemasByUpdatePeriodWithCredentials(updateperiod);
    }
}