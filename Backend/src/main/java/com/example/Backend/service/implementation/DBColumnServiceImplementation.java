package com.example.Backend.service.implementation;

import com.example.Backend.dto.mapper.DBColumnMapper;
import com.example.Backend.dto.model.DBColumnDTO;
import com.example.Backend.model.DBColumn;
import com.example.Backend.model.DBObject;
import com.example.Backend.repository.DBColumnRepo;
import com.example.Backend.repository.DBObjectRepo;
import com.example.Backend.service.DBColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DBColumnServiceImplementation implements DBColumnService {

    @Autowired
    private DBColumnRepo dbColumnRepo;

    @Autowired
    private DBObjectRepo dbObjectRepo;

    /**
     * Saves a dbcolumn
     * @param dbColumn request parameters
     * @return DBColumnDTO if save is successful
     */
    @Override
    public DBColumnDTO saveDBColumn(DBColumnDTO dbColumn) {
        DBObject dbObject = dbObjectRepo.findDBObjectBySchemaNameByDBObject(dbColumn.getDbSchemaName(),dbColumn.getDbObjectName());
        if(dbObject != null)
        {
            DBColumn dbc = getDbColumn(dbColumn, dbObject);

            try{
                return DBColumnMapper.toDBColumnDTO(dbColumnRepo.save(dbc));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Updates a dbcolumn
     * @param dbColumnDTO request parameters
     *
     * Please note that, despite the same method (dbcolumnRepo.save) is used, the actions of this method are not the same
     * as the savedbcolumn method. This updates a existent record because id from dbcolumn is not changed
     */
    @Override
    public void updateDBColumn(DBColumnDTO dbColumnDTO) {
        DBObject dbObject = dbObjectRepo.findDBObjectBySchemaNameByDBObject(dbColumnDTO.getDbSchemaName(),dbColumnDTO.getDbObjectName());
        if(dbObject != null)
        {
            DBColumn column = dbColumnRepo.findByDBColumnNameByDBObjectName(dbColumnDTO.getName(),
                    dbColumnDTO.getDbObjectName(), dbColumnDTO.getDbSchemaName());
            if(column != null)
            {
                DBColumn dbc = getDbColumn(dbColumnDTO, dbObject);
                column.setDatatype(dbc.getDatatype());
                column.setNullable(dbc.getNullable());
                if(dbc.getDescription() != null)
                {
                    column.setDescription(dbc.getDescription());
                }
                try{
                    dbColumnRepo.save(column);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Transforms a request on a DBColumn object
     * @param dbColumnDTO request parameters
     * @param dbObject given to define the name of the dbcolumn parent
     * @return DBColumn object
     */
    private DBColumn getDbColumn(DBColumnDTO dbColumnDTO, DBObject dbObject) {
        DBColumn dbc = new DBColumn();
        dbc.setDBObject(dbObject);
        dbc.setName(dbColumnDTO.getName());
        dbc.setDatatype(dbColumnDTO.getDatatype());
        dbc.setDescription(dbColumnDTO.getDescription());
        dbc.setNullable(dbColumnDTO.getNullable());
        return dbc;
    }

    /**
     * Deletes a dbcolumn
     * Since no id from objects is sent to frontend, other parameters must be defined to define which dbcolumn
     * shall be deleted.
     * @param dbschema name of the schema
     * @param dbobject name of the table
     * @param dbcolumn name of the dbcolumn
     */
    @Override
    public void deleteDBColumn(String dbschema, String dbobject, String dbcolumn) {
        DBColumn dbc = dbColumnRepo.findByDBColumnNameByDBObjectName(dbcolumn, dbobject, dbschema);
        if(dbc != null)
        {
            try {
                dbColumnRepo.delete(dbc);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Finds all dbcolumns with a given name
     * @param name of the column
     * @return list of dbcolumns
     */
    @Override
    public List<DBColumnDTO> findDBColumnByName(String name) {
        List<DBColumnDTO> list = new ArrayList<>();
        List<DBColumn> dbColumnList = dbColumnRepo.findByName(name);
        for(DBColumn dbc : dbColumnList)
        {
            list.add(DBColumnMapper.toDBColumnDTO(dbc));
        }

        return list;
    }

    /**
     * Finds all dbcolumns by data type
     * @param dataType type of data (int/varchar...)
     * @return list of dbcolumns
     */
    @Override
    public List<DBColumnDTO> findDBColumnsByDataType(String dataType) {
        List<DBColumnDTO> list = new ArrayList<>();
        List<DBColumn> dbColumnList = dbColumnRepo.findAllByDataType(dataType);
        for(DBColumn dbc : dbColumnList)
        {
            list.add(DBColumnMapper.toDBColumnDTO(dbc));
        }

        return list;
    }

    /**
     * Finds all dbcolumns
     * @return list of dbcolumns
     */
    @Override
    public List<DBColumnDTO> findAll() {
        List<DBColumnDTO> list = new ArrayList<>();
        List<DBColumn> dbColumnList = dbColumnRepo.findAll();
        for(DBColumn dbc : dbColumnList)
        {
            list.add(DBColumnMapper.toDBColumnDTO(dbc));
        }

        return list;
    }

    /**
     * Finds a dbcolumn by schema name, dbobject name and dbcolumn name
     * @param dbschemaname schema name
     * @param dbobjectname dbobject name
     * @param dbcolumn name of the column
     * @return DBCOlumnDTO
     */
    @Override
    public DBColumnDTO findBySchemaObjectAndColumn(String dbschemaname, String dbobjectname, String dbcolumn) {
        DBColumn dbc = this.dbColumnRepo.findByDBColumnNameByDBObjectName(dbcolumn,dbobjectname,dbschemaname);

        return dbc != null ? DBColumnMapper.toDBColumnDTO(dbc) : null;
    }

    /**
     * Lists all dbcolumns by dbobject name
     * @param schema name of the schema
     * @param name name of the dbobject
     * @return list of dbcolumns
     */
    @Override
    public List<DBColumnDTO> findByDBObjectName(String schema, String name) {
        List<DBColumnDTO> list = new ArrayList<>();
        List<DBColumn> dbColumnList = dbColumnRepo.findBySchemaNameByDBObjectName(schema,name);
        for(DBColumn dbc : dbColumnList)
        {
            list.add(DBColumnMapper.toDBColumnDTO(dbc));
        }

        return list;
    }

    /**
     * Finds all dbcolumns by dbobject name (can be from different schemas)
     * @param name of dbobject
     * @return returns list of columns
     */
    @Override
    public List<DBColumnDTO> findByDBObjectName(String name) {
        List<DBColumnDTO> list = new ArrayList<>();
        List<DBColumn> dbColumnList = dbColumnRepo.findAllByDBObjectName(name);
        for(DBColumn dbc : dbColumnList)
        {
            list.add(DBColumnMapper.toDBColumnDTO(dbc));
        }

        return list;
    }

    /**
     * Lists all columns by schema
     * @param name name of the schema
     * @return list of columns
     */
    @Override
    public List<DBColumnDTO> findByDBSchemaName(String name) {
        List<DBColumnDTO> list = new ArrayList<>();
        List<DBColumn> dbColumnList = dbColumnRepo.findAllByDBSchemaName(name);
        for(DBColumn dbc : dbColumnList)
        {
            list.add(DBColumnMapper.toDBColumnDTO(dbc));
        }

        return list;
    }
}
