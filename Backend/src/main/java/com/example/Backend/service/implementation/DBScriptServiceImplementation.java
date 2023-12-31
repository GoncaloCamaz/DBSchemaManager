package com.example.Backend.service.implementation;

import com.example.Backend.dto.mapper.DBScriptMapper;
import com.example.Backend.dto.model.DBScriptDTO;
import com.example.Backend.dto.model.ResultSetDTO;
import com.example.Backend.model.DBObject;
import com.example.Backend.model.DBSchema;
import com.example.Backend.model.DBScript;
import com.example.Backend.repository.DBObjectRepo;
import com.example.Backend.repository.DBSchemaRepo;
import com.example.Backend.repository.DBScriptRepo;
import com.example.Backend.service.DBScriptService;
import com.example.Backend.sql.ConnectionController;
import com.example.Backend.sql.SQLUtils;
import com.example.Backend.utils.RequestConstants;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DBScriptServiceImplementation implements DBScriptService {

    @Autowired
    private DBScriptRepo dbScriptRepo;

    @Autowired
    private DBObjectRepo dbObjectRepo;

    @Autowired
    private DBSchemaRepo dbSchemaRepo;

    @Override
    public DBScriptDTO saveScript(DBScriptDTO dbScriptDTO) {
        DBObject dbo = dbObjectRepo.findDBObjectBySchemaNameByDBObject(dbScriptDTO.getDbSchemaName(), dbScriptDTO.getDbObjectName());
        if(dbo != null)
        {
            DBScript script = new DBScript();
            script.setDbobject(dbo);
            script.setCode(dbScriptDTO.getCode());
            script.setDescription(dbScriptDTO.getDescription());
            script.setDate(dbScriptDTO.getDate());
            script.setDefiner(dbScriptDTO.getDefiner());
            try {
                return DBScriptMapper.toDBScriptDTO(dbScriptRepo.save(script));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void deleteScript(DBScriptDTO dbScript) {
        DBScript script = dbScriptRepo.findByDBObjectByDBScriptDate(dbScript.getDbObjectName(), dbScript.getDbSchemaName(), dbScript.getDate());

        try {
            dbScriptRepo.delete(script);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void updateScript(DBScriptDTO dbScript) {
        DBScript dbs = dbScriptRepo.findByDBObjectByDBScriptDate(dbScript.getDbObjectName(), dbScript.getDbSchemaName(), dbScript.getDate());

        if(dbs != null)
        {
            dbs.setDescription(dbScript.getDescription());

            try {
                dbScriptRepo.save(dbs);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<DBScriptDTO> findAll() {
        List<DBScriptDTO> list = new ArrayList<>();
        List<DBScript> scriptList = dbScriptRepo.findAll();
        for(DBScript scr : scriptList)
        {
            list.add(DBScriptMapper.toDBScriptDTO(scr));
        }

        return list;
    }

    @Override
    public DBScriptDTO findLatestScript(String schemaname, String dbobject) {
        List<DBScript> scriptList = dbScriptRepo.findLatestScript(dbobject, schemaname);
        if(scriptList.size() > 0)
        {
            return DBScriptMapper.toDBScriptDTO(scriptList.get(0));
        }

        return null;
    }

    /**
     *  Sends a script to the target database
     * @param script DBScript object
     * @param definer dbschema manager user
     * @param username schema optional username
     * @param password schema optional password
     * @param override at frontend, if th user decides to send the script to target database that is not fetched yet
     * the override boolean will be true
     * @return data generated by the view creation. It will only return 100 rows
     */
    @Override
    public ResultSetDTO sendScriptToTarget(DBScriptDTO script, String definer, String username, String password, boolean override) {
        DBSchema schema = this.dbSchemaRepo.findDB_SchemaByName(script.getDbSchemaName());
        ResultSetDTO result = new ResultSetDTO();
        String viewname = getViewName(schema, script);
        DBScriptDTO storedScript = findLatestScript(schema.getName(),viewname);

        ConnectionController connector = new ConnectionController(schema.getSqlservername(),schema.getConnectionstring(), username,password);
        if(connector.establishConnection() != -1)
        {
            if(!override)
            {
                result = connector.createOrReplaceNewView(viewname, script.getCode(), storedScript);
            }
            else
            {
                result = connector.sendViewQueryToTargetDB(viewname, script.getCode());
            }

            if(result.isValidQuery())
            {
                try {
                    sendUpdateRequest(schema.getName(),schema.getConnectionstring(),username,password,schema.getSqlservername(),viewname, definer);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
            connector.closeConnection();
        }
        else
        {
            result.setErrormessage("Connection to "+ schema.getName()  +"not established");
        }

        return result;
    }

    /**
     * Sends HTTP Request to Fetcher in order to update view
     * @throws JSONException
     * @throws IOException
     */
    private void sendUpdateRequest(String schemaname, String schemaconnectionstring,
                                   String username, String password, String sql,
                                   String dbobject, String definer) throws JSONException, IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost updaterequest = new HttpPost(RequestConstants.fetcherURL+"/update/schema/view");
        updaterequest.addHeader("content-type", "application/json");
        JSONObject json = new JSONObject();
        json.put("name", schemaname);
        json.put("connectionstring", schemaconnectionstring);
        json.put("username", username);
        json.put("password", password);
        json.put("sqlservername",sql);
        json.put("viewname",dbobject);
        json.put("definer",definer);
        StringEntity params = new StringEntity(json.toString());
        updaterequest.setEntity(params);
        httpClient.execute(updaterequest);
    }

    private String getViewName(DBSchema dbSchema, DBScriptDTO script) {
        String sqlservertype = dbSchema.getSqlservername();
        SQLUtils sqlUtils = new SQLUtils(sqlservertype);

        return sqlUtils.getViewNameFromCreateViewStatement(script.getCode());
    }

    @Override
    public String getViewQuery(DBScriptDTO script, String username, String password) {
        DBSchema schema = this.dbSchemaRepo.findDB_SchemaByName(script.getDbSchemaName());
        String query;
        ConnectionController connector = new ConnectionController(schema.getSqlservername(),schema.getConnectionstring(), username,password);
        connector.establishConnection();
        query = connector.fetchViewScript(script.getDbSchemaName(),script.getDbObjectName());
        connector.closeConnection();

        return query;
    }

    @Override
    public List<DBScriptDTO> findAllLatest() {
        List<DBScript> scripts = this.dbScriptRepo.findAllByMostRecentRecord();
        List<DBScriptDTO> scriptDTOS = new ArrayList<>();
        for(DBScript s : scripts)
        {
            scriptDTOS.add(DBScriptMapper.toDBScriptDTO(s));
        }

        return scriptDTOS;
    }

    @Override
    public List<DBScriptDTO> findAllOldestByDBObject(String schema, String dbObject) {
        List<DBScript> scripts = this.dbScriptRepo.findAllByDBObject(schema, dbObject);
        scripts.remove(0);

        List<DBScriptDTO> scriptDTOS = new ArrayList<>();

        for(DBScript script : scripts)
        {
            scriptDTOS.add(DBScriptMapper.toDBScriptDTO(script));
        }

        return scriptDTOS;
    }

    @Override
    public List<DBScriptDTO> findAllLatestByType(String type) {
        List<DBScript> scripts = this.dbScriptRepo.findAllByMostRecentRecord();
        List<DBScriptDTO> scriptDTOS = new ArrayList<>();
        for(DBScript s : scripts)
        {
            if(s.getDbobject().getType().equals(type))
                scriptDTOS.add(DBScriptMapper.toDBScriptDTO(s));
        }

        return scriptDTOS;
    }

    @Override
    public List<DBScriptDTO> findLatestScriptByType(String type, String name, String dbo_name) {
        List<DBScript> scripts = this.dbScriptRepo.findAllByDBObjectByType(type,name, dbo_name);
        scripts.remove(0);
        List<DBScriptDTO> scriptDTOS = new ArrayList<>();

        for(DBScript script : scripts)
        {
            scriptDTOS.add(DBScriptMapper.toDBScriptDTO(script));
        }

        return scriptDTOS;
    }

    @Override
    public List<DBScriptDTO> findAllLatestByTypeBySchema(String type, String schema) {
        List<DBScript> scripts = this.dbScriptRepo.findAllByMostRecentRecord();
        List<DBScriptDTO> scriptDTOS = new ArrayList<>();
        for(DBScript s : scripts)
        {
            if(s.getDbobject().getType().equals(type) && s.getDbobject().getDbschema().getName().equals(schema))
                scriptDTOS.add(DBScriptMapper.toDBScriptDTO(s));
        }

        return scriptDTOS;
    }
}
