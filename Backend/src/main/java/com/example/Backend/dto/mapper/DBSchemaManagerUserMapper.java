package com.example.Backend.dto.mapper;

import com.example.Backend.dto.model.DBSchemaManagerUserDTO;
import com.example.Backend.model.DBSchemaManagerUser;

/**
 * Transforms a DBSchemaManager in a DBSchemaManagerDTO
 */
public class DBSchemaManagerUserMapper {
    public static DBSchemaManagerUserDTO toDBSchemaManagerUserDTO(DBSchemaManagerUser user)
    {
        DBSchemaManagerUserDTO DBSchemaManagerUserDTO = new DBSchemaManagerUserDTO();
        DBSchemaManagerUserDTO.setUsername(user.getUsername());
        DBSchemaManagerUserDTO.setPassword(user.getPassword());
        DBSchemaManagerUserDTO.setRole(user.getUserRole().getName());

        return DBSchemaManagerUserDTO;
    }
}
