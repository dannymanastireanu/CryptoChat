package com.alpha.voice.model;

public class Message {
    private String from;
    private String to;
    private String body;
    private Long timestamp;
    private String type;
    private String cachePath;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Number getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return String.format(
                "{ \"from\": \"%s\", \"to\": \"%s\", \"body\": \"%s\", \"timestamp\": %d, \"type\": \"%s\", \"cachePath\": \"%s\"}",
                this.from, this.to, this.body, this.timestamp, this.type, this.cachePath);
    }
}
