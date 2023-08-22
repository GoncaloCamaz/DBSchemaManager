package com.example.Backend.service;

import com.example.Backend.dto.model.DBColumnDTO;

import java.util.List;

public interface DBColumnService
{
    DBColumnDTO saveDBColumn(DBColumnDTO dbColumn);

    void updateDBColumn(DBColumnDTO dbColumn);

    void deleteDBColumn(String schema, String object, String dbcolumn);

    List<DBColumnDTO> findAll();

    DBColumnDTO findBySchemaObjectAndColumn(String dbschemaname, String dbobjectname, String dbcolumn);

    List<DBColumnDTO> findDBColumnByName(String name);

    List<DBColumnDTO> findDBColumnsByDataType(String dataType);

    List<DBColumnDTO> findByDBObjectName(String schema, String name);

    List<DBColumnDTO> findByDBObjectName(String name);

    List<DBColumnDTO> findByDBSchemaName(String name);
}
