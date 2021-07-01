package com.example.justalk;

public class Messages {

    String message;
    String senderUid;
    long timeStamp;

    public Messages() {
    }

    public Messages(String message, String senderUid, long timeStamp) {
        this.message = message;
        this.senderUid = senderUid;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
