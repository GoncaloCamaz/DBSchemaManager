package com.example.Fetcher.Controller;

import com.example.Fetcher.BackendConnection.PostMethodsBuilder.*;
import com.example.Fetcher.Model.*;

import java.util.HashMap;

public class UpdateController
{
    private String bearertoken;

    public UpdateController() {
        bearertoken = "";
    }

    /**
     * Compares dbobjects from backend and target database
     * @param backendDBSchema schema from backend module
     * @param targetDBSchema schema from target database
     */
    public void updateDBObjects(Schema backendDBSchema, Schema targetDBSchema)
    {
        HashMap<String, DBObject> objectTarget = targetDBSchema.getObjects();
        HashMap<String, DBObject> objectBackend = backendDBSchema.getObjects();
        // if there are objects in target the connection will be checked
        if(objectTarget.size() > 0)
        {
            for(DBObject db : objectTarget.values())
            {
                if(!backendDBSchema.getObjects().containsKey(db.getName()))
                {
                    saveDBObject(targetDBSchema,db);
                }
            }
        }

        // Delete checks if there are objects on backend schema fetched
        if(objectBackend.size() > 0)
        {
            for(DBObject db : objectBackend.values())
            {
                if(!objectTarget.containsKey(db.getName()))
                {
                    if(db.getType().equals(DBObjectType.View) || db.getType().equals(DBObjectType.Procedure))
                    {
                        updateDBObjectStatus(targetDBSchema, db);
                    }else
                    {
                        deleteDBObject(targetDBSchema, db);
                    }
                }
            }
        }
    }

    private void updateDBObjectStatus(Schema targetDBSchema, DBObject db) {
        Builder<DBObject> builder = new DBObjectBuilder();

        HashMap<String, String> params = new HashMap<>();
        params.put("token", this.bearertoken);
        params.put("dbSchemaName", targetDBSchema.getName());
        params.put("status", "DELETED");
        builder.update(params,db);
        System.out.println("<UPDATED STATUS - DELETED> " + db.toString());
    }

    /**
     * Prepares parameters for saving a new dbobject
     * @param sch schema information
     * @param dbo dbobject that will be stored
     */
    private void saveDBObject(Schema sch, DBObject dbo)
    {
        Builder<DBObject> builder = new DBObjectBuilder();

        HashMap<String, String> params = new HashMap<>();
        params.put("token", this.bearertoken);
        params.put("dbSchemaName", sch.getName());
        params.put("connectionstring", sch.getConnectionstring());
        params.put("script", "null");
        builder.save(params,dbo);
        System.out.println("<ADDED> " + dbo.toString());
    }

    /**
     * Deletes a dbobject
     * @param sch schema information
     * @param dbo dbobject that will be deleted
     */
    private void deleteDBObject(Schema sch, DBObject dbo)
    {
        Builder<DBObject> builder = new DBObjectBuilder();

        HashMap<String, String> params = new HashMap<>();
        params.put("token", this.bearertoken);
        params.put("dbSchemaName", dbo.getName());
        params.put("connectionstring", sch.getConnectionstring());
        builder.delete(params,dbo);
        System.out.println("<DELETED> " + dbo.toString());
    }

    /**
     * Updates dbcolumns
     * @param backendDBO dbobject from backend
     * @param targetDBO dbobject from target database
     * @param schemaname name of the schema
     */
    public void updateDBColumns(DBObject backendDBO, DBObject targetDBO, String schemaname)
    {
        if(backendDBO == null)
        {
            for(DBColumn dbc : targetDBO.getColumnMap().values())
            {
                saveDBColumn(schemaname, targetDBO,dbc);
            }
        }
        else {
            for(DBColumn dbc : targetDBO.getColumnMap().values())
            {
                if(!backendDBO.getColumnMap().containsKey(dbc.getName()))
                {
                    saveDBColumn(schemaname,targetDBO,dbc);
                }
                else
                {
                    updateDBColumn(schemaname,targetDBO,dbc);
                }
            }

            // if there are columns in backend, perform necessity for delete dbcolumns
            if(backendDBO.getColumnMap().values().size() > 0)
            {
                for(DBColumn dbc : backendDBO.getColumnMap().values())
                {
                    if(!targetDBO.getColumnMap().containsKey(dbc.getName()))
                    {
                        deleteDBCOlumn(schemaname, targetDBO, dbc);
                    }
                }
            }
        }
    }

    /**
     * Method to prepare required parameters to save a new dbcolumn
     * @param schemaname name of the schema
     * @param dbo dbobject name
     * @param dbc dbcolumn
     */
    private void saveDBColumn(String schemaname, DBObject dbo, DBColumn dbc)
    {
        Builder<DBColumn> builder = new DBColumnBuilder();

        HashMap<String, String> params = new HashMap<>();
        params.put("token", this.bearertoken);
        params.put("dbSchemaName", schemaname);
        params.put("dbObjectName", dbo.getName());
        builder.save(params, dbc);
        System.out.println("<ADDED> " + dbc.toString());
    }

