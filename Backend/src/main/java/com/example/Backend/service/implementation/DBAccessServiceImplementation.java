package com.example.Backend.service.implementation;

import com.example.Backend.dto.mapper.DBAccessMapper;
import com.example.Backend.dto.model.DBAccessDTO;
import com.example.Backend.dto.model.ResultSetDTO;
import com.example.Backend.model.DBAccess;
import com.example.Backend.model.DBObject;
import com.example.Backend.model.DBSchema;
import com.example.Backend.repository.DBAccessRepo;
import com.example.Backend.repository.DBObjectRepo;
import com.example.Backend.repository.DBSchemaRepo;
import com.example.Backend.service.DBAccessService;
import com.example.Backend.sql.ConnectionController;
import com.example.Backend.utils.PasswordEncryptor;
import com.example.Backend.utils.PasswordEncryptorImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DBAccessServiceImplementation implements DBAccessService {

    @Autowired
    private DBAccessRepo accessRepo;

    @Autowired
    private DBObjectRepo dbObjectRepo;

    @Autowired
    private DBSchemaRepo dbSchemaRepo;

    /**
     * Sends new access to target database
     * If the operation is successful, the record is register on dbschemamanager database
     * @param dto request from frontend
     * @return result of the request that was sent to target database
     */
    @Override
    public ResultSetDTO sendAccessToTarget(DBAccessDTO dto) {
        ResultSetDTO resultSetDTO = new ResultSetDTO();
        DBSchema schema = this.dbSchemaRepo.findDB_SchemaByName(dto.getDbSchemaName());
        if(schema != null)
        {
            DBObject dbo = this.dbObjectRepo.findDBObjectBySchemaNameByDBObject(schema.getName(), dto.getDbObjectName());
            if(dbo != null)
            {
                PasswordEncryptor encryptor = new PasswordEncryptorImplementation();
                ConnectionController connectionController = new ConnectionController(schema.getSqlservername(), schema.getConnectionstring(),
                        schema.getUsername(), encryptor.decryptDataBaseObjectPassword(schema.getPassword()));

                String dbotype = dbo.getType();
                if(connectionController.establishConnection() == 1)
                {
                    resultSetDTO = connectionController.grantPrivilege(dto.getPermission(),dbo.getName(),dbotype,dto.getDbUsername(), dto.getPrivilege());
                    connectionController.closeConnection();
                    if(resultSetDTO.isValidQuery())
                    {
                        saveAccessToDBSchemaManagerDatabase(dto);
                    }
                }
                else
                {
                    resultSetDTO.setErrormessage("Connection failed!");
                }
            }
        }

        return resultSetDTO;
    }

    @Override
    public void saveAccessToDBSchemaManagerDatabase(DBAccessDTO dto) {
        try{
            DBAccess access = getDBAccess(dto);
            DBAccessMapper.toDBAccessDTO(this.accessRepo.save(access));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private DBAccess getDBAccess(DBAccessDTO dto) {
        DBAccess dba = new DBAccess();
        dba.setDescription(dto.getDescription());
        dba.setPrivilege(dto.getPrivilege());
        dba.setDbObjectName(dto.getDbObjectName());
        dba.setDbUsername(dto.getDbUsername());
        dba.setDbSchemaName(dto.getDbSchemaName());
        dba.setDbSchemaManagerUsername(dto.getDbSchemaManagerUsername());
        dba.setPermission(dto.getPermission());
        dba.setTimestamp(dto.getTimestamp());

        return dba;
    }

    @Override
    public void updateDBAccess(DBAccessDTO accessDTO) {
        DBAccess dbAccess = this.accessRepo.findBySchemaObjectUsernameAccessTypeAndTimeStamp(accessDTO.getDbSchemaName(),
                accessDTO.getDbObjectName(), accessDTO.getDbUsername(), accessDTO.getPrivilege(), accessDTO.getTimestamp());

        if(dbAccess != null)
        {
            dbAccess.setDescription(accessDTO.getDescription());
            try{
                this.accessRepo.save(dbAccess);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteDBAccess(String schema, String object, String username, String accesstype, LocalDateTime timestamp) {
        DBAccess access = this.accessRepo.findBySchemaObjectUsernameAccessTypeAndTimeStamp(schema,object,username,accesstype,timestamp);
        if(access != null)
        {
            try{
                this.accessRepo.delete(access);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<DBAccessDTO> findAll() {
        List<DBAccess> accessList = this.accessRepo.findAll();
        List<DBAccessDTO> accessDTOList = new ArrayList<>();

        for(DBAccess access : accessList)
        {
            accessDTOList.add(DBAccessMapper.toDBAccessDTO(access));
        }
        return accessDTOList;
    }

    @Override
    public List<DBAccessDTO> findAllBySchema(String dbschema) {
        List<DBAccess> accessList = this.accessRepo.findAllByDbSchemaName(dbschema);
        List<DBAccessDTO> accessDTOList = new ArrayList<>();

        for(DBAccess access : accessList)
        {
            accessDTOList.add(DBAccessMapper.toDBAccessDTO(access));
        }
        return accessDTOList;
    }
}
