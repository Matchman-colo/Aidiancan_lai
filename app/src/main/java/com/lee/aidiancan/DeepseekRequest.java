package com.lee.aidiancan;

import com.google.gson.annotations.SerializedName;

public class DeepseekRequest {
    @SerializedName("model")
    private String model = "deepseek-chat";

    @SerializedName("messages")
    private Message[] messages;

    public DeepseekRequest(String userMessage) {
        this.messages = new Message[]{
            new Message("user", userMessage)
        };
    }

    public static class Message {
        @SerializedName("role")
        private String role;

        @SerializedName("content")
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
} 