package com.example.Fetcher.Resource;

import com.example.Fetcher.Controller.MainController;
import com.example.Fetcher.Model.Schema;
import com.example.Fetcher.Model.SQLServerType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * API Endpoint defined in this class
 */
@RestController
@RequestMapping("/update")
public class UpdateResource {

    /**
     * Updates a schema
     * @param request parameters to identiy the schema
     */
    @PostMapping("schema")
    public void updateSchema(@RequestBody HashMap<String, String> request)
    {
        System.out.println("SCHEMA REQUEST CATCHED");
        MainController controller = new MainController();
        Schema schema = getSchema(request);
        controller.startOnDemandUpdate(schema);
    }

    /**
     * Transforms JSON from backend on a Schema
     * @param request
     * @return
     */
    private Schema getSchema(HashMap<String, String> request) {
        Schema schema = new Schema();
        schema.setName(request.get("name")); // name of the schema to apply on queries
        schema.setConnectionstring(request.get("connectionstring")); // connection string
        schema.setPassword(request.get("password")); // password of the schema
        schema.setUsername(request.get("username")); // username
        schema.setServer(SQLServerType.valueOf(request.get("sqlservername"))); // to know which MetaDataQuerying to use

        return schema;
    }

    /**
     * Updates a view everytime a new view or update is inserted by an user
     * @param request parameters that should contain information about the schema and the name of the view to be
     *                updated
     */
    @PostMapping("schema/view")
    public void updateSchemaView(@RequestBody HashMap<String, String> request)
    {
        MainController controller = new MainController();
        Schema schema = getSchema(request);
        String viewname = request.get("viewname");
        String definer = request.get("definer");
        controller.startOnDemandViewUpdate(schema,viewname, definer);
    }
}
