package com.example.Fetcher.QueryBuilder;

public class OracleQuerying implements MetadataQuerying{

    @Override
    public String fetchAllTables(String schema) {
        return "SELECT TABLE_NAME FROM USER_TABLES";
    }

    @Override
    public String fetchAllViews(String schema) {
        return "SELECT VIEW_NAME FROM user_views";
    }

    @Override
    public String fetchAllProcedures(String schema) {
        return "Select owner, Name, Text, Type from all_source where type = 'PROCEDURE' and owner = '" + schema+"'";
    }

    @Override
    public String fetchColumnsByTable(String schema, String table) {
        return "Select table_name, column_name, data_type, nullable from user_tab_cols where table_name ='" + table+"'";
    }

    @Override
    public String fetchViewScript(String schema, String viewname) {
        return "select VIEW_NAME, TEXT from user_views WHERE VIEW_NAME = '"+ viewname+"'";
    }

    @Override
    public String column_tablename() {
        return "TABLE_NAME";
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
        return "nullable";
    }

    @Override
    public String column_viewdefinition() {
        return "TEXT";
    }

    @Override
    public String column_procedurename() {
        return "Name";
    }

    @Override
    public String column_proceduredefinition() {
        return "TEXT";
    }
}
