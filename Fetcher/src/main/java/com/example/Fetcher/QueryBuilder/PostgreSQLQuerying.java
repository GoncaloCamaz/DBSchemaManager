package com.example.Fetcher.QueryBuilder;

public class PostgreSQLQuerying implements MetadataQuerying{

    @Override
    public String fetchAllTables(String schema) {
        return "SELECT TABLE_NAME, TABLE_TYPE, TABLE_SCHEMA FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA = 'public'";
    }

    @Override
    public String fetchAllViews(String schema) {
        return "SELECT TABLE_NAME, TABLE_TYPE, TABLE_SCHEMA FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'VIEW' AND TABLE_SCHEMA = 'public'";
    }

    @Override
    public String fetchAllProcedures(String schema) {
        return "SELECT ROUTINE_NAME, ROUTINE_DEFINITION, ROUTINE_TYPE, ROUTINE_SCHEMA FROM INFORMATION_SCHEMA.ROUTINES WHERE ROUTINE_TYPE = 'PROCEDURE' AND ROUTINE_SCHEMA = " + "'" + schema + "'" + ";";
    }

    @Override
    public String fetchColumnsByTable(String schema, String table) {
        return "SELECT TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, DATA_TYPE, IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'public'"+ " AND TABLE_NAME = " + "'" + table + "'" + ";";
    }

    @Override
    public String fetchViewScript(String schema, String viewname) {
        return "SELECT TABLE_SCHEMA, TABLE_NAME, VIEW_DEFINITION FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = 'public'"+ " AND TABLE_NAME = " + "'" + viewname + "'" + ";";
    }

    @Override
    public String column_tablename() {
        return "table_name";
    }

    @Override
    public String column_columnname() {
        return "column_name";
    }

    @Override
    public String column_columndatatype() {
        return "data_type";
    }

    @Override
    public String column_columnisnullable() {
        return "is_nullable";
    }

    @Override
    public String column_viewdefinition() {
        return "view_definition";
    }

    @Override
    public String column_procedurename() {
        return "routine_name";
    }

    @Override
    public String column_proceduredefinition() {
        return "routine_definition";
    }
}
