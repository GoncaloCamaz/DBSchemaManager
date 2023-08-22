package com.example.Backend.resource;

import com.example.Backend.dto.model.DBSchemaDTO;
import com.example.Backend.dto.model.ResultSetDTO;
import com.example.Backend.service.DBSchemaService;
import com.example.Backend.sql.ConnectionController;
import com.example.Backend.sql.SQLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Endpoints for send queries to target databases
 */
@RestController
@RequestMapping("/targetdatabase")
public class FreeQueryResource {

    @Autowired
    private DBSchemaService dbSchemaService;

    /**
     * Send query entrypoint
     * @param request parameters query and schema name
     * @return query result
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("query")
    public ResponseEntity<?> sendQuery(@RequestBody HashMap<String, String> request) {
        String query = request.get("query");
        String schema = request.get("schema");

        DBSchemaDTO sch = dbSchemaService.findDBSchemaByNameWithCredentials(schema);
        if (sch != null) {
            ResultSetDTO resultSetDTO = sendQueryToTarget(sch, sch.getUsername(), sch.getPassword(), query);
            return new ResponseEntity<>(resultSetDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Schema doesnt exist", HttpStatus.CONFLICT);
        }
    }

    /**
     * Method that sends the query. if the connection is established, the query is sent
     * @param sch name of the schema
     * @param username username of schema connection
     * @param password password of shema connection
     * @param query query inserted by the user
     * @return object with the result of the query sent
     */
    private ResultSetDTO sendQueryToTarget(DBSchemaDTO sch, String username, String password, String query) {
        ConnectionController connectionController = new ConnectionController(sch.getSqlservername(), sch.getConnectionstring(),
                username, password);
        ResultSetDTO resultSetDTO = new ResultSetDTO();

        try {
            if (connectionController.establishConnection() == 1) {
                resultSetDTO = connectionController.sendQueryToTargetDB(query);
                connectionController.closeConnection();
            }
            else
            {
                resultSetDTO.setErrormessage("Connection to " + sch.getName() +" not established!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultSetDTO;
    }

    /**
     * Lists all db users from a target database.
     * @param request parameters
     * @return list with users of target database
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("users")
    public ResponseEntity<?> fetchAllUsers(@RequestBody HashMap<String, String> request) {
        String schema = request.get("schema");

        DBSchemaDTO sch = dbSchemaService.findDBSchemaByNameWithCredentials(schema);
        if (sch != null) {
            SQLUtils utils = new SQLUtils(sch.getSqlservername());
            return new ResponseEntity<>(sendQueryToTarget(sch, sch.getUsername(), sch.getPassword(),utils.fetchAllUsersQuery()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Schema doesnt exist", HttpStatus.CONFLICT);
        }
    }
}