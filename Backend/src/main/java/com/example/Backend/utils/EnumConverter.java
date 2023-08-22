package com.example.Backend.utils;

import java.util.Locale;

public class EnumConverter {

    public static String getMySQLServerType(String input)
    {
        String treated = input.toLowerCase(Locale.ROOT);

        switch (treated){
            case "mysql":
                return SQLServerType.MySQL.toString();
            case "oracle":
                return SQLServerType.Oracle.toString();
            case "postgresql":
                return SQLServerType.PostgreSQL.toString();
            default:
                return SQLServerType.SQLServer.toString();
        }
    }
    public static SQLServerType getEnumMySQLServerType(String input)
    {
        String treated = input.toLowerCase(Locale.ROOT);

        switch (treated){
            case "mysql":
                return SQLServerType.MySQL;
            case "oracle":
                return SQLServerType.Oracle;
            case "postgresql":
                return SQLServerType.PostgreSQL;
            default:
                return SQLServerType.SQLServer;
        }
    }

    public static String getUpdatePeriod(String input)
    {
        String treated = input.toLowerCase(Locale.ROOT);

        switch (treated){
            case "weekly":
                return UpdatePeriod.Weekly.toString();
            case "monthly":
                return UpdatePeriod.Monthly.toString();
            default:
                return UpdatePeriod.Daily.toString();
        }
    }
}
