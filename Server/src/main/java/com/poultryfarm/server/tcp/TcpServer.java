package com.poultryfarm.server.tcp;

import com.poultryfarm.server.GetHandler;
import com.poultryfarm.server.PostHandler;
import com.poultryfarm.server.Server;
import com.poultryfarm.server.ServerException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer implements Server {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ServerSocket serverSocket;
    private final Map<String, GetHandler> getHandlers = new HashMap<>();
    private final Map<String, PostHandler> postHandlers = new HashMap<>();

    public TcpServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void start() {
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                SocketHandler socketHandler = new SocketHandler(socket);
                for (Map.Entry<String, GetHandler> entry : getHandlers.entrySet()) {
                    socketHandler.addGetHandler(entry.getKey(), entry.getValue());
                }
                for (Map.Entry<String, PostHandler> entry : postHandlers.entrySet()) {
                    socketHandler.addPostHandler(entry.getKey(), entry.getValue());
                }
                executorService.submit(socketHandler);
            } catch (IOException e) {
                close();
                throw new ServerException(e);
            }
        }
        close();
    }

    @Override
    public void close() {
        try {
            serverSocket.close();
            executorService.shutdown();
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public void addGetHandler(String handlerName, GetHandler handler) {
        getHandlers.put(handlerName, handler);
    }

    @Override
    public void addPostHandler(String handlerName, PostHandler handler) {
        postHandlers.put(handlerName, handler);
    }
}
