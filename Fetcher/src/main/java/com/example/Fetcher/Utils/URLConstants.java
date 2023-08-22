package com.example.Fetcher.Utils;

/**
 * Constants used on fetcher
 */
public class URLConstants
{
    /**
     * URL for backend microservice
     * Port: 8082
     * Name of the network: backend
     */
    public static final String BACKENDURL = "http://backend:8082";
    /**
     * Login URL
     */
    public static final String LOGINURL = BACKENDURL+"/dbschemamanager/login";

    /**
     * URLS used for save, update, get and delete DBSchemas, DBObjects, DBColumns and DBScripts
     */
    public static final String GETSCHEMASURL = BACKENDURL + "/dbschema";
    public static final String GETDBObjectsBySchemaName =  BACKENDURL+"/dbobject/dbschema/";
    public static final String GETDBColumnsByDBObjectURL = BACKENDURL+"/dbcolumn/";

    public static final String SAVEDBObjectURL = BACKENDURL+"/dbobject/save";
    public static final String DELETEDBObjectURL = BACKENDURL+"/dbobject/delete";

    public static final String SAVEDBColumnURL = BACKENDURL+"/dbcolumn/save";
    public static final String UPDATEDBColumnURL = BACKENDURL+"/dbcolumn/update";
    public static final String DELETEDBColumnURL = BACKENDURL+"/dbcolumn/delete";

    public static final String GETDBScriptURL = BACKENDURL+"/dbscript/latest/";
    public static final String SAVEDBScriptURL = BACKENDURL+"/dbscript/save";
    public static final String UPDATESCHEMALASTUPDATE = BACKENDURL+"/dbschema/update/updatetime";

    public static final String UpdateDBObjectURL = BACKENDURL+"/dbobject/update" ;
}
