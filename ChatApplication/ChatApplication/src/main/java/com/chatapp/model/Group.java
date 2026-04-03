package com.chatapp.model;

import java.time.LocalDateTime;

public class Group {
    private int id;
    private String name;
    private String description;
    private int createdBy;
    private String createdByUsername;
    private LocalDateTime createdAt;

    public Group() {}

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public String getName()                     { return name; }
    public void setName(String n)               { this.name = n; }
    public String getDescription()              { return description; }
    public void setDescription(String d)        { this.description = d; }
    public int getCreatedBy()                   { return createdBy; }
    public void setCreatedBy(int c)             { this.createdBy = c; }
    public String getCreatedByUsername()        { return createdByUsername; }
    public void setCreatedByUsername(String u)  { this.createdByUsername = u; }
    public LocalDateTime getCreatedAt()         { return createdAt; }
    public void setCreatedAt(LocalDateTime t)   { this.createdAt = t; }
}
