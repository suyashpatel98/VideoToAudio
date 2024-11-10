package com.generic.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private String email, videoKey, requestId;

    @JsonCreator
    public Message(@JsonProperty("email") String email,
                   @JsonProperty("videoKey") String videoKey,
                   @JsonProperty("requestId") String requestId) {
        this.email = email;
        this.videoKey = videoKey;
        this.requestId = requestId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
