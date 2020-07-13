package com.guoliang.commonlib.entity;

public class MessageEvent {
    private String message;
    private String value;

    public String toString() {
        return "message = " + message
                + ", value = " + value;
    }

    public MessageEvent() {

    }

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(String message, String value) {
        this.message = message;
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
