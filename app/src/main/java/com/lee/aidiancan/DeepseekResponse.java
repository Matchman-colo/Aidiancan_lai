package com.lee.aidiancan;

import com.google.gson.annotations.SerializedName;

public class DeepseekResponse {
    @SerializedName("choices")
    private Choice[] choices;

    public String getResponse() {
        if (choices != null && choices.length > 0 && choices[0].message != null) {
            return choices[0].message.content;
        }
        return "";
    }

    public static class Choice {
        @SerializedName("message")
        private Message message;
    }

    public static class Message {
        @SerializedName("content")
        private String content;
    }
} 