package com.example.Backend.sql.Querying;

import com.example.Backend.dto.model.DBScriptDTO;
import com.example.Backend.dto.model.ResultSetDTO;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostgreSQLQuerying implements QueryBuilder{

    @Override
    public String getViewNameFromCreateViewStatement(String query) {
        String firstQuery = query.replaceAll("[\\t\\n\\r]+"," ");
        Pattern patternText = Pattern.compile("(?i)(CREATE\\s([a-zA-Z\\s]+)?)VIEW\\s([a-zA-Z0-9_]+)\\sas\\s");
        Matcher matcherText = patternText.matcher(firstQuery);
        String viewname = "";

        if(matcherText.find())
        {
            viewname = matcherText.group();
            viewname = viewname.replaceFirst("(?i)(CREATE)", "");
            viewname = viewname.replaceFirst("(?i)(\\s([a-zA-Z\\s]+)?\\sview\\s)", " ");
            viewname = viewname.replaceFirst("(?i)(\\sas\\s?)", "");
            viewname = viewname.replaceAll("\\s", "");
        }

        return viewname;
    }

    @Override
    public String select100RowsFromViewQuery(String viewname) {
        return "SELECT * FROM " + viewname + " LIMIT 100;";
    }

    @Override
    public String fetchViewInformationsQuery(String viewname) {
        return "SELECT view_definition, definer, create_time FROM INFORMATION_SCHEMA.VIEWS as infV " +
                "inner join Information_schema.tables as invT where infV.table_name = '" +viewname +"' and invT.table_name = '" +viewname +"';";
    }

    @Override
    public String fetchViewScriptQuery(String schemaname, String viewname) {
        return "SELECT * FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = " + "'" + schemaname + "'" + " AND TABLE_NAME = " + "'" + viewname + "'" + ";";
    }

    @Override
    public ResultSetDTO checkViewResult(String viewname, DBScriptDTO latest, String query, String definer, String date) {
        ResultSetDTO resultSetDTO = new ResultSetDTO();
        resultSetDTO.setWarningmessage("");
        resultSetDTO.setErrormessage("");

        if(latest != null)
        {
            if(compareViews(query, latest.getCode()) != 1)
            {
                resultSetDTO.setErrormessage("There might be updates not catched on " + viewname + ".");
            }
        }
        else
        {
            resultSetDTO.setWarningmessage("No records of " + viewname + " found!");
            resultSetDTO.setErrormessage("There might be updates not catched on " + viewname + ".");
        }

        return resultSetDTO;
    }

    @Override
    public String grant(String grant, String dbobject, String objectType, String user,String privilege) {
        objectType = objectType.toLowerCase(Locale.ROOT);

        if(grant.equals("grant"))
        {
            if(objectType.equals("procedure") || objectType.equals("function"))
            {
                return "GRANT " + privilege + " ON " + objectType + " " + dbobject + " TO " + user;
            }
            else {
                return "GRANT " + privilege + " ON " + dbobject + " TO " + user;
            }
        }
        else if(grant.equals("revoke"))
        {
            if(objectType.equals("procedure") || objectType.equals("function"))
            {
                return "REVOKE " + privilege + " ON " + objectType + " " + dbobject + " FROM " + user;
            }
            else {
                return "REVOKE " + privilege + " ON " + dbobject + " FROM " + user;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public String fetchAllDBUsers() {
        return "Select u.usename as Username from pg_catalog.pg_user u";
    }

    @Override
    public String definitionName() {
        return "view_definition";
    }

    @Override
    public String updateTimeName() {
        return "create_time";
    }

    @Override
    public String definerName() {
        return "definer";
    }

    private int compareViews(String viewcode, String storedquery) {
        String rawViewCode = viewcode.replaceAll("[\\t\\n\\s\\r]+", "");
        String rawStoredQuery = storedquery.replaceAll("[\\t\\n\\s\\r]+", "");

        if(rawStoredQuery.equals(rawViewCode))
            return 1;

        return -1;
    }
}
