package com.example.Backend.resource;

import com.example.Backend.dto.model.DBColumnDTO;
import com.example.Backend.service.DBColumnService;
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
 * API endpoint to interact with dbcolumn entity.
 *
 * Please note that url get method variables must be decoded because they may contain spaces that are encoded
 * since URLs cannot contain spaces
 */
@RestController
@RequestMapping("/dbcolumn")
public class DBColumnResource
{
    @Autowired
    private DBColumnService dbColumnService;

    /**
     * Saves a new DBColumn
     * Please note that both in dbcolumnresource and dbobject resource, crud operations endpoints dont answer with
     * a ResponseObject because there is no useful information to show to an user since these operations are only
     * done by the Fetcher service
     * @param request with parameters
     * @return ResponseEntity
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("save")
    public ResponseEntity<?> saveDBColumn(@RequestBody HashMap<String, String> request)
    {
        try{
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.dbcolumnSaveRequirements)))
                return new ResponseEntity<>("Some required parameters were null", HttpStatus.BAD_REQUEST);

            DBColumnDTO dbColumn = getDbColumn(request);
            if(dbColumnService.findBySchemaObjectAndColumn(dbColumn.getDbSchemaName(), dbColumn.getDbObjectName(), dbColumn.getName()) == null)
            {
                dbColumnService.saveDBColumn(dbColumn);
                return new ResponseEntity<>("New column inserted", HttpStatus.CREATED);
            }
            else
            {
                return new ResponseEntity<>("Column already exists on given schema and object.", HttpStatus.CONFLICT);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong while inserting the column", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Transforms parameters from HTTP Request on a DBColumnDTO
     * @param request params
     * @return DBCOlumnDTO
     */
    private DBColumnDTO getDbColumn(HashMap<String, String> request) {
        DBColumnDTO dbColumn = new DBColumnDTO();
        dbColumn.setDatatype(request.get("datatype"));
        dbColumn.setName(request.get("name"));
        dbColumn.setDbSchemaName(request.get("dbSchemaName"));
        dbColumn.setDbObjectName(request.get("dbObjectName"));
        if(request.containsKey("description"))
        {
            dbColumn.setDescription(request.get("description"));
        }
        if(request.containsKey("nullable"))
            dbColumn.setNullable(request.get("nullable"));

        return dbColumn;
    }

    /**
     * Updates a dbcolumn
     * @param request parameters
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("update")
    public ResponseEntity<?> updateDBColumn(@RequestBody HashMap<String, String> request)
    {
        try{
            DBColumnDTO column = getDbColumn(request);
            this.dbColumnService.updateDBColumn(column);
            return new ResponseEntity<>("DBColumn updated", HttpStatus.OK);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong while updating the column", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Deletes a dbcolumn
     * @param schema schema name
     * @param object object name
     * @param dbcolumn column name
     * @return response
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("delete/{schema_name}/{dbobject_name}/{dbcolumn_name}")
    public ResponseEntity<?> deleteDBColumn(@PathVariable("schema_name") String schema, @PathVariable("dbobject_name") String object,
                                            @PathVariable("dbcolumn_name") String dbcolumn)
    {
        try{
            String schema_decoded = URLDecoder.decode(schema, StandardCharsets.UTF_8);
            String object_decoded = URLDecoder.decode(object, StandardCharsets.UTF_8);
            String column_decoded = URLDecoder.decode(dbcolumn, StandardCharsets.UTF_8);
            this.dbColumnService.deleteDBColumn(schema_decoded, object_decoded, column_decoded);
            return new ResponseEntity<>("DBColumn deleted", HttpStatus.OK);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong while deleting the column", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Lists all dbcolumns available
     * @return List<DBColumnDTO>
     */
    @GetMapping("")
    public List<DBColumnDTO> getAllColumns()
    {
        return this.dbColumnService.findAll();
    }

    /**
     * Finds all dbcolumns with the name specified in the url {name}.
     * Note that GetMapping(...{name}) and @PathVariable("name") must match
     * @param name of the column
     * @return list of columns
     */
    @GetMapping("name/{name}")
    public List<DBColumnDTO> getColumnByName(@PathVariable("name") String name)
    {
        try{
            String column_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
            return this.dbColumnService.findDBColumnByName(column_decoded);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Lists all dbcolumns by their data type
     * @param datatype data type of the column
     * @return list o columns
     */
    @GetMapping("datatype/{datatype}")
    public List<DBColumnDTO> getColumnsByDataType(@PathVariable("datatype") String datatype)
    {
        try{
            String column_decoded = URLDecoder.decode(datatype, StandardCharsets.UTF_8);
            return this.dbColumnService.findDBColumnsByDataType(column_decoded);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Lists all dbcolumns by a schema dbSchemaName and a dbobject dbObjectName
     * @param schema_name name of the schema
     * @param name name of the dbobect
     * @return list of dbcolumns
     */
    @GetMapping("dbschema/{dbSchemaName}/dbobject/{dbObjectName}")
    public List<DBColumnDTO> getColumnsByDBObjectName(@PathVariable("dbSchemaName") String schema_name, @PathVariable("dbObjectName") String name)
    {
        try{
            String schema_decoded = URLDecoder.decode(schema_name, StandardCharsets.UTF_8);
            String dbobject_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
            return this.dbColumnService.findByDBObjectName(schema_decoded,dbobject_decoded);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Lists all columns from dbobject dbObjectName
     * @param name of the dbobject
     * @return list of columns
     */
    @GetMapping("dbobject/{dbObjectName}")
    public List<DBColumnDTO> getColumnsByDBObjectName(@PathVariable("dbObjectName") String name)
    {
        try{
            String dbobject_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
            return this.dbColumnService.findByDBObjectName(dbobject_decoded);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Lists all columns from dbschema dbSchemaName
     * @param name of the schema
     * @return list of dbcolumns
     */
    @GetMapping("dbschema/{dbSchemaName}")
    public List<DBColumnDTO> getColumnsByDBSchemaName(@PathVariable("dbSchemaName") String name)
    {
        try{
            String schema_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
            return this.dbColumnService.findByDBSchemaName(schema_decoded);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