    /**
     * Prepares a delete request
     * @param schemaname name of the schema
     * @param targetDBO object that column belongs to
     * @param dbc dbcolumn object
     */
    private void deleteDBCOlumn(String schemaname, DBObject targetDBO, DBColumn dbc)
    {
        Builder<DBColumn> builder = new DBColumnBuilder();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", this.bearertoken);
        params.put("dbSchemaName", schemaname);
        params.put("dbObjectName", targetDBO.getName());
        builder.delete(params, dbc);
        System.out.println("<DELETED> " + dbc.toString());
    }

    /**
     * Prepares an update request
     * @param schemaname name of the schema
     * @param targetDBO dbobjcet that the column belongs to
     * @param dbc dbcolumn
     */
    private void updateDBColumn(String schemaname, DBObject targetDBO, DBColumn dbc)
    {
        Builder<DBColumn> builder = new DBColumnBuilder();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", this.bearertoken);
        params.put("dbSchemaName", schemaname);
        params.put("dbObjectName", targetDBO.getName());
        builder.update(params, dbc);
        System.out.println("<UPDATED> " + dbc.toString());
    }

    /**
     * This method updates the scripts
     * @param backendDBO object from backend
     * @param targetDBO object from target database
     * @param schema name of the schema
     * @param definer name of the user
     */
    public void updateDBScripts(DBObject backendDBO, DBObject targetDBO, String schema, String definer)
    {
        if(backendDBO != null)
        {
            if(backendDBO.getScript().equals("") || backendDBO.getScript() == null)
            {
                saveDBScript(schema, targetDBO,definer);
            }
            else
            {
                String backendDBOScript = backendDBO.getScript();
                String targetDBOScript = targetDBO.getScript();
                if(compareScripts(backendDBOScript, targetDBOScript) != 1)
                {
                    saveDBScript(schema, targetDBO, definer);
                }
            }
        }
        else // if no backend dbo is found
        {
            saveDBScript(schema, targetDBO,definer);
        }
    }

    /**
     * Scripts are compared with script string
     * @param scriptBackend script from backend
     * @param scriptTarget script from target database
     * @return 1 if script is the same, -1 otherwise
     */
    private int compareScripts(String scriptBackend, String scriptTarget)
    {
        int ret = -1;
        // removes all spaces, new line or tabs from the script
        scriptBackend.replaceAll("\s\n\t","");
        scriptTarget.replaceAll("\s\n\t","");

        if(scriptBackend.equals(scriptTarget))
        {
            ret = 1;
        }

        return ret;
    }

    /**
     * This method prepares the parameters to save a new dbscript
     * @param schema name of the schma
     * @param dbo dbobject
     * @paaram definer name of the user that created the view
     */
    private void saveDBScript(String schema, DBObject dbo, String definer)
    {
        Builder<DBObject> builder = new ScriptBuilder();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", this.bearertoken);
        params.put("dbObjectName", String.valueOf(dbo.getName()));
        params.put("dbSchemaName", String.valueOf(schema));
        params.put("description", "Fetched from target database");
        params.put("script", dbo.getScript());
        params.put("definer", definer);
        builder.save(params, dbo);
    }

    /**
     * Method to update procedures and their scripts
     * no columns are updated
     * @param backendDBSchema schema from backend (containing objects procedure)
     * @param targetDBSchema schema from target database (containing objects procedure)
     */
    public void updateProcedures(Schema backendDBSchema, Schema targetDBSchema) {
        if(targetDBSchema.getObjects().size() > 0)
        {
            HashMap<String, DBObject> objectTarget = targetDBSchema.getObjects();
            HashMap<String, DBObject> objectBackend = backendDBSchema.getObjects();

            for(DBObject db : objectTarget.values())
            {
                if(!backendDBSchema.getObjects().containsKey(db.getName()))
                {
                    saveDBObject(targetDBSchema,db);
                    updateDBScripts(null, db, targetDBSchema.getName(), "");
                }
            }

            if(objectBackend.size() > 0)
            {
                for(DBObject db : objectBackend.values())
                {
                    if(!targetDBSchema.getObjects().containsKey(db.getName()))
                    {
                        deleteDBObject(targetDBSchema,db);
                    }
                    else
                    {
                        if(compareScripts(db.getScript(), targetDBSchema.getObjects().get(db.getName()).getScript()) != 1)
                        {
                            updateDBScripts(db, targetDBSchema.getObjects().get(db.getName()),targetDBSchema.getName(),"");
                        }
                    }
                }
            }
        }
        else
        {
            System.out.println("NO DBOBJECTS FOUND IN: " + backendDBSchema.getName()+" SCHEMA\n");
        }
    }

    /**
     * Updates the Last Update on from the DBSchemaDTO
     * the first parameter is new hasmap because extra parameters are not required
     * @param schema schema information
     */
    public void updateSchemaLastUpdate(Schema schema)
    {
        Builder<Schema> builder = new DBSchemaBuilder();
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("token",this.bearertoken);
        builder.update(parameters, schema);
    }

    /**
     * Sets the bearer token received from backend
     * @param bearertoken token received
     */
    public void setBearertoken(String bearertoken) {
        this.bearertoken = bearertoken;
    }
}