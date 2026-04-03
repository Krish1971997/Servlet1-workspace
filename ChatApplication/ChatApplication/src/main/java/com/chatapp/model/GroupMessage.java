package com.chatapp.model;

import java.time.LocalDateTime;

public class GroupMessage {
    private int id;
    private int groupId;
    private int senderId;
    private String senderUsername;
    private String message;
    private boolean hidden;
    private LocalDateTime createdAt;

    public GroupMessage() {}

    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }
    public int getGroupId()                   { return groupId; }
    public void setGroupId(int g)             { this.groupId = g; }
    public int getSenderId()                  { return senderId; }
    public void setSenderId(int s)            { this.senderId = s; }
    public String getSenderUsername()         { return senderUsername; }
    public void setSenderUsername(String u)   { this.senderUsername = u; }
    public String getMessage()                { return message; }
    public void setMessage(String m)          { this.message = m; }
    public boolean isHidden()                 { return hidden; }
    public void setHidden(boolean h)          { this.hidden = h; }
    public LocalDateTime getCreatedAt()       { return createdAt; }
    public void setCreatedAt(LocalDateTime t) { this.createdAt = t; }
}
