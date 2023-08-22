package com.example.Fetcher.Utils;

/**
 * Class to map the json response from backend to an object containing the access token and user role
 */
public class BackendResponseToken {
    private String token;
    private String role;

    public BackendResponseToken() {
    }

    public BackendResponseToken(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
