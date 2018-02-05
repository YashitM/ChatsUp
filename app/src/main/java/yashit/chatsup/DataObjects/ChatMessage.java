package yashit.chatsup.DataObjects;

import java.util.ArrayList;

/**
 * Created by yashi on 02-Jul-17.
 */

public class ChatMessage {
    private ArrayList<Message> messages;
    private String sender;
    private String receiver;

    public ChatMessage() {
        messages = new ArrayList<>();
    }

    public ChatMessage(String sender, String receiver) {
        messages = new ArrayList<>();
        this.sender = sender;
        this.receiver = receiver;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addMessages(Message message) {
        this.messages.add(message);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
