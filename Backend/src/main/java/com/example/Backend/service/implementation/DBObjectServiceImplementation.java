package com.example.Backend.service.implementation;

import com.example.Backend.dto.mapper.DBObjectMapper;
import com.example.Backend.dto.model.DBObjectDTO;
import com.example.Backend.model.DBObject;
import com.example.Backend.model.DBSchema;
import com.example.Backend.repository.DBObjectRepo;
import com.example.Backend.repository.DBSchemaRepo;
import com.example.Backend.service.DBObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class DBObjectServiceImplementation implements DBObjectService
{
    @Autowired
    private DBObjectRepo dbObjectRepo;

    @Autowired
    private DBSchemaRepo dbSchemaRepo;

    @Override
    public DBObjectDTO saveDBObject(DBObjectDTO dbObjectDTO) {
        DBSchema schema = dbSchemaRepo.findDB_SchemaByName(dbObjectDTO.getDbSchemaName());
        if(schema != null)
        {
            DBObject dbo = new DBObject();
            dbo.setName(dbObjectDTO.getName());
            dbo.setDbschema(schema);
            dbo.setType(dbObjectDTO.getType());
            dbo.setDescription(dbObjectDTO.getDescription());
            dbo.setDbColumnSet(new HashSet<>());
            dbo.setDbScriptSet(new HashSet<>());

            try
            {
                return DBObjectMapper.toDBObjectDTO(dbObjectRepo.save(dbo));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void deleteDBObject(DBObjectDTO dbObjectDTO) {
        DBObject dbObject = dbObjectRepo.findDBObjectBySchemaNameByDBObject(dbObjectDTO.getDbSchemaName(), dbObjectDTO.getName());
        if(dbObject != null)
        {
            try {
                dbObjectRepo.delete(dbObject);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateDBObject(DBObjectDTO dbObjectDTO) {
        try
        {
            DBObject dbo = dbObjectRepo.findDBObjectBySchemaNameByDBObject(dbObjectDTO.getDbSchemaName(),dbObjectDTO.getName());
            if(dbo != null)
            {
                dbo.setDescription(dbObjectDTO.getDescription());
                dbo.setStatus(dbObjectDTO.getStatus());
                dbObjectRepo.save(dbo);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<DBObjectDTO> findAll() {
        List<DBObjectDTO> list = new ArrayList<>();
        List<DBObject> dbObjectList = dbObjectRepo.findAll();
        for(DBObject dbo : dbObjectList)
        {
            list.add(DBObjectMapper.toDBObjectDTO(dbo));
        }

        return list;
    }

    @Override
    public List<DBObjectDTO> findDBObjectsBySchemaName(String schemaname) {
        List<DBObjectDTO> list = new ArrayList<>();
        List<DBObject> dbObjectList = dbObjectRepo.findDBObjectsBySchemaName(schemaname);
        for(DBObject dbo : dbObjectList)
        {
            list.add(DBObjectMapper.toDBObjectDTO(dbo));
        }

        return list;
    }

    @Override
    public List<DBObjectDTO> findDBObjectsBySchemaConnectionString(String connectionString) {
        List<DBObjectDTO> list = new ArrayList<>();
        List<DBObject> dbObjectList = dbObjectRepo.findDBObjectsBySchemaConnectionString(connectionString);
        for(DBObject dbo : dbObjectList)
        {
            list.add(DBObjectMapper.toDBObjectDTO(dbo));
        }

        return list;
    }

    @Override
    public List<DBObjectDTO> findDBObjectsByTypeBySchemaName(String schemaname, String dbo_type) {
        List<DBObjectDTO> list = new ArrayList<>();
        List<DBObject> dbObjectList = dbObjectRepo.findDBObjectsByTypeBySchemaName(schemaname,dbo_type);
        for(DBObject dbo : dbObjectList)
        {
            list.add(DBObjectMapper.toDBObjectDTO(dbo));
        }

        return list;
    }

    @Override
    public List<DBObjectDTO> findDBObjectsByType(String type) {
        List<DBObjectDTO> list = new ArrayList<>();
        List<DBObject> dbObjectList = dbObjectRepo.findDBObjectsByType(type);
        for(DBObject dbo : dbObjectList)
        {
            list.add(DBObjectMapper.toDBObjectDTO(dbo));
        }

        return list;
    }

    @Override
    public DBObjectDTO findDBObjectByNameBySchemaName(String dbSchemaName, String name) {

        DBObject dbo = this.dbObjectRepo.findDBObjectBySchemaNameByDBObject(dbSchemaName, name);
        return dbo != null ? DBObjectMapper.toDBObjectDTO(dbo) : null;
    }
}
