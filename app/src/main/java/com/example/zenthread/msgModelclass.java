package com.example.zenthread;

public class msgModelclass {
    String message;
    String senderid;
    long timestamp;

    public msgModelclass() {
    }

    public msgModelclass(String message, String senderid, long timestamp) {
        this.message = message;
        this.senderid = senderid;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
