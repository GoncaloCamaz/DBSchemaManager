package com.example.Fetcher.BackendConnection.PostMethodsBuilder;

import com.example.Fetcher.Model.DBObject;
import com.example.Fetcher.Utils.URLConstants;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class DBObjectBuilder implements Builder<DBObject> {

    /**
     * This method is not used because tables are either deleted or added
     * @param parameters
     * @param dbObject
     * @return
     */
    @Override
    public int update(HashMap<String,String> parameters, DBObject dbObject) {
        String requestURL = URLConstants.UpdateDBObjectURL;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", parameters.get("token"));

            JSONObject json = new JSONObject();
            json.put("dbSchemaName", parameters.get("dbSchemaName"));
            json.put("name",dbObject.getName());
            json.put("type", dbObject.getType().toString());
            json.put("status", "DELETED");
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

    @Override
    public int save(HashMap<String,String> parameters, DBObject dbObject) {
        String requestURL = URLConstants.SAVEDBObjectURL;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", parameters.get("token"));

            JSONObject json = new JSONObject();
            json.put("dbSchemaName", parameters.get("dbSchemaName"));
            json.put("name",dbObject.getName());
            json.put("type", dbObject.getType().toString());
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

    @Override
    public int delete(HashMap<String,String> parameters, DBObject dbObject) {
        String requestURL = URLConstants.DELETEDBObjectURL + "/dbschema/"+parameters.get("dbSchemaName")
                + "/dbobject/"+dbObject.getName();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpDelete request = new HttpDelete(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", parameters.get("token"));
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
}
