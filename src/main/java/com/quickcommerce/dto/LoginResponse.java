package com.quickcommerce.dto;

public class LoginResponse {
    public String token;
    public String email;
    public String userRole;
    public Long userId;
    public String user;
    public String message;
    
    public LoginResponse() {}
    public LoginResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

