package com.poultryfarm.server.tcp;

import com.poultryfarm.server.GetHandler;
import com.poultryfarm.server.PostHandler;
import com.poultryfarm.server.ServerException;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
            String[] requestArgs = request.split("\\?")[0].split("/");
            String method = requestArgs[0];
            String handlerName = requestArgs[1];
            if (method.equalsIgnoreCase("GET")) {
                Properties properties = new Properties();
                if (request.contains("?")) {
                    String getParameters = request.split("\\?")[1];
                    for (String pair : getParameters.split("&")) {
                        String[] keyValue = pair.split("=");
                        properties.setProperty(keyValue[0], keyValue[1]);
                    }
                }
                handleGetRequest(handlerName, properties);
            }
            if (method.equalsIgnoreCase("POST")) {
                String line;
                StringBuilder dataBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    dataBuilder.append(line).append("\n");
                }
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

    private void handleGetRequest(String handlerName, Properties parameters) throws IOException {
        GetHandler handler = getHandlers.get(handlerName);
        if (handler == null) {
            return;
        }
        bufferedWriter.write(handler.handle(parameters));
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
