package com.example.Backend.dto.mapper;

import com.example.Backend.dto.model.DBObjectDTO;
import com.example.Backend.model.DBObject;

/**
 * Transforms a DBObject in a DBObjectDTO
 */
public class DBObjectMapper {
    public static DBObjectDTO toDBObjectDTO(DBObject dbo)
    {
        DBObjectDTO dbObjectDTO = new DBObjectDTO();
        dbObjectDTO.setName(dbo.getName());
        dbObjectDTO.setDbSchemaName(dbo.getDbschema().getName());
        dbObjectDTO.setStatus(dbo.getStatus());
        dbObjectDTO.setType(dbo.getType());
        dbObjectDTO.setDescription(dbo.getDescription());

        return dbObjectDTO;
    }
}
