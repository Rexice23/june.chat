package ru.otus.june.chat.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private int port;
    private Map<String, ClientHandler> clients;
    private AuthenticationProvider authenticationProvider;

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public Server(int port) {
        this.port = port;
        this.clients = new HashMap<>();
        this.authenticationProvider = new InMemoryAuthenticationProvider(this);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);
            authenticationProvider.initialize();
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(this, socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        broadcastMessage("В чат зашел: " + clientHandler.getUsername());
        clients.put(clientHandler.getUsername(), clientHandler);
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler.getUsername());
        broadcastMessage("Из чата вышел: " + clientHandler.getUsername());
    }

    public synchronized void broadcastMessage(String message) {
        for (ClientHandler c : clients.values()) {
            c.sendMessage(message);
        }
    }

    public synchronized void sendPrivateMessage(String targetUsername, String content) {
        ClientHandler clientHandler = clients.get(targetUsername);
        if (clientHandler != null) {
            clientHandler.sendMessage(content);
        } else {
            System.out.println("Пользователь " + targetUsername + " не найден.");
        }
    }

    public boolean isUsernameBusy(String username) {
        for (ClientHandler c : clients.values()) {
            if (c.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}