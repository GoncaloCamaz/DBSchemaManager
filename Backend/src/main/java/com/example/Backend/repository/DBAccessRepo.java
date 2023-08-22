package com.example.Backend.repository;

import com.example.Backend.model.DBAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DBAccessRepo extends JpaRepository<DBAccess, Long> {

    @Query("SELECT dba FROM DBAccess dba WHERE dba.dbSchemaName=:schema and dba.dbObjectName=:object and dba.dbUsername=:username and dba.privilege=:accesstype and dba.timestamp=:timestamp")
    DBAccess findBySchemaObjectUsernameAccessTypeAndTimeStamp(@Param("schema") String schema, @Param("object") String object,
                                                              @Param("username") String username, @Param("accesstype") String accesstype,
                                                              @Param("timestamp")LocalDateTime timestamp);

    @Query("SELECT dba FROM DBAccess dba WHERE dba.dbSchemaName=:schema")
    List<DBAccess> findAllByDbSchemaName(@Param("schema") String schema);
}
