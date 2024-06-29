package ru.otus.june.chat.server;

public interface AuthentificationProvider {
    void initialize();

    boolean authenticate(ClientHandler clientHandler, String login, String password);

    boolean registration(ClientHandler clientHandler, String login, String password, String username);

    boolean checkKickUser(ClientHandler clientHandler, String login);
}