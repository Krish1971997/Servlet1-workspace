package com.chatapp.model;

import java.time.LocalDateTime;
import java.util.List;

public class Post {
    private int id;
    private int authorId;
    private String authorUsername;
    private Integer groupId;          // null = wall post
    private String groupName;
    private String content;
    private boolean hidden;
    private boolean shared;           // true if post_shares rows exist
    private List<Integer> sharedWith; // user IDs
    private LocalDateTime createdAt;
    private List<Comment> comments;

    public Post() {}

    // ── Getters / Setters ──────────────────────────────────────
    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }
    public int getAuthorId()                   { return authorId; }
    public void setAuthorId(int a)             { this.authorId = a; }
    public String getAuthorUsername()          { return authorUsername; }
    public void setAuthorUsername(String u)    { this.authorUsername = u; }
    public Integer getGroupId()                { return groupId; }
    public void setGroupId(Integer g)          { this.groupId = g; }
    public String getGroupName()               { return groupName; }
    public void setGroupName(String n)         { this.groupName = n; }
    public String getContent()                 { return content; }
    public void setContent(String c)           { this.content = c; }
    public boolean isHidden()                  { return hidden; }
    public void setHidden(boolean h)           { this.hidden = h; }
    public boolean isShared()                  { return shared; }
    public void setShared(boolean s)           { this.shared = s; }
    public List<Integer> getSharedWith()       { return sharedWith; }
    public void setSharedWith(List<Integer> l) { this.sharedWith = l; }
    public LocalDateTime getCreatedAt()        { return createdAt; }
    public void setCreatedAt(LocalDateTime t)  { this.createdAt = t; }
    public List<Comment> getComments()         { return comments; }
    public void setComments(List<Comment> c)   { this.comments = c; }
}
