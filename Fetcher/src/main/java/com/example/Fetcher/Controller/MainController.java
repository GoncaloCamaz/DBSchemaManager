package com.example.Fetcher.Controller;

import com.example.Fetcher.Model.DBObject;
import com.example.Fetcher.Model.Schema;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

public class MainController {
    private final BackendConnectionController backendConnectionController;
    private final DatabaseConnectionController databaseConnectionController;
    private final UpdateController updateController;
    private HashMap<String, Schema> schemas;
    private final String updateperiod;
    private String definer;

    public MainController(String updateperiod) {
        this.backendConnectionController = new BackendConnectionController();
        this.databaseConnectionController = new DatabaseConnectionController();
        this.updateController = new UpdateController();
        this.updateperiod = updateperiod;
        this.schemas = new HashMap<>();
        this.definer = "";
    }

    public MainController() {
        this.backendConnectionController = new BackendConnectionController();
        this.databaseConnectionController = new DatabaseConnectionController();
        this.updateController = new UpdateController();
        this.schemas = new HashMap<>();
        this.updateperiod = "";
        this.definer = "";
    }

    /**
     * This method starts daily, weekly and monthly updates
     */
    public void startUpdate() {
        try {
            if (this.backendConnectionController.startupProcedure() == 200) {
                this.updateController.setBearertoken(this.backendConnectionController.getBearerToken());
                HashMap<String, Schema> fetchedSchemas = this.backendConnectionController.fetchAllSchemas(this.updateperiod);

                if (fetchedSchemas.size() > 0 && this.backendConnectionController.validateSchemas(fetchedSchemas) != -1) {
                    updateSchemaMetadata(fetchedSchemas);
                } else {
                    System.out.println("=== NO SCHEMAS FOUND ===\n");
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method starts update on view by demand
     * @param schema schema is provided for connection string, username and password
     * @param viewname name of the view that will be updated
     * @param definer name of the user that inserted the view
     */
    public void startOnDemandViewUpdate(Schema schema, String viewname, String definer) {
        try {
            if(this.backendConnectionController.startupProcedure() == 200)
            {
                this.setDefiner(definer);
                this.updateController.setBearertoken(this.backendConnectionController.getBearerToken());
                HashMap<String,Schema> fetchedSchemas = new HashMap<>();
                fetchedSchemas.put(schema.getConnectionstring(), schema);
                this.backendConnectionController.setCurrentSchema(schema.clone());
                this.databaseConnectionController.setCurrentSchema(schema.clone());
                if(fetchedSchemas.size() > 0 && this.backendConnectionController.validateSchemas(fetchedSchemas) != -1)
                {
                    if(this.backendConnectionController.fetchBackendViews(viewname) == 1
                            && this.databaseConnectionController.runTargetDatabaseViewsUpdate(viewname) == 1)
                    {
                        runViewsUpdate();
                        runColumnsUpdate();
                        reset();
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called to start updates on an recently inserted schema by an user.
     * This can also be called if the user presses the on demand update on frontend
     * @param schema schema inserted
     */
    public void startOnDemandUpdate(Schema schema) {
        try {
            if (this.backendConnectionController.startupProcedure() == 200) {
                this.updateController.setBearertoken(this.backendConnectionController.getBearerToken());
                HashMap<String, Schema> fetchedSchemas = new HashMap<>();
                fetchedSchemas.put(schema.getConnectionstring(), schema);
                if (fetchedSchemas.size() > 0 && this.backendConnectionController.validateSchemas(fetchedSchemas) != -1) {
                    updateSchemaMetadata(fetchedSchemas);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is the core of the Fetcher module.
     * @param fetchedSchemas schemas fetched from backend module
     */
    private void updateSchemaMetadata(HashMap<String, Schema> fetchedSchemas) {
        this.schemas = fetchedSchemas;
        for (Schema sch : this.schemas.values()) {
            this.backendConnectionController.setCurrentSchema(sch.clone());
            this.databaseConnectionController.setCurrentSchema(sch.clone());
            if (this.databaseConnectionController.establishConnection() == 1) {
                if (this.backendConnectionController.fetchBackendTables() == 1
                        && this.databaseConnectionController.runTargetDatabaseTablesUpdate() == 1) {
                    runTablesUpdate();
                    runColumnsUpdate();
                    reset(); // reset to delete objects fro both backend and database controller that are no longer needed
                }
                if (this.backendConnectionController.fetchBackendViews() == 1
                        && this.databaseConnectionController.runTargetDatabaseViewsUpdate() == 1) {
                    runViewsUpdate();
                    runColumnsUpdate();
                    reset(); // reset to delete objects fro both backend and database controller that are no longer needed
                }
                if (this.backendConnectionController.runBackendProceduresUpdate() == 1
                        && this.databaseConnectionController.runTargetDatabaseProceduresUpdate() == 1) {
                    runProceduresUpdate();
                    reset(); // reset to delete objects fro both backend and database controller that are no longer needed
                }
                this.databaseConnectionController.closeConnection(); // connection closed
                this.updateController.updateSchemaLastUpdate(sch); // sends the update to the schema Last Update On column
            }
        }
    }

    /**
     * Sends both versions of the schema (from backend and target database) to UpdateController
     */
    private void runTablesUpdate() {
        System.out.println("=== STARTING TABLES UPDATE ===\n");
        this.updateController.updateDBObjects(
                this.backendConnectionController.getCurrentSchema(),
                this.databaseConnectionController.getCurrentSchema()
        );
        System.out.println("=== TABLES UPDATE CHECK ===\n");
    }

    /**
     * Sends both versions of the objects columns (from backend and target database) to UpdateController
     */
    private void runColumnsUpdate() {
        System.out.println("=== STARTING COLUMNS UPDATE ===\n");
        for (DBObject dbo : this.databaseConnectionController.getCurrentSchema().getObjects().values()) {
            this.updateController.updateDBColumns(
                    this.backendConnectionController.getCurrentSchema().getObjects().get(dbo.getName()),
                    this.databaseConnectionController.getCurrentSchema().getObjects().get(dbo.getName()),
                    this.backendConnectionController.getCurrentSchema().getName()
            );
        }
        System.out.println("=== COLUMNS UPDATE CHECK ===\n");
    }

    /**
     * Sends both versions of the schema (from backend and target database) to UpdateController
     * After updating the dbobjects it updates the scripts
     */
    private void runViewsUpdate() {
        System.out.println("=== STARTING VIEWS UPDATE ===\n");
        this.updateController.updateDBObjects(
                this.backendConnectionController.getCurrentSchema(),
                this.databaseConnectionController.getCurrentSchema()
        );
        for (DBObject dbo : this.databaseConnectionController.getCurrentSchema().getObjects().values()) {
            this.updateController.updateDBScripts(
                    this.backendConnectionController.getCurrentSchema().getObjects().get(dbo.getName()),
                    this.databaseConnectionController.getCurrentSchema().getObjects().get(dbo.getName()),
                    this.backendConnectionController.getCurrentSchema().getName(),
                    this.definer
            );
        }
        System.out.println("=== VIEWS UPDATE CHECK ===\n");
    }

    /**
     * Sends both versions of the schema (from backend and target database) to UpdateController
     */
    private void runProceduresUpdate() {
        System.out.println("=== STARTING PROCEDURES UPDATE ===\n");
        this.updateController.updateProcedures(
                this.backendConnectionController.getCurrentSchema(),
                this.databaseConnectionController.getCurrentSchema());
        System.out.println("=== PROCEDURES UPDATE CHECK ===\n");
    }

    /**
     * Resets all objects
     */
    private void reset() {
        this.backendConnectionController.reset();
        this.databaseConnectionController.reset();
    }

    public void setDefiner(String definer) {
        this.definer = definer;
    }
}