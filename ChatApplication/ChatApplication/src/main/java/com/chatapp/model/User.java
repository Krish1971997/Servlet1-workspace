package com.chatapp.model;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;          // "user" | "admin"
    private boolean active;
    private LocalDateTime createdAt;

    public User() {}

    public User(int id, String username, String email, String role, boolean active, LocalDateTime createdAt) {
        this.id = id; this.username = username; this.email = email;
        this.role = role; this.active = active; this.createdAt = createdAt;
    }

    // ── Getters / Setters ──────────────────────────────────────
    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }
    public String getUsername()             { return username; }
    public void setUsername(String u)       { this.username = u; }
    public String getEmail()                { return email; }
    public void setEmail(String e)          { this.email = e; }
    public String getPassword()             { return password; }
    public void setPassword(String p)       { this.password = p; }
    public String getRole()                 { return role; }
    public void setRole(String r)           { this.role = r; }
    public boolean isActive()               { return active; }
    public void setActive(boolean a)        { this.active = a; }
    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void setCreatedAt(LocalDateTime t){ this.createdAt = t; }

    public boolean isAdmin()                { return "admin".equals(role); }
}
