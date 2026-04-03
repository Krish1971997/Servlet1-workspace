package com.chatapp.model;

import java.time.LocalDateTime;

public class Comment {
    private int id;
    private int postId;
    private int authorId;
    private String authorUsername;
    private String content;
    private boolean hidden;
    private LocalDateTime createdAt;

    public Comment() {}

    // ── Getters / Setters ──────────────────────────────────────
    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }
    public int getPostId()                    { return postId; }
    public void setPostId(int p)              { this.postId = p; }
    public int getAuthorId()                  { return authorId; }
    public void setAuthorId(int a)            { this.authorId = a; }
    public String getAuthorUsername()         { return authorUsername; }
    public void setAuthorUsername(String u)   { this.authorUsername = u; }
    public String getContent()                { return content; }
    public void setContent(String c)          { this.content = c; }
    public boolean isHidden()                 { return hidden; }
    public void setHidden(boolean h)          { this.hidden = h; }
    public LocalDateTime getCreatedAt()       { return createdAt; }
    public void setCreatedAt(LocalDateTime t) { this.createdAt = t; }
}
