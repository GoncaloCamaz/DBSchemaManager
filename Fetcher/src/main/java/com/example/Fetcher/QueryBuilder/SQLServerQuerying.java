package com.example.Fetcher.QueryBuilder;

public class SQLServerQuerying implements MetadataQuerying{

    @Override
    public String fetchAllTables(String schema) {
        return "Select TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = '" +schema+ "'" +" AND TABLE_TYPE = 'BASE TABLE'";
    }

    @Override
    public String fetchAllViews(String schema) {
        return "Select TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = '" +schema+ "'" +" AND TABLE_TYPE = 'View'";
    }

    @Override
    public String fetchAllProcedures(String schema) {
        return "SELECT ROUTINE_SCHEMA, ROUTINE_NAME, ROUTINE_TYPE, ROUTINE_DEFINITION, LAST_ALTERED FROM INFORMATION_SCHEMA.ROUTINES WHERE ROUTINE_TYPE = 'Procedure' AND ROUTINE_SCHEMA = '"+ schema+"'";
    }

    @Override
    public String fetchColumnsByTable(String schema, String table) {
        return "SELECT TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, IS_NULLABLE, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + schema +"' AND TABLE_NAME = '" + table + "'";
    }

    @Override
    public String fetchViewScript(String schema, String viewname) {
        return "Select TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE, VIEW_DEFINITION from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = '" +schema+ "' " +" AND TABLE_TYPE = 'View' AND TABLE_NAME='" + viewname+"'";
    }

    @Override
    public String column_tablename() {
        return "TABLE_NAME";
    }

    @Override
    public String column_columnname() {
        return "COLUMN_NAME";
    }

    @Override
    public String column_columndatatype() {
        return "DATA_TYPE";
    }

    @Override
    public String column_columnisnullable() {
        return "IS_NULLABLE";
    }

    @Override
    public String column_viewdefinition() {
        return "VIEW_DEFINITION";
    }

    @Override
    public String column_procedurename() {
        return "ROUTINE_NAME";
    }

    @Override
    public String column_proceduredefinition() {
        return "ROUTINE_DEFINITION";
    }
}
