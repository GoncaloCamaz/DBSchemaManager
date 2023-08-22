package com.example.Backend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DBColumnDTO {
    private String name;
    private String datatype;
    private String dbObjectName;
    private String dbSchemaName;
    private String description;
    private String nullable;

    public DBColumnDTO() {
    }

    public DBColumnDTO(String name, String datatype, String dbObjectName, String dbSchemaName, String description, String nullable) {
        this.name = name;
        this.datatype = datatype;
        this.dbObjectName = dbObjectName;
        this.dbSchemaName = dbSchemaName;
        this.description = description;
        this.nullable = nullable;
    }

    public DBColumnDTO(DBColumnDTO dbColumnDTO)
    {
        this.name = dbColumnDTO.getName();
        this.datatype = dbColumnDTO.getDatatype();
        this.dbObjectName = dbColumnDTO.getDbObjectName();
        this.dbSchemaName = dbColumnDTO.getDbSchemaName();
        this.description = dbColumnDTO.getDescription();
        this.nullable = dbColumnDTO.getNullable();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
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

    @Override
    public DBColumnDTO clone()
    {
        return new DBColumnDTO(this);
    }
}
