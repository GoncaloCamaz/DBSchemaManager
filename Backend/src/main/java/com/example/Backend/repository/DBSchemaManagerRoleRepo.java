package com.example.Backend.repository;

import com.example.Backend.model.DBSchemaManagerRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DBSchemaManagerRoleRepo extends JpaRepository<DBSchemaManagerRole, Long> {

    @Query("SELECT role FROM DBSchemaManagerRole role WHERE role.name=:name")
    DBSchemaManagerRole findRoleByName(@Param("name") String name);
}
