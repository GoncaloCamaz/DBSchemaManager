package com.example.Fetcher.BackendConnection.PostMethodsBuilder;

import com.example.Fetcher.Model.Schema;
import com.example.Fetcher.Utils.URLConstants;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class DBSchemaBuilder implements Builder<Schema>{

    @Override
    public int update(HashMap<String, String> parameters, Schema schema) {
        String requestURL = URLConstants.UPDATESCHEMALASTUPDATE;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", parameters.get("token"));

            JSONObject json = new JSONObject();
            json.put("name", schema.getName());
            json.put("connectionstring", schema.getConnectionstring());
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
     * Schemas are either saved or deleted on backend and never by the fetcher
     * @param parameters optional parameters
     * @param schema information
     * @return result
     */
    @Override
    public int save(HashMap<String, String> parameters, Schema schema) {
        return 0;
    }

    @Override
    public int delete(HashMap<String, String> parameters, Schema schema) {
        return 0;
    }
}
