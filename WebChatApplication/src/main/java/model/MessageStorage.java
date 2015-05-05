package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.Message;

public class MessageStorage {

    private static final List<Message> INSTANSE = Collections.synchronizedList(new ArrayList<Message>());

    private MessageStorage() {
    }

    public static List<Message> getStorage() {
        return INSTANSE;
    }

    public static void addMessage(Message Message) {
        INSTANSE.add(Message);
    }

    public static void addAll(Message[] Messages) {
        INSTANSE.addAll(Arrays.asList(Messages));
    }

    public static void addAll(List<Message> Messages) {
        INSTANSE.addAll(Messages);
    }

    public static int getSize() {
        return INSTANSE.size();
    }

    public static List<Message> getMessages() {
        return INSTANSE.subList(0, INSTANSE.size());
    }

    public static Message getMessageById(String id) {
        for (Message Message : INSTANSE) {
            if (Message.getId().equals(id)) {
                return Message;
            }
        }
        return null;
    }
}
