package com.example.Backend.sql;

import com.example.Backend.dto.model.DBScriptDTO;
import com.example.Backend.dto.model.ResultSetDTO;
import com.example.Backend.sql.Querying.*;
import com.example.Backend.utils.EnumConverter;
import com.example.Backend.utils.SQLServerType;

/**
 * Every instance of SQLUtils created must define the type of sql server.
 * This is done in order to define which QueryBuilder should be used
 */
public class SQLUtils {

    private QueryBuilder queryBuilder;

    public SQLUtils(String servertype)
    {
        this.queryBuilder = getServerType(servertype);
    }

    private QueryBuilder getServerType(String servertype) {
        SQLServerType enumSQLServer = EnumConverter.getEnumMySQLServerType(servertype);

        switch (enumSQLServer){
            case MySQL:
                return new MySQLQuerying();
            case SQLServer:
                return new SQLServerQuerying();
            case Oracle:
                return new OracleQuerying();
            default:
                return new PostgreSQLQuerying();
        }
    }

    public String getGrantString(String grant, String dbobject, String objectType, String user, String privilege)
    {
        return this.queryBuilder.grant(grant,dbobject,objectType,user,privilege);
    }

    public String getViewNameFromCreateViewStatement(String createViewQuery) {
        return this.queryBuilder.getViewNameFromCreateViewStatement(createViewQuery);
    }

    public String fetchViewInformationsQuery(String viewname) {
        String query = this.queryBuilder.fetchViewInformationsQuery(viewname);
        return query;
    }

    public String select100RowsFromViewQuery(String viewname) {
        return this.queryBuilder.select100RowsFromViewQuery(viewname);
    }

    public String fetchViewScriptQuery(String schema, String viewname)
    {
        return this.queryBuilder.fetchViewScriptQuery(schema,viewname);
    }

    public ResultSetDTO checkViewResults(String viewname, DBScriptDTO latest, String query, String definer, String date)
    {
        return this.queryBuilder.checkViewResult(viewname,latest,query,definer,date);
    }

    public String fetchAllUsersQuery()
    {
        return this.queryBuilder.fetchAllDBUsers();
    }

    public String getViewDefinitionName(){
        return this.queryBuilder.definitionName();
    }

    public String getUpdateTimeName() {
        return this.queryBuilder.updateTimeName();
    }

    public String getDefinerName(){
        return this.queryBuilder.definerName();
    }
}
