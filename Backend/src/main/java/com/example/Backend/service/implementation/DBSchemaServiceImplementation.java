package com.example.Backend.service.implementation;

import com.example.Backend.dto.mapper.DBSchemaMapper;
import com.example.Backend.dto.model.DBSchemaDTO;
import com.example.Backend.model.DBSchema;
import com.example.Backend.model.Platform;
import com.example.Backend.repository.DBSchemaRepo;
import com.example.Backend.repository.PlatformRepo;
import com.example.Backend.service.DBSchemaService;
import com.example.Backend.utils.EnumConverter;
import com.example.Backend.utils.PasswordEncryptor;
import com.example.Backend.utils.PasswordEncryptorImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class DBSchemaServiceImplementation implements DBSchemaService {

    private PasswordEncryptor encryptor;

    @Autowired
    private DBSchemaRepo dbSchemaRepo;

    @Autowired
    private PlatformRepo platformRepo;

    @Override
    public DBSchemaDTO saveDBSchema(DBSchemaDTO schemaDTO) {
        Platform platform = platformRepo.findByName(schemaDTO.getPlatformname());
        if(platform != null)
        {
            encryptor = new PasswordEncryptorImplementation();
            DBSchema dbSchema = new DBSchema();
            dbSchema.setPlatform(platform);
            dbSchema.setName(schemaDTO.getName());
            dbSchema.setConnectionstring(schemaDTO.getConnectionstring());
            dbSchema.setUsername(schemaDTO.getUsername());
            dbSchema.setPassword(encryptor.encryptDataBaseObjectPassword(schemaDTO.getPassword()));
            dbSchema.setDescription(schemaDTO.getDescription());
            dbSchema.setUpdateperiod(EnumConverter.getUpdatePeriod(schemaDTO.getUpdateperiod()));
            dbSchema.setSqlservername(EnumConverter.getMySQLServerType(schemaDTO.getSqlservername()));
            dbSchema.setDbObjectSet(new HashSet<>());

            try {
                return DBSchemaMapper.toDBSchemaDTO(dbSchemaRepo.save(dbSchema));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This method only updates description, update period, name, platform and sqlservername.
     * @param schemaDTO object schema with all informations
     */
    @Override
    public void updateDBSchema(DBSchemaDTO schemaDTO) {
        DBSchema dbSchema = dbSchemaRepo.findDBSchemaByConnectionString(schemaDTO.getConnectionstring());
        Platform platform = platformRepo.findByName(schemaDTO.getPlatformname());
        if(dbSchema != null)
        {
            if(platform != null)
            {
                dbSchema.setPlatform(platform);
            }
            dbSchema.setDescription(schemaDTO.getDescription());
            dbSchema.setUpdateperiod(EnumConverter.getUpdatePeriod(schemaDTO.getUpdateperiod()));
            dbSchema.setSqlservername(EnumConverter.getMySQLServerType(schemaDTO.getSqlservername()));
            dbSchema.setName(schemaDTO.getName());
            try {
                dbSchemaRepo.save(dbSchema);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method only updates description, update period, name, platform and sqlservername.
     * @param schemaDTO object schema with all informations
     */
    @Override
    public void updateDBSchemaLastUpdate(DBSchemaDTO schemaDTO) {
        DBSchema dbSchema = dbSchemaRepo.findDBSchemaByConnectionString(schemaDTO.getConnectionstring());
        if(dbSchema != null)
        {
            dbSchema.setLastupdate(schemaDTO.getLastupdate());
            try {
                dbSchemaRepo.save(dbSchema);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateDBSchemaCredentials(DBSchemaDTO schemaDTO) {
        DBSchema dbSchema = dbSchemaRepo.findDBSchemaByConnectionString(schemaDTO.getConnectionstring());
        if(dbSchema != null)
        {
            encryptor = new PasswordEncryptorImplementation();
            dbSchema.setPassword(encryptor.encryptDataBaseObjectPassword(schemaDTO.getPassword()));
            dbSchema.setUsername(schemaDTO.getUsername());
            dbSchema.setConnectionstring(schemaDTO.getConnectionstring());
            try {
                dbSchemaRepo.save(dbSchema);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteDBSchema(String connectionString) {
        DBSchema schema = dbSchemaRepo.findDBSchemaByConnectionString(connectionString);
        if(schema != null)
        {
            try {
                this.dbSchemaRepo.delete(schema);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<DBSchemaDTO> findAll() {
        List<DBSchemaDTO> list = new ArrayList<>();
        List<DBSchema> schemaList = dbSchemaRepo.findAll();

        for(DBSchema schema : schemaList)
        {
            list.add(DBSchemaMapper.toDBSchemaDTO(schema));
        }

        return list;
    }

    @Override
    public DBSchemaDTO findDBSchemaByName(String name) {
        DBSchema schema = dbSchemaRepo.findDB_SchemaByName(name);

        return schema != null ? DBSchemaMapper.toDBSchemaDTO(schema) : null;
    }

    @Override
    public DBSchemaDTO findDBSchemaByConnectionString(String connectionstring) {
        DBSchema schema = dbSchemaRepo.findDBSchemaByConnectionString(connectionstring);

        return schema != null ? DBSchemaMapper.toDBSchemaDTO(schema) : null;
    }

    @Override
    public List<DBSchemaDTO> findDBSchemasByPlatformURL(String platformURL) {
        List<DBSchemaDTO> list = new ArrayList<>();
        List<DBSchema> schemaList = dbSchemaRepo.findDBSchemasByPlatformURL(platformURL);

        for(DBSchema schema : schemaList)
        {
            list.add(DBSchemaMapper.toDBSchemaDTO(schema));
        }

        return list;
    }

    @Override
    public List<DBSchemaDTO> findDBSchemasByUpdatePeriod(String updateperiod) {
        List<DBSchemaDTO> list = new ArrayList<>();
        List<DBSchema> schemaList = dbSchemaRepo.findDBSchemasByUpdatePeriod(updateperiod);

        for(DBSchema schema : schemaList)
        {
            list.add(DBSchemaMapper.toDBSchemaDTO(schema));
        }

        return list;
    }

    @Override
    public DBSchemaDTO findDBSchemaByNameWithCredentials(String dbSchemaName) {
        DBSchema schema = this.dbSchemaRepo.findDB_SchemaByName(dbSchemaName);

        if(schema != null)
        {
            DBSchemaDTO schemaDTO = DBSchemaMapper.toDBSchemaDTO(schema);

            encryptor = new PasswordEncryptorImplementation();
            schemaDTO.setPassword(encryptor.decryptDataBaseObjectPassword(schema.getPassword()));

            return schemaDTO;
        }
        return null;
    }

    @Override
    public List<DBSchemaDTO> findDBSchemasByUpdatePeriodWithCredentials(String updateperiod) {
        List<DBSchemaDTO> list = new ArrayList<>();
        List<DBSchema> schemaList = dbSchemaRepo.findDBSchemasByUpdatePeriod(updateperiod);
        encryptor = new PasswordEncryptorImplementation();

        for(DBSchema schema : schemaList)
        {
            DBSchemaDTO schemaDTO = DBSchemaMapper.toDBSchemaDTO(schema);
            schemaDTO.setPassword(encryptor.decryptDataBaseObjectPassword(schema.getPassword()));
            list.add(schemaDTO);
        }

        return list;
    }

    @Override
    public List<DBSchemaDTO> findDBSchemasByPlatformName(String platform_decoded) {
        List<DBSchemaDTO> list = new ArrayList<>();
        List<DBSchema> schemaList = dbSchemaRepo.findDBSchemasByPlatformName(platform_decoded);

        for(DBSchema schema : schemaList)
        {
            list.add(DBSchemaMapper.toDBSchemaDTO(schema));
        }

        return list;
    }
}
