package com.example.Backend.dto.mapper;

import com.example.Backend.dto.model.DBColumnDTO;
import com.example.Backend.model.DBColumn;

/**
 * Transforms a DBColumn in a DBColumnDTO
 */
public class DBColumnMapper {

    public static DBColumnDTO toDBColumnDTO(DBColumn dbc)
    {
        DBColumnDTO dbColumnDTO = new DBColumnDTO();
        dbColumnDTO.setName(dbc.getName());
        dbColumnDTO.setDatatype(dbc.getDatatype());
        dbColumnDTO.setDbObjectName(dbc.getDbobject().getName());
        dbColumnDTO.setDbSchemaName(dbc.getDbobject().getDbschema().getName());
        dbColumnDTO.setDescription(dbc.getDescription());
        dbColumnDTO.setNullable(dbc.getNullable());

        return dbColumnDTO;
    }
}
