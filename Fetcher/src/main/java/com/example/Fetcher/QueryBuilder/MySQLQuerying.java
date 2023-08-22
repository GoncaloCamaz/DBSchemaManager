package com.example.Fetcher.QueryBuilder;

public class MySQLQuerying implements MetadataQuerying{

    @Override
    public String fetchAllTables(String schema) {
        return "SELECT TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA = " + "'" + schema + "'";
    }

    @Override
    public String fetchColumnsByTable(String schema, String table) {
        return "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = " + "'" + schema + "'" + " AND TABLE_NAME = " + "'" + table + "'";
    }

    @Override
    public String fetchAllViews(String schema) {
        return "SELECT TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'VIEW' AND TABLE_SCHEMA = " + "'" + schema + "'";
    }

    @Override
    public String fetchAllProcedures(String schema) {
        return "SELECT ROUTINE_SCHEMA, ROUTINE_NAME, ROUTINE_DEFINITION, ROUTINE_TYPE FROM INFORMATION_SCHEMA.ROUTINES WHERE ROUTINE_TYPE = 'PROCEDURE' AND ROUTINE_SCHEMA = " + "'" + schema + "'";
    }

    @Override
    public String fetchViewScript(String schema,String viewname) {
        return "SELECT TABLE_SCHEMA, TABLE_NAME, VIEW_DEFINITION FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = " + "'" + schema + "'" + " AND TABLE_NAME = " + "'" + viewname + "'";
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
