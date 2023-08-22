
package com.example.Backend.Security;

/**
 * Security constants
 */
public class SecurityConstants
{
    public static final String SECRET = "^[a-zA-Z0-9._]+$\r\nsecretKey13829482_894ussduiniunUUU_IN_niunJJ$";
    public static final long EXPIRATION_TIME = 432_000_000; // 5 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_TYPE = "Authorization";
    public static final String CLIENT_DOMAIN_URL = "*";
}
