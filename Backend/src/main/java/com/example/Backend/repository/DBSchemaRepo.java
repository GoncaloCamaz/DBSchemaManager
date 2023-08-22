package com.example.Backend.repository;

import com.example.Backend.model.DBSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DBSchemaRepo extends JpaRepository<DBSchema, Long>
{
    @Query("SELECT sch FROM DBSchema sch WHERE sch.name=:schema_name")
    DBSchema findDB_SchemaByName(@Param("schema_name") String schema_name);

    @Query("SELECT sch FROM DBSchema sch WHERE sch.connectionstring=:connectionString")
    DBSchema findDBSchemaByConnectionString(@Param("connectionString") String schema_connectionstring);

    @Query("SELECT sch FROM DBSchema sch WHERE sch.updateperiod=:updateperiod")
    List<DBSchema> findDBSchemasByUpdatePeriod(@Param("updateperiod") String updateperiod);
}
