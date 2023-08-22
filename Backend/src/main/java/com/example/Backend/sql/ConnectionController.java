package com.example.Backend.sql;

import com.example.Backend.dto.model.DBScriptDTO;
import com.example.Backend.dto.model.ResultSetDTO;
import com.example.Backend.dto.model.ResultSetElementDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionController
{
    private Connection connection;
    private String connectionstring;
    private String sqlservertype;
    private String username;
    private String password;

    public ConnectionController(String sqlservertype, String connectionstring, String username, String password) {
        this.connectionstring = connectionstring;
        this.username = username;
        this.password = password;
        this.sqlservertype = sqlservertype;
    }

    /**
     * Opens jdbc connection with target database
     * @return 1 if connection established; -1 otherwise
     */
    public int establishConnection(){
        try {
            Connection con = DriverManager.getConnection(this.connectionstring, this.username, this.password);
            this.setConnection(con);
            return 1;
        } catch (SQLException throwables) {
            return -1;
        }
    }

    /**
     * Closes jdbc connection to target database
     */
    public void closeConnection(){
        try {
            this.connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Sends a grant privilege to target database
     * @param grant (GRANT/REVOKE) specifies if it's a grant or a revoke access
     * @param dbobject name of the object
     * @param objectType type of the object
     * @param user name of the user
     * @param privilege grant type (SELECT/EXECUTE...)
     * @return result of the operation to respond to frontend request
     */
    public ResultSetDTO grantPrivilege(String grant, String dbobject, String objectType, String user,String privilege)
    {
        ResultSetDTO resultSetDTO = new ResultSetDTO();
        resultSetDTO.setValidQuery(false);
        resultSetDTO.setRows(new ArrayList<>());
        resultSetDTO.setColumns(new ArrayList<>());

        try {
            SQLUtils sqlUtils = new SQLUtils(this.sqlservertype);
            String grantQuery = sqlUtils.getGrantString(grant,dbobject,objectType,user,privilege);
            Statement statement = this.connection.createStatement();
            statement.execute(grantQuery);
            resultSetDTO.setValidQuery(true);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            resultSetDTO.setErrormessage(throwable.getMessage());
        }

        return resultSetDTO;
    }

    /**
     * Method to create or replace a view on target database
     * @param viewname name of the view (is extracted with regex's from query
     * @param newquery query to be sent
     * @param script record of latest script existing on DBSchemaManager database
     * @return ResultSetDTO to respond to frontend request
     */
    public ResultSetDTO createOrReplaceNewView(String viewname, String newquery, DBScriptDTO script)
    {
        ResultSetDTO resultSetDTO = new ResultSetDTO();
        try{
            SQLUtils sqlUtils = new SQLUtils(this.sqlservertype);
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlUtils.fetchViewInformationsQuery(viewname));

            String viewcode = "";
            String date = "";
            String definer = "";
            while (resultSet.next()) {
                viewcode = resultSet.getString(sqlUtils.getViewDefinitionName());
                date = resultSet.getString(sqlUtils.getUpdateTimeName());
                definer = resultSet.getString(sqlUtils.getDefinerName());
            }
            return getResultSetDTO(viewname, newquery, script, sqlUtils, viewcode, date, definer);
        } catch (SQLException e)
        {
            e.printStackTrace();
            resultSetDTO.setErrormessage(e.getMessage());
        }
        return null;
    }

    /**
     * Checks if view already exists on target database (viewcode, date and definer != "") and if it exists, compares
     * the most recent record stored on dbschema manager database with the actual query script defined on the target database
     * If there is no records of the script on the database and the view already exists on target database,
     * the result of this method shall advise frontend user that there might be versions of the view not fetched yet
     * @param viewname name of the view
     * @param newquery query inserted by the user
     * @param script latest record stored on the database
     * @param sqlUtils utils
     * @param viewcode view code fetched from target database
     * @param date date of the view creation
     * @param definer definer of the view fetched from target database
     * @return result with successful or not information
     */
    private ResultSetDTO getResultSetDTO(String viewname, String newquery, DBScriptDTO script, SQLUtils sqlUtils, String viewcode, String date, String definer) {
        if(viewcode.isEmpty() && date.isEmpty() && definer.isEmpty())
        {
            return sendViewQueryToTargetDB(viewname, newquery);
        }
        else {
            ResultSetDTO result = sqlUtils.checkViewResults(viewname, script, viewcode, definer, date);
            if(result.getErrormessage().isEmpty() && result.getWarningmessage().isEmpty())
            {
                return sendViewQueryToTargetDB(viewname, newquery);
            }
            else
            {
                return result;
            }
        }
    }

    /**
     * Sends the view to target database and fetches firsts 100 rows of the view result
     * @param viewname name of the view
     * @param query viewquery
     * @return result set dto with columns and rows fetched from created view to show on frontend the result
     */
    public ResultSetDTO sendViewQueryToTargetDB(String viewname, String query)
    {
        ResultSetDTO resultSetDTO = new ResultSetDTO();
        resultSetDTO.setValidQuery(false);
        resultSetDTO.setRows(new ArrayList<>());
        resultSetDTO.setColumns(new ArrayList<>());
        SQLUtils sqlUtils = new SQLUtils(this.sqlservertype);

        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
            String sqlSelectQuery = sqlUtils.select100RowsFromViewQuery(viewname);
            ResultSet resultSet = statement.executeQuery(sqlSelectQuery);
            List<String> columns = getColumnsFromResultSet(resultSet);
            List<ResultSetElementDTO> rows = getRowsFromResultSet(resultSet, columns);
            resultSetDTO.setColumns(columns);
            resultSetDTO.setRows(rows);
            if(!resultSetDTO.getColumns().isEmpty())
            {
                resultSetDTO.setValidQuery(true);
                resultSetDTO.setQueryName(viewname);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            resultSetDTO.setErrormessage(throwable.getMessage());
        }
        return resultSetDTO;
    }

    /**
     * Sends an SQL query to a given target database
     * @param query query inserted by the user
     * @return result set dto object
     */
    public ResultSetDTO sendQueryToTargetDB(String query)
    {
        ResultSetDTO resultSetDTO = new ResultSetDTO();
        resultSetDTO.setValidQuery(false);
        resultSetDTO.setRows(new ArrayList<>());
        resultSetDTO.setColumns(new ArrayList<>());

        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<String> columns = getColumnsFromResultSet(resultSet);
            List<ResultSetElementDTO> rows = getRowsFromResultSet(resultSet, columns);
            resultSetDTO.setColumns(columns);
            resultSetDTO.setRows(rows);
            if(!resultSetDTO.getColumns().isEmpty())
            {
                resultSetDTO.setValidQuery(true);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            resultSetDTO.setErrormessage(throwables.getMessage());
        }
        return resultSetDTO;
    }

    /**
     * Transforms content from target database on rows to send to frontend
     * @param resultSet content from target database
     * @param columns list of columns that were previously retrieved from target database
     * @return List of ResultSetElementDTO that represents the rows retrieved
     */
    private List<ResultSetElementDTO> getRowsFromResultSet(ResultSet resultSet, List<String> columns)
    {
        List<ResultSetElementDTO> result = new ArrayList<>();

        try {
            while(resultSet.next())
            {
                ResultSetElementDTO row = new ResultSetElementDTO();
                for(String c : columns)
                {
                    row.addRow(c,resultSet.getObject(c));
                }
                result.add(row);
            }

            return result;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Retrieves columns names from content
     * @param resultSet content fetched from target database
     * @return list with the name of the columns
     */
    private List<String> getColumnsFromResultSet(ResultSet resultSet) {
        try {
            int numberOfColumns = resultSet.getMetaData().getColumnCount();
            List<String> result = new ArrayList<>();
            for(int i = 1; i <= numberOfColumns; i++)
            {
                result.add(resultSet.getMetaData().getColumnLabel(i));
            }

            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Fetches the view definition from target database
     * @param schema name of the schema
     * @param dbObjectName name of the view
     * @return view definition
     */
    public String fetchViewScript(String schema, String dbObjectName) {
        try {
            Statement statement = this.connection.createStatement();
            SQLUtils sqlUtils = new SQLUtils(this.sqlservertype);
            String createViewQuery = sqlUtils.fetchViewScriptQuery(schema, dbObjectName);
            ResultSet rs = statement.executeQuery(createViewQuery);
            String result = "";
            while(rs.next())
            {
                result = rs.getString(sqlUtils.getViewDefinitionName());
            }
            return result;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public void setConnection(Connection connetion) {
        this.connection = connetion;
    }

    public String getConnectionstring() {
        return connectionstring;
    }

    public void setConnectionstring(String connectionstring) {
        this.connectionstring = connectionstring;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}