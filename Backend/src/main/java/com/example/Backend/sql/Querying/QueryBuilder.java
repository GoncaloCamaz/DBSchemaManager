package com.example.Backend.sql.Querying;


import com.example.Backend.dto.model.DBScriptDTO;
import com.example.Backend.dto.model.ResultSetDTO;

public interface QueryBuilder {

    String getViewNameFromCreateViewStatement(String query);

    String select100RowsFromViewQuery(String viewname);

    String fetchViewInformationsQuery(String viewname);

    String fetchViewScriptQuery(String schemaname, String viewname);

    ResultSetDTO checkViewResult(String viewname, DBScriptDTO latest, String query, String definer, String date);

    String grant(String grant, String dbobject, String objectType, String user, String privilege);

    String fetchAllDBUsers();

    String definitionName();

    String updateTimeName();

    String definerName();
}
