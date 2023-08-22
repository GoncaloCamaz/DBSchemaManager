package com.example.Backend.repository;

import com.example.Backend.model.DBScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DBScriptRepo extends JpaRepository<DBScript, Long>
{
    @Query("SELECT scr FROM DBScript scr WHERE scr.dbobject.name=:dbobject_name and scr.dbobject.dbschema.name=:dbschema_name ORDER BY scr.date desc")
    List<DBScript> findLatestScript(@Param("dbobject_name") String dbobject_name, @Param("dbschema_name") String dbschema_name);

    @Query("SELECT scr FROM DBScript scr WHERE scr.dbobject.name=:dbobject_name and scr.dbobject.dbschema.name=:dbschema_name and scr.date=:date")
    DBScript findByDBObjectByDBScriptDate(@Param("dbobject_name") String dbobject_name, @Param("dbschema_name") String dbschema_name,@Param("date") LocalDateTime date);

    @Query(value = "select * from dbscript s join (Select max(dbscript.id_dbscript) as id, dbscript.dbobject_id from dbscript group by dbscript.dbobject_id) res on s.id_dbscript = res.id;", nativeQuery = true)
    List<DBScript> findAllByMostRecentRecord();

    @Query("SELECT scr FROM DBScript scr WHERE scr.dbobject.dbschema.name=:schema and scr.dbobject.name=:dbObject order by scr.date desc")
    List<DBScript> findAllByDBObject(@Param("schema") String schema, @Param("dbObject") String dbObject);

    @Query("SELECT scr FROM DBScript scr WHERE scr.dbobject.dbschema.name=:schema and scr.dbobject.name=:dbObject and scr.dbobject.type=:type order by scr.date desc")
    List<DBScript> findAllByDBObjectByType(@Param("type") String type, @Param("schema") String schema, @Param("dbObject") String dbObject);
}
