package com.laxmiclinic.dto.response;

public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private String username;
    private String fullName;
    private String email;
    private String role;
    private long expiresIn;

    public AuthResponse() {
    }

    public AuthResponse(String token, String username, String fullName, String email, String role, long expiresIn) {
        this.token = token;
        this.tokenType = "Bearer";
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
