package com.example.Fetcher.BackendConnection;

import com.example.Fetcher.Utils.BackendResponseToken;
import com.example.Fetcher.Utils.SecurityConstants;
import com.example.Fetcher.Utils.URLConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ConnectionEstablisher
{
    private String bearertoken;

    public ConnectionEstablisher() {
        this.bearertoken = "";
    }

    /**
     * This Method establishes login with backend module.
     * @return 200 if a token is received; -1 otherwise
     * @throws JSONException due to usage of json objects
     * @throws IOException due to connection via http
     */
    public int loginMethod() throws JSONException, IOException {
        String requestURL = URLConstants.LOGINURL;
        JSONObject json = new JSONObject();
        json.put("username", SecurityConstants.USERNAME);
        json.put("password", SecurityConstants.PASSWORD);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(requestURL);
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            ObjectMapper mapper = new ObjectMapper();
            String raw_response = EntityUtils.toString(response.getEntity());
            BackendResponseToken token = mapper.readValue(raw_response, BackendResponseToken.class);
            if(response.getStatusLine().getStatusCode() == 200)
            {
                this.setBearertoken(token.getToken());
                return 200;
            }
            else
            {
                return response.getStatusLine().getStatusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
        return -1;
    }

    public String getBearertoken() {
        return bearertoken;
    }

    public void setBearertoken(String bearertoken) {
        this.bearertoken = bearertoken;
    }
}
