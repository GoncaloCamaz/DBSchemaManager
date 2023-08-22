package com.example.Fetcher.BackendConnection.PostMethodsBuilder;

import com.example.Fetcher.Model.DBColumn;
import com.example.Fetcher.Utils.URLConstants;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;

public class DBColumnBuilder implements Builder<DBColumn> {

    @Override
    public int update(HashMap<String,String> parameters, DBColumn dbColumn) {
        String requestURL = URLConstants.UPDATEDBColumnURL;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", parameters.get("token"));

            JSONObject json = new JSONObject();
            json.put("dbObjectName", parameters.get("dbObjectName"));
            json.put("dbSchemaName", parameters.get("dbSchemaName"));
            json.put("name",dbColumn.getName());
            json.put("datatype", dbColumn.getDatatype());
            json.put("nullable", dbColumn.getNullable());
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
    public int save(HashMap<String,String> parameters, DBColumn dbColumn) {
        String requestURL = URLConstants.SAVEDBColumnURL;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(requestURL);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", parameters.get("token"));

            JSONObject json = new JSONObject();
            json.put("dbObjectName", parameters.get("dbObjectName"));
            json.put("dbSchemaName", parameters.get("dbSchemaName"));
            json.put("name",dbColumn.getName());
            json.put("datatype", dbColumn.getDatatype());
            json.put("nullable", dbColumn.getNullable());
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
    public int delete(HashMap<String,String> parameters, DBColumn dbColumn) {
        String requestURL = URLConstants.DELETEDBColumnURL + "/dbschema/"
                + parameters.get("dbSchemaName") + "/dbobject/"+parameters.get("dbObjectName")
                + "/dbcolumn/"+dbColumn.getName();
        CloseableHttpClient httpClient = HttpClients.createDefault();
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
