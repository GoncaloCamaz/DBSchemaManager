package com.example.Backend.service;

import com.example.Backend.dto.model.DBSchemaDTO;

import java.util.List;

public interface DBSchemaService
{
    DBSchemaDTO saveDBSchema(DBSchemaDTO schema);

    void updateDBSchema(DBSchemaDTO schema);

    void updateDBSchemaLastUpdate(DBSchemaDTO dbSchemaDTO);

    void updateDBSchemaCredentials(DBSchemaDTO schemaDTO);

    void deleteDBSchema(String connectionString);

    List<DBSchemaDTO> findAll();

    DBSchemaDTO findDBSchemaByName(String name);

    DBSchemaDTO findDBSchemaByConnectionString(String schema_connectionstring);

    List<DBSchemaDTO> findDBSchemasByPlatformURL(String plataform_URL);

    List<DBSchemaDTO> findDBSchemasByUpdatePeriod(String updateperiod);

    DBSchemaDTO findDBSchemaByNameWithCredentials(String dbSchemaName);

    List<DBSchemaDTO> findDBSchemasByUpdatePeriodWithCredentials(String updateperiod);

    List<DBSchemaDTO> findDBSchemasByPlatformName(String platform_decoded);
}
