package ru.geekbrains.july_chat.chat_server.auth;

import ru.geekbrains.july_chat.chat_server.error.UserNotFoundException;
import ru.geekbrains.july_chat.chat_server.error.WrongCredentialException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InMemoryAuthService implements AuthService {
    private List<User> users;

    public InMemoryAuthService() {
        this.users = new ArrayList<>(
                Arrays.asList(
                        new User("log1", "pass", "nick1"),
                        new User("log2", "pass", "nick2"),
                        new User("log3", "pass", "nick3"),
                        new User("log4", "pass", "nick4")
                )
        );
    }

    @Override
    public void start() {
        System.out.println("Auth service started!");
    }

    @Override
    public void stop() {
        System.out.println("Auth service stopped");
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (User user : users) {
            if (login.equals(user.getLogin())) {
                if (password.equals(user.getPassword())) return user.getNickname();
                else throw new WrongCredentialException("");
            }
        }
        throw new UserNotFoundException("User not found!");
    }

    @Override
    public String changeNickname(String oldNick, String newNick) {
        return null;
    }

    @Override
    public void changePassword(String nickname, String oldPassword, String newPassword) {

    }

    @Override
    public void createNewUser(String login, String password, String nickname) {

    }

    @Override
    public void deleteUser(String nickname) {

    }
}
