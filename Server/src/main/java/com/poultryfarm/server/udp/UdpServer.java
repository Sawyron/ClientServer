package com.poultryfarm.server.udp;

import com.poultryfarm.server.GetHandler;
import com.poultryfarm.server.PostHandler;
import com.poultryfarm.server.Server;
import com.poultryfarm.server.ServerException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UdpServer implements Server {
    private final DatagramSocket socket;
    private final byte[] buff = new byte[1024];
    private final Map<String, GetHandler> getHandlers = new HashMap<>();
    private final Map<String, PostHandler> postHandlers = new HashMap<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public UdpServer(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void start() {
        while (!socket.isClosed()) {
            DatagramPacket packet = new DatagramPacket(buff, 0, buff.length);
            try {
                socket.receive(packet);
                String request = new String(packet.getData());
                executorService.submit(() -> {
                    String[] requestLines = request.split("\n");
                    String[] requestArgs = requestLines[0].split("\\?")[0].split("/");
                    String method = requestArgs[0];
                    if (method.equalsIgnoreCase("GET")) {
                        Properties properties = new Properties();
                        if (request.contains("?")) {
                            String getParameters = request.split("\\?")[1];
                            for (String pair : getParameters.split("&")) {
                                String[] keyValue = pair.split("=");
                                properties.setProperty(keyValue[0], keyValue[1]);
                            }
                        }
                        handleGet(requestArgs[1], packet.getPort(), packet.getAddress(), properties);
                    }
                    if (method.equalsIgnoreCase("POST")) {
                        StringBuilder dataBuilder = new StringBuilder();
                        for (int i = 1; i < requestLines.length; i++) {
                            dataBuilder.append(requestLines[i]).append("\n");
                        }
                        handlePost(requestArgs[1], dataBuilder.toString());
                    }
                });
            } catch (IOException e) {
                throw new ServerException(e);
            }
        }
    }

    @Override
    public void close() {
        socket.close();
        executorService.shutdown();
    }

    @Override
    public void addGetHandler(String handlerName, GetHandler handler) {
        getHandlers.put(handlerName, handler);
    }

    @Override
    public void addPostHandler(String handlerName, PostHandler handler) {
        postHandlers.put(handlerName, handler);
    }

    private void handleGet(String handlerName, int port, InetAddress address, Properties properties) {
        GetHandler handler = getHandlers.get(handlerName);
        if (handler == null) {
            return;
        }
        byte[] data = handler.handle(properties).getBytes();
        DatagramPacket packet = new DatagramPacket(data, 0, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    private void handlePost(String handlerName, String data) {
        PostHandler handler = postHandlers.get(handlerName);
        if (handler == null) {
            return;
        }
        handler.handle(data);
    }
}
