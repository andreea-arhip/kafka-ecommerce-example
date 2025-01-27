package org.example.application.exception;

public class MessageSendException extends RuntimeException {
    public MessageSendException(String message) {
        super(message);
    }
}
