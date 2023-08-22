package com.example.Backend.service.implementation;

import com.example.Backend.dto.mapper.DBSchemaManagerUserMapper;
import com.example.Backend.dto.model.DBSchemaManagerUserDTO;
import com.example.Backend.model.DBSchemaManagerRole;
import com.example.Backend.model.DBSchemaManagerUser;
import com.example.Backend.repository.DBSchemaManagerRoleRepo;
import com.example.Backend.repository.DBSchemaManagerUserRepo;
import com.example.Backend.service.DBSchemaManagerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DBSchemaManagerUserServiceImplementation implements DBSchemaManagerUserService
{
    @Autowired
    private DBSchemaManagerUserRepo userRepo;

    @Autowired
    private DBSchemaManagerRoleRepo roleRepo;

    /**
     * This method saves a new user
     * Please note that the password is not set. Corporate users are authenticated via ldap server
     * Only fetcher_service needs to authenticate with a password that is stored on dbschemamanager database
     * @param userDTO user dto
     * @return object saved
     */
    @Override
    public DBSchemaManagerUserDTO saveUser(DBSchemaManagerUserDTO userDTO) {
        try {
            DBSchemaManagerRole userRole = roleRepo.findRoleByName(userDTO.getRole());
            if(userRole != null)
            {
                DBSchemaManagerUser user = new DBSchemaManagerUser();
                user.setUsername(userDTO.getUsername());
                user.setUserRole(userRole);

                return DBSchemaManagerUserMapper.toDBSchemaManagerUserDTO(userRepo.save(user));
            }
           return null;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateUser(DBSchemaManagerUserDTO userDTO) {
        try {
            DBSchemaManagerRole userRole = roleRepo.findRoleByName(userDTO.getRole());
            if(userRole != null)
            {
                DBSchemaManagerUser user = userRepo.findByUsername(userDTO.getUsername());
                if(user != null)
                {
                    user.setUserRole(userRole);
                    userRepo.save(user);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(DBSchemaManagerUserDTO userDTO) {
        DBSchemaManagerUser user = userRepo.findByUsername(userDTO.getUsername());
        if(user != null)
        {
            userRepo.delete(user);
        }
    }

    @Override
    public DBSchemaManagerUserDTO findByUsername(String username) {
        DBSchemaManagerUser user = userRepo.findByUsername(username);
        return user != null ? DBSchemaManagerUserMapper.toDBSchemaManagerUserDTO(user) : null;
    }

    @Override
    public List<DBSchemaManagerUserDTO> userList() {
        List<DBSchemaManagerUserDTO> userDTOS = new ArrayList<>();
        List<DBSchemaManagerUser> users = userRepo.findAll();

        for(DBSchemaManagerUser user : users)
        {
            userDTOS.add(DBSchemaManagerUserMapper.toDBSchemaManagerUserDTO(user));
        }

        return userDTOS;
    }

    @Override
    public DBSchemaManagerRole findDBSchemaManagerRoleByName(String role) {
        return roleRepo.findRoleByName(role);
    }

    @Override
    public DBSchemaManagerRole saveRole(DBSchemaManagerRole role) {
        return roleRepo.save(role);
    }
}
