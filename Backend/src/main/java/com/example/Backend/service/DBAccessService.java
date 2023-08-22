package com.example.Backend.service;

import com.example.Backend.dto.model.DBAccessDTO;
import com.example.Backend.dto.model.ResultSetDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface DBAccessService {

    ResultSetDTO sendAccessToTarget(DBAccessDTO dto);

    void saveAccessToDBSchemaManagerDatabase(DBAccessDTO dto);

    void updateDBAccess(DBAccessDTO accessDTO);

    void deleteDBAccess(String schema, String object, String username, String access, LocalDateTime timestamp);

    List<DBAccessDTO> findAll();

    List<DBAccessDTO> findAllBySchema(String dbschema);
}
