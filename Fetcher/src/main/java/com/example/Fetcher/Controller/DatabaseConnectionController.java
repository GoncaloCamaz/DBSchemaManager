package com.example.Fetcher.Controller;

import com.example.Fetcher.QueryBuilder.*;
import com.example.Fetcher.Model.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Locale;

public class DatabaseConnectionController
{
    private Schema currentSchema;
    private Connection connection;
    private MetadataQuerying querying;

    public DatabaseConnectionController() {
        this.currentSchema = new Schema();
    }

    /**
     * Establishes connection with the target database
     * This method must be called before updates are performed
     * @return 1 if all good; 0 otherwise
     */
    public int establishConnection()
    {
        try {
            this.connection = DriverManager.getConnection(this.currentSchema.getConnectionstring(),
                    this.currentSchema.getUsername(),
                    this.currentSchema.getPassword());
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Fetch all tables and its columns
     * @return 1 if operation successfull; -1 otherwise
     */
    public int runTargetDatabaseTablesUpdate()
    {
        try {
            fetchSchemaTables();
            fetchColumns();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    /**
     * Fetch all views and columns and scripts
     * @return 1 if operation successfull; -1 otherwise
     */
    public int runTargetDatabaseViewsUpdate()
    {
        try {
            fetchSchemaViews();
            fetchColumns();
            fetchViewScripts();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return -1;
    }

    /**
     * Since the only object that will be found is the one with the name viewname, there is no need to specify
     * the name of the view on columns
     * @param viewname name of the view
     * @return 1 if operation successfull; -1 otherwise
     */
    public int runTargetDatabaseViewsUpdate(String viewname)
    {
        try {
            establishConnection();
            fetchSchemaViews(viewname);
            fetchColumns();
            fetchViewScripts();
            closeConnection();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return -1;
    }

    /**
     * Fetches procedures
     * @return 1 if operation successfull; -1 otherwise
     */
    public int runTargetDatabaseProceduresUpdate() {
        try
        {
            fetchProcedures();
            return 1;
        } catch (SQLException e )
        {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Method that fetches all schema tables from target database (called by the public method on this class)
     * @throws SQLException if no connection exists
     */
    public void fetchSchemaTables() throws SQLException {
        Statement stmt = this.connection.createStatement();
        HashMap<String, DBObject> objectHashMap = new HashMap<>();
        ResultSet rs =stmt.executeQuery(this.querying.fetchAllTables(this.currentSchema.getName()));
        while(rs.next())
        {
            String dboName = rs.getString(this.querying.column_tablename());
            String dboType = DBObjectType.Table.name();
            DBObject dbo = new DBObject(dboName, dboType, currentSchema.getName());
            objectHashMap.put(dboName, dbo);
        }
        this.currentSchema.setObjects(objectHashMap);
    }

    /**
     * Fetches al columns by dbobject that should already exist on this object
     * @throws SQLException if no connection is established
     */
    public void fetchColumns() throws SQLException {
        Statement stmt = this.connection.createStatement();

        for(DBObject dbo : this.currentSchema.getObjects().values())
        {
            HashMap<String, DBObject> objectHashMap = this.currentSchema.getObjects();
            ResultSet rs =stmt.executeQuery(this.querying.fetchColumnsByTable(this.currentSchema.getName(),dbo.getName()));

            while(rs.next())
            {
                String name = rs.getString(this.querying.column_columnname());
                String datatype = rs.getString(this.querying.column_columndatatype());
                String nullable = rs.getString(this.querying.column_columnisnullable());
                DBColumn column = new DBColumn(name,datatype,nullable,dbo.getName());
                column.setDbSchemaName(this.currentSchema.getName());
                objectHashMap.get(dbo.getName()).getColumnMap().put(name, column);
            }
            this.currentSchema.setObjects(objectHashMap);
        }
    }

    /**
     * This method gets all views
     * @throws SQLException if no connection established
     */
    public void fetchSchemaViews() throws SQLException {
        Statement stmt = this.connection.createStatement();
        HashMap<String, DBObject> objectHashMap = new HashMap<>();
        ResultSet rs =stmt.executeQuery(this.querying.fetchAllViews(this.currentSchema.getName()));

        while(rs.next())
        {
            String dboName = rs.getString(this.querying.column_tablename());
            String dboType = DBObjectType.View.name();
            DBObject dbo = new DBObject(dboName, dboType, currentSchema.getName());
            dbo.setSchemaName(this.currentSchema.getName());
            objectHashMap.put(dboName, dbo);
        }
        this.currentSchema.setObjects(objectHashMap);
    }

    /**
     * This method gets view with the provided viewname
     * @param viewname this parameter is the name of the view
     * @throws SQLException if no connection established
     */
    public void fetchSchemaViews(String viewname) throws SQLException {
        Statement stmt = this.connection.createStatement();
        HashMap<String, DBObject> objectHashMap = new HashMap<>();
        ResultSet rs =stmt.executeQuery(this.querying.fetchAllViews(this.currentSchema.getName()));

        while(rs.next())
        {
            String dboName = rs.getString(this.querying.column_tablename());
            if(dboName.equals(viewname))
            {
                String dboType = DBObjectType.View.name();
                DBObject dbo = new DBObject(dboName, dboType, currentSchema.getName());
                dbo.setSchemaName(this.currentSchema.getName());
                objectHashMap.put(dboName, dbo);
            }
        }
        this.currentSchema.setObjects(objectHashMap);
    }

    /**
     * Fetches view definition
     * @throws SQLException if no connection established
     */
    public void fetchViewScripts() throws SQLException {
        Statement stmt=this.connection.createStatement();

        for(DBObject dbo : this.currentSchema.getObjects().values())
        {
            ResultSet rs = stmt.executeQuery(this.querying.fetchViewScript(this.currentSchema.getName(),dbo.getName()));
            while(rs.next())
            {
                String scriptCode = rs.getString(this.querying.column_viewdefinition());
                dbo.setScript(scriptCode);
            }
        }
    }

    /**
     * Fetches procedures and creates the object with the procedure definition
     * @throws SQLException if data not found
     */
    public void fetchProcedures() throws SQLException {
        Statement statement = this.connection.createStatement();
        HashMap<String,DBObject> objectHashMap = new HashMap<>();
        ResultSet rs = statement.executeQuery(this.querying.fetchAllProcedures(this.currentSchema.getName()));

        while(rs.next())
        {
            String dboName = rs.getString(this.querying.column_procedurename());
            String definition = rs.getString(this.querying.column_proceduredefinition());
            DBObject dbo = new DBObject(dboName, DBObjectType.Procedure.name(), this.currentSchema.getName());
            dbo.setScript(definition);
            objectHashMap.put(dboName, dbo);
        }
        this.currentSchema.setObjects(objectHashMap);
    }

    /**
     * closes connection with target database
     */
    public void closeConnection()
    {
        try {
            this.connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Resets the hashmap from this object
     * Its used to keep memory usage low
     */
    public void reset()
    {
        this.currentSchema.setObjects(new HashMap<>());
    }

    public Schema getCurrentSchema() {
        return currentSchema;
    }

    /**
     * Method that defines the current schema to fetch information from
     * It must define the type of sql server in order to get the correct names from MetaDataQuerying
     * @param currentSchema information about the current schema
     */
    public void setCurrentSchema(Schema currentSchema) {
        this.currentSchema = currentSchema;
        String sqlServerType = this.currentSchema.getServer().name().toLowerCase(Locale.ROOT);
        switch (sqlServerType) {
            case "mysql" -> this.querying = new MySQLQuerying();
            case "oracle" -> this.querying = new OracleQuerying();
            case "sqlserver" -> this.querying = new SQLServerQuerying();
            default -> this.querying = new PostgreSQLQuerying();
        }
    }
}
