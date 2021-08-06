package ru.geekbrains.july_chat.chat_server;

import ru.geekbrains.july_chat.chat_server.error.UserNotFoundException;
import ru.geekbrains.july_chat.chat_server.error.WrongCredentialException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Handler;

public class ChatClientHandler {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Thread handlerThread;
    private JulyChatServer server;
    private String currentUser;

    public ChatClientHandler(Socket socket, JulyChatServer server) {
        try {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Handler created");
            this.server = server;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle() {
        handlerThread = new Thread(() -> {
            authorize();
            try {
                while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
                    String message = in.readUTF();
                    System.out.printf("Client #%s: %s\n", this.currentUser, message);
                    if (message.substring(7).startsWith("/w")) {
                        server.sendPrivateMessage(message);
                    } else {
                        server.broadcastMessage(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                server.removeAuthorizedClientFromList(this);
            }
        });
        handlerThread.start();
    }

    private void authorize() {
        while (true) {
            try {
                String message = in.readUTF();
                if (message.startsWith("auth: ")) {
                    String[] credentials = message.substring(6).split("\\s");
                    try {
                        this.currentUser = server.getAuthService().getNicknameByLoginAndPassword(credentials[0], credentials[1]);
                        this.server.addAuthorizedClientToList(this);
                        sendMessage("authok: " + this.currentUser);
                        break;
                    } catch (WrongCredentialException e) {
                        sendMessage("ERROR: Wrong credentials");
                    } catch (UserNotFoundException e) {
                        sendMessage("ERROR: User not found!");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Thread getHandlerThread() {
        return handlerThread;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void sendMessage(String message) {
        try {
            this.out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
