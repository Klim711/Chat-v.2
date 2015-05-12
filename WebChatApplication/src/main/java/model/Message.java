package model;

import java.util.Date;

public class Message {
    private String id;
    private String user;
    private String text;
    private Boolean isDeleted;
    private String date;

    public Message() {
    }

    public Message(String id, String user, String text, Boolean b) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.isDeleted = b;
    }

    public Message(String id, String user, String text, Boolean isDeleted, String date) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.isDeleted = isDeleted;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void sout() {
        System.out.println(date + " " + user + " : " + text);
    }
    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\",\"user\":\"" + user + "\",\"text\":\"" + text +
                "\",\"isDeleted\":" + isDeleted + ",\"date\":\"" + date + "\"}";
    }
}
