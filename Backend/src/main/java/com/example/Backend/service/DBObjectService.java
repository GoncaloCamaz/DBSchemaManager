package com.example.Backend.service;

import com.example.Backend.dto.model.DBObjectDTO;
import java.util.List;

public interface DBObjectService
{
    DBObjectDTO saveDBObject(DBObjectDTO dbObject);

    void deleteDBObject(DBObjectDTO dbObject);

    void updateDBObject(DBObjectDTO dbObject);

    List<DBObjectDTO> findAll();

    List<DBObjectDTO> findDBObjectsBySchemaName(String schemaname);

    List<DBObjectDTO> findDBObjectsBySchemaConnectionString(String connectionString);

    List<DBObjectDTO> findDBObjectsByTypeBySchemaName(String schemaname, String dbo_type);

    List<DBObjectDTO> findDBObjectsByType(String type);

    DBObjectDTO findDBObjectByNameBySchemaName(String dbSchemaName, String name);
}
