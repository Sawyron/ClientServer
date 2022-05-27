package com.poultryfarm.server.tcp;

import com.poultryfarm.server.GetHandler;
import com.poultryfarm.server.PostHandler;
import com.poultryfarm.server.ServerException;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketHandler implements Runnable {
    private final Socket client;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final Map<String, GetHandler> getHandlers = new HashMap<>();
    private final Map<String, PostHandler> postHandlers = new HashMap<>();

    public SocketHandler(Socket client) {
        this.client = client;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public void run() {
        try {
            String request = bufferedReader.readLine();
            String[] requestArgs = request.split("/");
            String method = requestArgs[0];
            String handlerName = requestArgs[1];
            String line;
            StringBuilder dataBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                dataBuilder.append(line).append("\n");
            }
            if (method.equalsIgnoreCase("GET")) {
                handleGetRequest(handlerName);
            }
            if (method.equalsIgnoreCase("POST")) {
                handlePostRequest(handlerName, dataBuilder.toString());
            }
            client.close();
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    public void addGetHandler(String name, GetHandler handler) {
        getHandlers.put(name, handler);
    }

    public void addPostHandler(String name, PostHandler handler) {
        postHandlers.put(name, handler);
    }

    private void handleGetRequest(String handlerName) throws IOException {
        GetHandler handler = getHandlers.get(handlerName);
        if (handler == null) {
            return;
        }
        bufferedWriter.write(handler.handle());
        bufferedWriter.flush();
    }

    private void handlePostRequest(String handlerName, String data) throws IOException {
        PostHandler handler = postHandlers.get(handlerName);
        if (handler == null) {
            return;
        }
        handler.handle(data);
    }
}
