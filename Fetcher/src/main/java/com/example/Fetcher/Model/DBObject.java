package com.example.Fetcher.Model;

import java.util.HashMap;

/**
 * DBObject class
 */
public class DBObject
{
    // Name of the dbobject
    private String name;
    // Type of the dbobject
    private DBObjectType type;
    // columns associated
    private HashMap<String,DBColumn> columnMap;
    // script (only defined on views and procedures)
    private String script;
    // name of the schema
    private String schemaName;

    public DBObject(String name, String type, String schemaName)
    {
        this.name = name;
        this.type = convertToEnum(type);
        this.columnMap = new HashMap<>();
        this.script = "";
        this.schemaName = schemaName;
    }

    /**
     * Converts a string to an enum class
     * @param type type of the object in string
     * @return DBObjectType
     */
    private DBObjectType convertToEnum(String type)
    {
        String t = type.toLowerCase();
        switch (t){
            case "view":
                return DBObjectType.View;
            case "procedure":
                return DBObjectType.Procedure;
            default :
                return DBObjectType.Table;
        }
    }

    public HashMap<String, DBColumn> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(HashMap<String, DBColumn> columnMap) {
        this.columnMap = columnMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DBObjectType getType() {
        return type;
    }

    public void setType(DBObjectType type) {
        this.type = type;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public String toString() {
        return "DBObject{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", columnMap=" + columnMap +
                ", script='" + script + '\'' +
                ", schemaName='" + schemaName + '\'' +
                '}'+'\n';
    }
}
