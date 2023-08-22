package com.example.Fetcher.QueryBuilder;

public interface MetadataQuerying
{
    /**
     * Fetches all tables
     * @param schema name of the schema to use on query
     * @return query fully formed
     */
    String fetchAllTables(String schema);

    /**
     * Fetches all views
     * @param schema name of the schema to use on query
     * @return query fully formed
     */
    String fetchAllViews(String schema);

    /**
     * Fetches all procedures
     * @param schema name of the schema
     * @return query fully formed
     */
    String fetchAllProcedures(String schema);

    /**
     * Fetches all columns by table or view
     * @param schema name of the schema
     * @param table name of the table
     * @return query fully formed
     */
    String fetchColumnsByTable(String schema, String table);

    /**
     * Fetches script from a given view
     * @param schema name of the schema
     * @param viewname name of the view
     * @return query fully formed
     */
    String fetchViewScript(String schema,String viewname);

    /**
     * All methods bellow specify the name of the object (table name /column name / column type...) on each type of SQL
     * @return query fully formed
     */
    String column_tablename();

    String column_columnname();

    String column_columndatatype();

    String column_columnisnullable();

    String column_viewdefinition();

    String column_procedurename();

    String column_proceduredefinition();
}
