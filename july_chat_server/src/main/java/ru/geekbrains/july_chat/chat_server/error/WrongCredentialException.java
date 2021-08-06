package ru.geekbrains.july_chat.chat_server.error;

public class WrongCredentialException  extends RuntimeException {
    public WrongCredentialException(String message) {
        super(message);
    }
}
