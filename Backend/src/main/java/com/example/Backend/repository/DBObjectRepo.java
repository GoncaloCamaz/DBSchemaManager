package com.example.Backend.repository;

import com.example.Backend.model.DBObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DBObjectRepo extends JpaRepository<DBObject,Long>
{
    @Query("SELECT dbo FROM DBObject dbo WHERE dbo.dbschema.name=:schema_name and dbo.name=:dbobject_name")
    DBObject findDBObjectBySchemaNameByDBObject(@Param("schema_name") String schema_name, @Param("dbobject_name") String dbobject_name);

    @Query("SELECT dbo FROM DBObject dbo WHERE dbo.dbschema.name=:schema_name")
    List<DBObject> findDBObjectsBySchemaName(@Param("schema_name") String schema_name);

    @Query("SELECT dbo FROM DBObject dbo WHERE dbo.dbschema.connectionstring=:connectionString")
    List<DBObject> findDBObjectsBySchemaConnectionString(@Param("connectionString") String connectionString);

    @Query("SELECT dbo FROM DBObject dbo WHERE dbo.dbschema.name=:schema_name AND dbo.type=:dbo_type")
    List<DBObject> findDBObjectsByTypeBySchemaName(@Param("schema_name") String schemaname, @Param("dbo_type") String dbo_type);

    @Query("SELECT dbo FROM DBObject dbo WHERE dbo.type=:dbotype")
    List<DBObject> findDBObjectsByType(@Param("dbotype")String type);
}
