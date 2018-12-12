package com.jakebliss.gauchoassistant.gauchoassistant.chat;

public class ChatMessage {

    public static final String TYPE_USER_CHAT = "typeUserChat";
    public static final String TYPE_ASSISTANT_CHAT = "typeAssistantChat";

    private String text;
    private long timestamp;
    private String type;

    public ChatMessage(String text, long timestamp) {
        this(text, TYPE_USER_CHAT);
    }

    public ChatMessage(String text, String type) {
        this.text = text;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }
}