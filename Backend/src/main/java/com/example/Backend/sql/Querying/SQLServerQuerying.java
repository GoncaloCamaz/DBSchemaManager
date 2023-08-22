package com.example.Backend.sql.Querying;

import com.example.Backend.dto.model.DBScriptDTO;
import com.example.Backend.dto.model.ResultSetDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLServerQuerying implements QueryBuilder{
    @Override
    public String getViewNameFromCreateViewStatement(String query) {
        String firstQuery = query.replaceAll("[\\t\\n\\r]+"," ");
        Pattern pattern = Pattern.compile("(?i)(CREATE\\s([a-zA-Z\\s=]+)?VIEW\\s)\\[[a-zA-Z0-9_\\s]+]");
        Pattern patternText = Pattern.compile("(?i)(CREATE\\s([a-zA-Z\\s]+)?)VIEW\\s([a-zA-Z0-9_]+)\\sas\\s");

        Matcher matcher = pattern.matcher(firstQuery);
        Matcher matcherText = patternText.matcher(firstQuery);
        String viewname;

        if(matcherText.find())
        {
            viewname = matcherText.group();
            viewname = viewname.replaceFirst("(?i)(CREATE)", "");
            viewname = viewname.replaceFirst("(?i)(\\s([a-zA-Z\\s]+)?\\sview\\s)", " ");
            viewname = viewname.replaceFirst("(?i)(\\sas\\s?)", "");
            viewname = viewname.replaceAll("\\s", "");
        }
        else
        {
            viewname = matcher.group();
            viewname = viewname.replaceFirst("(?i)(CREATE\\s([a-zA-Z\\s=]+)?VIEW\\s)", "");

        }

        return viewname;
    }

    @Override
    public String select100RowsFromViewQuery(String viewname) {
        return "SELECT * FROM " + viewname + " LIMIT 100;";
    }

    @Override
    public String fetchViewInformationsQuery(String viewname) {
        return "SELECT name, modify_date, view_definition FROM sys.views as sviw " +
        "inner join INFORMATION_SCHEMA.VIEWS as inf on inf.TABLE_NAME = " + "'"+viewname+ "'" +" and  sviw.name = "+ "'"+viewname+ "'"+ ";";
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
        LocalDateTime datetime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if(latest != null)
        {
            if(datetime != null && latest.getDate() != null)
            {
                if(compareViews(datetime, latest.getDate()) != 1)
                {
                    resultSetDTO.setErrormessage("There might be updates not catched on " + viewname + ".");
                    resultSetDTO.setWarningmessage("Last updated on " + datetime);
                }
            }
            else
            {
                if(compareViewStrings(query, latest.getCode()) != 1)
                {
                    resultSetDTO.setErrormessage("There might be updates not catched on " + viewname + ".");
                    resultSetDTO.setWarningmessage("Last updated on " + datetime);
                }
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
    public String grant(String grant, String dbobject, String objectType, String user, String privilege) {
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
        return "Select name as Username from sysusers";
    }

    @Override
    public String definitionName() {
        return "view_definition";
    }

    @Override
    public String updateTimeName() {
        return "modify_date";
    }

    @Override
    public String definerName() {
        return "";
    }

    private int compareViews(LocalDateTime newviewDate, LocalDateTime storedviewDate) {

        if(newviewDate.isAfter(storedviewDate))
            return -1;

        return 1;
    }

    private int compareViewStrings(String newquery, String oldquery)
    {
        String rawViewCode = newquery.replaceAll("[\\t\\n\\s\\r]+", "");
        String rawStoredQuery = oldquery.replaceAll("[\\t\\n\\s\\r]+", "");

        if(rawStoredQuery.equals(rawViewCode))
            return 1;

        return -1;
    }
}
