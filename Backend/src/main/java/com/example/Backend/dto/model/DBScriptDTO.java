package com.example.Backend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DBScriptDTO {
    private String code;
    private LocalDateTime date;
    private String description;
    private String definer;
    private String dbObjectName;
    private String dbSchemaName;

    public DBScriptDTO() {
    }

    public DBScriptDTO(String code, LocalDateTime date, String description, String definer, String dbObjectName, String dbSchemaname) {
        this.code = code;
        this.date = date;
        this.description = description;
        this.definer = definer;
        this.dbObjectName = dbObjectName;
        this.dbSchemaName = dbSchemaname;
    }

    public DBScriptDTO(DBScriptDTO dto)
    {
        this.code = dto.getCode();
        this.date = dto.getDate();
        this.description = dto.getDescription();
        this.definer = dto.getDefiner();
        this.dbObjectName = dto.getDbObjectName();
        this.dbSchemaName = dto.getDbSchemaName();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDbObjectName() {
        return dbObjectName;
    }

    public void setDbObjectName(String dbObjectName) {
        this.dbObjectName = dbObjectName;
    }

    public String getDbSchemaName() {
        return dbSchemaName;
    }

    public void setDbSchemaName(String dbSchemaName) {
        this.dbSchemaName = dbSchemaName;
    }

    public String getDefiner() {
        return definer;
    }

    public void setDefiner(String definer) {
        this.definer = definer;
    }

    @Override
    public DBScriptDTO clone()
    {
        return new DBScriptDTO(this);
    }
}
