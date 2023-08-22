package com.example.Fetcher.BackendConnection.PostMethodsBuilder;

import com.example.Fetcher.Model.DBObject;
import com.example.Fetcher.Utils.URLConstants;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class ScriptBuilder implements Builder<DBObject> {

    @Override
    public int save(HashMap<String, String> parameters, DBObject dbObject) {
        String requestURL = URLConstants.SAVEDBScriptURL;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", parameters.get("token"));

            JSONObject json = new JSONObject();
            json.put("dbSchemaName", parameters.get("dbSchemaName"));
            json.put("dbObjectName", parameters.get("dbObjectName"));
            json.put("code", parameters.get("script"));
            json.put("definer",parameters.get("definer"));
            if(parameters.containsKey("description"))
                json.put("description",parameters.get("description"));

            json.put("name",dbObject.getName());
            StringEntity params = new StringEntity(json.toString());
            request.setEntity(params);
            CloseableHttpResponse response = httpClient.execute(request);

            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * Update and delete method are not used because if a new version of a script is found, it is stored as a more recent
     * version of it and never deleted or updated
     * @param parameters
     * @param dbObject
     * @return
     */
    @Override
    public int update(HashMap<String, String> parameters, DBObject dbObject) {
        return 0;
    }

    @Override
    public int delete(HashMap<String, String> parameters, DBObject dbObject) {
        return 0;
    }
}
