package com.shaybar.todolist.services;

public class ExceptionMessage extends Exception {
    private Message fullMessage;

    public ExceptionMessage(Message message) {
        super(message.getFormatedMessage());
        this.fullMessage = fullMessage;
    }

    public Message getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(Message fullMessage) {
        this.fullMessage = fullMessage;
    }
}
