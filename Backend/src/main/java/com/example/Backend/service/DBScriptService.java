package com.example.Backend.service;

import com.example.Backend.dto.model.DBScriptDTO;
import com.example.Backend.dto.model.ResultSetDTO;

import java.util.List;

public interface DBScriptService
{
    DBScriptDTO saveScript(DBScriptDTO DBScript);

    void deleteScript(DBScriptDTO DBScript);

    void updateScript(DBScriptDTO DBScript);

    List<DBScriptDTO> findAll();

    DBScriptDTO findLatestScript(String schemaname, String dbobject);

    ResultSetDTO sendScriptToTarget(DBScriptDTO script, String user, String username, String password, boolean overrideversion);

    String getViewQuery(DBScriptDTO script, String username, String password);

    List<DBScriptDTO> findAllLatest();

    List<DBScriptDTO> findAllOldestByDBObject(String schema, String dbObject);

    List<DBScriptDTO> findAllLatestByType(String type);

    List<DBScriptDTO> findLatestScriptByType(String type, String name, String dbo_name);

    List<DBScriptDTO> findAllLatestByTypeBySchema(String type_decoded, String schema_decoded);
}
