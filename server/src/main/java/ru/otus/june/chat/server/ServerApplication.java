package ru.otus.june.chat.server;

import java.net.ServerSocket;

public class ServerApplication {
    public static void main(String[] args) {
        int port = 8189;
        try (ServerSocket serverSocket = new ServerSocket(port)) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
