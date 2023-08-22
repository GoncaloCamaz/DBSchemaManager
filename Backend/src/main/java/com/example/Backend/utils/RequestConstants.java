package com.example.Backend.utils;

public class RequestConstants
{
    /**
     * DBSchema
     */
    public static final String[] dbschemaSaverequirements = new String[]{"name","connectionstring","sqlservername","platformname","updateperiod"};
    public static final String[] dbschemaUpdaterequirements = new String[]{"name","sqlservername","platformname","updateperiod","connectionstring"};
    public static final String[] dbschemaUpdateCredentialsrequirements = new String[]{"username","password","connectionstring"};
    public static final String[] dbschemaUpdateLastUpdateTime = new String[]{"name","connectionstring"};

    /**
     * DBColumn
     */
    public static final String[] dbcolumnSaveRequirements = new String[]{"name","datatype","dbSchemaName","dbObjectName"};

    /**
     * DBObject
     */
    public static final String[] dbobjectSaveRequirements = new String[]{"name","type","dbSchemaName"};

    /**
     * DBScript
     * dbScriptSaveRequirements - Fetcher updates
     * dbScripCheckandSaveeRequirements - Front End interactions; if sudo true then the user has knowledge about new
     * version of dbscript inserted on the target database by other user but wants to override
     */
    public static final String[] dbscriptSaveRequirements = new String[]{"code","dbSchemaName","dbObjectName"};
    public static final String[] dbscriptCheckAndSaveRequirements = new String[]{"code","dbSchemaName", "sudo", "definer"};

    /**
     * Machine
     */
    public static final String[] machinerequirements = new String[]{"name","machinetype"};
    public static final String[] machineAccessRequirements = new String[]{"machinename","username","password"};
    public static final String[] machineplataformrequirements = new String[]{"machinename","platformname"};
    public static final String[] plataformrequirements = new String[]{"name","url"};
    public static final String[] userrequirements = new String[]{"username", "role"};

    /**
     * DBAccess
     */
    public static final String[] dbaccessSaveRequirements = new String[]{"dbSchemaName","dbObjectName","dbUsername", "privilege","permission"};

    /**
     * Fetcher URL
     */
    public static final String fetcherURL = "http://fetcher:8081";
}
