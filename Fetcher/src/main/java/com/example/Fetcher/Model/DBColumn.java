package com.example.Fetcher.Model;

/**
 * DBColumn object
 *
 * This object is different from the object defined on backend DTO model because there is information that is not
 * necessary.
 */
public class DBColumn
{
    private String name;
    private String datatype;
    private String parentTable;
    private String nullable;

    /**
     * DBSchemaName is needed to store new columns to the backend due to the lack of id's on the data transfer object
     */
    private String dbSchemaName;

    public DBColumn(String name, String datatype, String nullable, String parentTable) {
        this.name = name;
        this.datatype = datatype;
        this.nullable = nullable;
        this.parentTable = parentTable;
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

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public String getParentTable() {
        return parentTable;
    }

    public void setParentTable(String parentTable) {
        this.parentTable = parentTable;
    }

    public String getDbSchemaName() {
        return dbSchemaName;
    }

    public void setDbSchemaName(String dbSchemaName) {
        this.dbSchemaName = dbSchemaName;
    }

    @Override
    public String toString() {
        return "DBColumn{" +
                "name='" + name + '\'' +
                ", datatype='" + datatype + '\'' +
                ", parentTable='" + parentTable + '\'' +
                ", nullable='" + nullable + '\'' +
                ", dbSchemaName='" + dbSchemaName + '\'' +
                '}'+'\n';
    }
}
