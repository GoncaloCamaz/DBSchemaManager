package com.example.Backend.Security.request;

import java.io.Serializable;

/**
 * Response to login method
 */
public class JwtResponse implements Serializable
{
    private final String token;
    private final String role;

    public JwtResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return this.token;
    }

    public String getRole() {
        return role;
    }
}