package com.example.Fetcher.Controller;

import com.example.Fetcher.BackendConnection.ConnectionEstablisher;
import com.example.Fetcher.Model.DBColumn;
import com.example.Fetcher.Model.DBObject;
import com.example.Fetcher.Model.DBObjectType;
import com.example.Fetcher.Model.Schema;
import com.example.Fetcher.Utils.URLConstants;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * This is the controller responsible to connect with the backend module.
 */
public class BackendConnectionController
{
    private final ConnectionEstablisher connectionEstablisher;
    private Schema currentSchema;

    public BackendConnectionController() {
        this.connectionEstablisher = new ConnectionEstablisher();
        this.currentSchema = new Schema();
    }

    public Schema getCurrentSchema() {
        return currentSchema;
    }

    public void setCurrentSchema(Schema currentSchema) {
        this.currentSchema = currentSchema;
    }

    public int startupProcedure() throws IOException, JSONException
    {
        return connectionEstablisher.loginMethod();
    }

    /**
     * Fetchs all tables and respective columns from backend module
     * @return 1 if fetching complete, -1 otherwise
     */
    public int fetchBackendTables()
    {
        try {
            fetchSchemaTables();
            fetchAllObjectsBySchema(DBObjectType.Table);
            fetchAllColumns();

            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Fetches all Views, columns and scripts from views from backend module
     * @return 1 if fetching complete, -1 otherwise
     */
    public int fetchBackendViews()
    {
        try {
            fetchSchemaViews();
            fetchAllColumns();
            fetchViewScript();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * This method is set for single view update
     * @param viewname name of the view that will be updated
     * @return 1 if fetching complete, -1 otherwise
     */
    public int fetchBackendViews(String viewname)
    {
        try {
            fetchSchemaViews(viewname);
            fetchAllColumns();
            fetchViewScript();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Fetches all schemas by update period from backend module
     * @param updatePeriod daily, monthly or weekly
     * @return HashMap of schemas (key -> schema connection string; value -> schema)
     * @throws IOException due to http request
     */
    public HashMap<String,Schema> fetchAllSchemas(String updatePeriod) throws IOException {
        String requestURL = URLConstants.GETSCHEMASURL;
        // if update period equals "" then this method fetches all schemas
        if(!updatePeriod.equals(""))
        {
            requestURL = requestURL + "/updateperiod/"+updatePeriod+"/credentials";
        }
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", connectionEstablisher.getBearertoken());
            CloseableHttpResponse response = httpClient.execute(request);
            String result = EntityUtils.toString(response.getEntity());
            HashMap<String, Schema> schemas = new HashMap<>();
            JSONArray outputArray = new JSONArray(result);
            int arraySize = outputArray.length();
            for (int i = 0; i < arraySize; i++) {
                JSONObject o = outputArray.getJSONObject(i);
                Schema schema = extractSchemaInformation(o);
                schemas.put(schema.getConnectionstring(), schema);
            }
            return schemas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts the JSON object fetched from backend module to object Schema
     * @param o json object from backend (DBSchemaDTO)
     * @return object Schema
     * @throws JSONException if necessary json objects are not present
     */
    private Schema extractSchemaInformation(JSONObject o) throws JSONException {
        String connectionString = String.valueOf(o.get("connectionstring"));
        String schemaname = String.valueOf(o.get("name"));
        String username = String.valueOf(o.get("username"));
        String password = String.valueOf(o.get("password"));
        String sqlserver = String.valueOf(o.get("sqlservername"));

        return new Schema(schemaname,connectionString,username,password,sqlserver);
    }

    /**
     * Fetches all dbobjects from backend by schema (set on instance variables) and by type (table/view/procedure)
     * @param type type of the dbobject
     * @throws IOException due to http
     */
    private void fetchAllObjectsBySchema(DBObjectType type) throws IOException {
        String requestURL = URLConstants.GETDBObjectsBySchemaName +currentSchema.getName() + "/type/"+type.toString();
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", connectionEstablisher.getBearertoken());
            CloseableHttpResponse response = httpClient.execute(request);
            String result = EntityUtils.toString(response.getEntity());
            HashMap<String, DBObject> objectMap = new HashMap<>();

            JSONArray outputArray = new JSONArray(result);
            int arraySize = outputArray.length();
            for (int i = 0; i < arraySize; i++) {
                JSONObject object = outputArray.getJSONObject(i);
                DBObject dbObject = extractDBObjectInformation(object);
                objectMap.put(dbObject.getName(), dbObject);
            }
            if (objectMap.size() > 0) {
                this.currentSchema.setObjects(objectMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method
     * @param viewname name of the view
     */
    public void fetchSchemaViews(String viewname) {
        this.setViewNameToCompare(viewname);
    }

    /**
     * This method differs from the previous one because the name of the view is set in order to save only one
     * object that will be updated (this method is called by view on demand update
     * @param viewname name of the view
     */
    private void setViewNameToCompare(String viewname) {
        String requestURL = URLConstants.GETDBObjectsBySchemaName +currentSchema.getName() + "/dbobject/"+viewname;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", connectionEstablisher.getBearertoken());
            CloseableHttpResponse response = httpClient.execute(request);
            String result = EntityUtils.toString(response.getEntity());
            HashMap<String, DBObject> objectMap = new HashMap<>();
            JSONObject outputObject = new JSONObject(result);
            DBObject dbObject = extractDBObjectInformation(outputObject);
            if(dbObject.getName().equals(viewname))
            {
                objectMap.put(dbObject.getName(), dbObject);
            }

            if (objectMap.size() > 0) {
                this.currentSchema.setObjects(objectMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Transforms object from backend (DBObjectDTO) into DBOBject
     * @param o json from backnend
     * @return DBObject with name and object type
     * @throws JSONException if the object o does not contain name or type
     */
    private DBObject extractDBObjectInformation(JSONObject o) throws JSONException {
        String name = String.valueOf(o.get("name"));
        String type = String.valueOf(o.get("type"));

        return new DBObject(name,type,currentSchema.getName());
    }

    /**
     * Fetche all columns
     * @throws IOException due to http request
     */
    public void fetchAllColumns() throws IOException
    {
        if(this.currentSchema.getObjects().size() > 0)
        {
            // For every DBObject get its columns from backend
            for(DBObject dbo : this.currentSchema.getObjects().values())
            {
                String requestURL = URLConstants.GETDBColumnsByDBObjectURL+"dbschema/"+getCurrentSchema().getName()+ "/dbobject/" + dbo.getName();
                try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                    HttpGet request = new HttpGet(requestURL);
                    request.addHeader("content-type", "application/json");
                    request.addHeader("Authorization", connectionEstablisher.getBearertoken());
                    CloseableHttpResponse response = httpClient.execute(request);
                    String result = EntityUtils.toString(response.getEntity());
                    HashMap<String, DBColumn> columnMap = new HashMap<>();

                    JSONArray outputArray = new JSONArray(result);
                    int arraySize = outputArray.length();
                    for (int i = 0; i < arraySize; i++) {
                        JSONObject object = outputArray.getJSONObject(i);
                        DBColumn column = extractDBColumnInformation(object, dbo.getName());
                        columnMap.put(column.getName(), column);
                    }
                    if (columnMap.size() > 0) {
                        this.currentSchema.getObjects().get(dbo.getName()).setColumnMap(columnMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Transforms json from backend (DBColumnDTO) in DBColumn object
     * @param o json from backend
     * @param dbobjectname name of the object
     * @return DBColumn
     * @throws JSONException if json does not contain necessary information
     */
    private DBColumn extractDBColumnInformation(JSONObject o, String dbobjectname) throws JSONException {
        String name = String.valueOf(o.get("name"));
        String datatype = String.valueOf(o.get("datatype"));
        String nullable = String.valueOf(o.get("nullable"));
        String schema = String.valueOf(o.get("dbSchemaName"));

        DBColumn column = new DBColumn(name, datatype,nullable,dbobjectname);
        column.setDbSchemaName(schema);
        return column;
    }

    /**
     * Fetches the latest script from each object stored in backend (view/procedure) in order to later compare with
     * the script fetched from database
     * @throws IOException due to http request
     */
    public void fetchViewScript() throws IOException {
        if(this.currentSchema.getObjects().size() > 0)
        {
            for(DBObject dbo : this.currentSchema.getObjects().values())
            {
                String requestURL = URLConstants.GETDBScriptURL+this.currentSchema.getName()+"/"+dbo.getName();
                try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                    HttpGet request = new HttpGet(requestURL);
                    request.addHeader("content-type", "application/json");
                    request.addHeader("Authorization", connectionEstablisher.getBearertoken());
                    CloseableHttpResponse response = httpClient.execute(request);
                    String result = EntityUtils.toString(response.getEntity());
                    if (result.length() > 0) {
                        JSONObject obj = new JSONObject(result);
                        String code;
                        if (obj.has("code")) {
                            code = obj.getString("code");
                            dbo.setScript(code);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getBearerToken()
    {
        return this.connectionEstablisher.getBearertoken();
    }

    /**
     * Resets the hashmap
     */
    public void reset()
    {
        this.currentSchema.setObjects(new HashMap<>());
    }

    /**
     * Checks if connectionstring, username and password exists
     * @param schemas schemas to be validated
     * @return 0 if all good, -1 if necessary informations in order to establish connection are not present
     */
    public int validateSchemas(HashMap<String, Schema> schemas) {
        int ret = 0;
        for(Schema sch : schemas.values())
        {
            if (sch.getConnectionstring() == null || sch.getUsername() == null || sch.getPassword() == null) {
                ret = -1;
                break;
            }
        }

        return ret;
    }

    public void fetchSchemaTables() {
        try {
            this.fetchAllObjectsBySchema(DBObjectType.Table);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchSchemaViews() {
        try {
            this.fetchAllObjectsBySchema(DBObjectType.View);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int runBackendProceduresUpdate() {
        try{
            fetchProcedures();
            return 1;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Fetches procedures from backend
     */
    private void fetchProcedures() {
        String requestURL = URLConstants.GETDBObjectsBySchemaName +currentSchema.getName() + "/type/"+DBObjectType.Procedure.name();
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", connectionEstablisher.getBearertoken());
            CloseableHttpResponse response = httpClient.execute(request);
            String result = EntityUtils.toString(response.getEntity());
            HashMap<String, DBObject> objectMap = new HashMap<>();

            JSONArray outputArray = new JSONArray(result);
            int arraySize = outputArray.length();
            for (int i = 0; i < arraySize; i++) {
                JSONObject object = outputArray.getJSONObject(i);
                DBObject dbObject = extractDBObjectInformation(object);
                objectMap.put(dbObject.getName(), dbObject);
            }
            if (objectMap.size() > 0) {
                this.currentSchema.setObjects(objectMap);
                fetchViewScript();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
