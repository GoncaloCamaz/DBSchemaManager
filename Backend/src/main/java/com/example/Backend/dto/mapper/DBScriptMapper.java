package com.example.Backend.dto.mapper;

import com.example.Backend.dto.model.DBScriptDTO;
import com.example.Backend.model.DBScript;

/**
 * Transforms a DBScript in a DBScriptDTO
 */
public class DBScriptMapper
{
    public static DBScriptDTO toDBScriptDTO(DBScript dbs)
    {
        DBScriptDTO dbScriptDTO = new DBScriptDTO();
        dbScriptDTO.setCode(dbs.getCode());
        dbScriptDTO.setDate(dbs.getDate());
        dbScriptDTO.setDescription(dbs.getDescription());
        dbScriptDTO.setDefiner(dbs.getDefiner());
        dbScriptDTO.setDbObjectName(dbs.getDbobject().getName());
        dbScriptDTO.setDbSchemaName(dbs.getDbobject().getDbschema().getName());

        return dbScriptDTO;
    }
}
