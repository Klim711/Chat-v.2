package model;

public class Message {
    private String id;
    private String user;
    private String text;
    private Boolean isDeleted;

    public Message() {
    }

    public Message(String id, String user, String text, Boolean b) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.isDeleted = b;
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

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\",\"user\":\"" + user + "\",\"text\":\"" + text + "\",\"isDeleted\":" + isDeleted + "}";
    }
}
