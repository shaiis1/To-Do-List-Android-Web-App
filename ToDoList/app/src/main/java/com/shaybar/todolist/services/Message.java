package com.shaybar.todolist.services;

public class Message {
    private MessageType messageType;
    private String formatedMessage;
    private int errorCode;

    /**
     * Message
     * build message object using MessagesFactory enum, to make pretty messages format to Front end.
     */
    public Message(MessageType messageType, MessagesFactory message, Object... args) {
        super();
        this.messageType = messageType;
        this.formatedMessage = String.format(message.getMessage(), args);
        this.errorCode = message.getErrorCode();
    }


    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getFormatedMessage() {
        return formatedMessage;
    }

    public void setFormatedMessage(String formatedMessage) {
        this.formatedMessage = formatedMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", formatedMessage='" + formatedMessage + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
