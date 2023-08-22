package com.example.Backend.dto.mapper;

import com.example.Backend.dto.model.DBAccessDTO;
import com.example.Backend.model.DBAccess;

public class DBAccessMapper {

    public static DBAccessDTO toDBAccessDTO(DBAccess access)
    {
        DBAccessDTO accessDTO = new DBAccessDTO();
        accessDTO.setTimestamp(access.getTimestamp());
        accessDTO.setDescription(access.getDescription());
        accessDTO.setPermission(access.getPermission());
        accessDTO.setPrivilege(access.getPrivilege());
        accessDTO.setDbUsername(access.getDbUsername());
        accessDTO.setDbSchemaManagerUsername(access.getDbSchemaManagerUsername());
        accessDTO.setDbObjectName(access.getDbObjectName());
        accessDTO.setDbSchemaName(access.getDbSchemaName());

        return accessDTO;
    }
}
