package com.example.Backend.dto.mapper;

import com.example.Backend.dto.model.DBSchemaDTO;
import com.example.Backend.model.DBSchema;

/**
 * Transforms a DBSchema in a DBSchemaDTO
 */
public class DBSchemaMapper {
    public static DBSchemaDTO toDBSchemaDTO(DBSchema dbs)
    {
        DBSchemaDTO dbSchemaDTO = new DBSchemaDTO();
        dbSchemaDTO.setName(dbs.getName());
        dbSchemaDTO.setConnectionstring(dbs.getConnectionstring());
        dbSchemaDTO.setUsername(dbs.getUsername());
        dbSchemaDTO.setPassword(dbs.getPassword());
        dbSchemaDTO.setLastupdate(dbs.getLastupdate());
        dbSchemaDTO.setSqlservername(dbs.getSqlservername());
        dbSchemaDTO.setUpdateperiod(dbs.getUpdateperiod());
        dbSchemaDTO.setDescription(dbs.getDescription());

        return dbSchemaDTO;
    }
}
