package com.self.chatapp;

/**
 * Created by Nishant on 17-04-2017.
 */

public class Userclass {
    String id, usename, message;

    public Userclass() {
    }

    public Userclass(String id, String usename, String message) {
        this.id = id;
        this.usename = usename;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getUsename() {
        return usename;
    }

    public String getMessage() {
        return message;
    }
}
