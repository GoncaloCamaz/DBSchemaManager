package com.example.Backend.service;

import com.example.Backend.dto.model.DBSchemaManagerUserDTO;
import com.example.Backend.model.DBSchemaManagerRole;

import java.util.List;

public interface DBSchemaManagerUserService
{
    void updateUser(DBSchemaManagerUserDTO userDTO);

    DBSchemaManagerUserDTO saveUser(DBSchemaManagerUserDTO userDTO);

    void deleteUser(DBSchemaManagerUserDTO userDTO);

    DBSchemaManagerUserDTO findByUsername(String username);

    List<DBSchemaManagerUserDTO> userList();

    DBSchemaManagerRole findDBSchemaManagerRoleByName(String role);

    DBSchemaManagerRole saveRole(DBSchemaManagerRole role);
}
