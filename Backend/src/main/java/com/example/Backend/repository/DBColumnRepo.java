package com.example.Backend.repository;

import com.example.Backend.model.DBColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * This interface is where Spring Boot forms a query to send to DBSChema Database.
 * The Query Tag specifies the query to the method. The language is SQL based, but it cannot be interpreted by sql.
 * <p>
 * Parameters on the query are specified on the methods with Param tag. The name inside param must match the one specified
 * inside query tag after =:
 */
public interface DBColumnRepo extends JpaRepository<DBColumn, Long>
{
    @Query("SELECT dbc FROM DBColumn dbc WHERE dbc.name=:column_name and dbc.dbobject.name=:dbobject_name and dbc.dbobject.dbschema.name=:schema_name")
    DBColumn findByDBColumnNameByDBObjectName(@Param("column_name") String column_name, @Param("dbobject_name") String dbobject_name,@Param("schema_name") String schema_name);

    @Query("SELECT dbc FROM DBColumn dbc WHERE dbc.name=:column_name")
    List<DBColumn> findByName(@Param("column_name") String column_name);

    @Query("SELECT dbc FROM DBColumn dbc WHERE dbc.datatype=:data_type")
    List<DBColumn> findAllByDataType(@Param("data_type") String data_type);

    @Query("SELECT dbc FROM DBColumn dbc WHERE dbc.dbobject.name=:dbo_name")
    List<DBColumn> findAllByDBObjectName(@Param("dbo_name")String name);

    @Query("SELECT dbc FROM DBColumn dbc WHERE dbc.dbobject.name=:dbobject_name and dbc.dbobject.dbschema.name=:schema_name")
    List<DBColumn> findBySchemaNameByDBObjectName(@Param("schema_name") String schema_name,@Param("dbobject_name") String dbobject_name);

    @Query("SELECT dbc FROM DBColumn dbc WHERE dbc.dbobject.dbschema.name=:schema_name")
    List<DBColumn> findAllByDBSchemaName(@Param("schema_name")String name);
}
