package com.example.omnia.ourproject.SharedClasses;

import java.util.Date;

/**
 * Created by 3ZT on 06-Dec-17.
 */

public class ChatMessage {
    private String MessageState;
    private String Type;
    private String MessageText;
    private String MessageOwner;
    private long MessageTime;

    public ChatMessage(String messageText, String messageOwner,String type,String messageState) {
        MessageText = messageText;
        MessageOwner = messageOwner;
        MessageTime = new Date().getTime();
        Type=type;
        MessageState=messageState;
    }

    public ChatMessage()
    {

    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public String getMessageOwner() {
        return MessageOwner;
    }

    public void setMessageOwner(String messageOwner) {
        MessageOwner = messageOwner;
    }

    public long getMessageTime() {
        return MessageTime;
    }

    public void setMessageTime(long messageTime) {
        MessageTime = messageTime;
    }


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMessageState() {
        return MessageState;
    }

    public void setMessageState(String messageState) {
        MessageState = messageState;
    }
}