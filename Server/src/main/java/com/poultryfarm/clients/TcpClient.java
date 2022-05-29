package com.poultryfarm.clients;

import com.poultryfarm.server.ServerException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient implements Client {
    private final int port;
    private final InetAddress address;

    public TcpClient(int port, InetAddress address) {
        this.port = port;
        this.address = address;
    }

    @Override
    public void sendData(String data) {
        try (Socket socket = new Socket(address, port)) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public String receiveString(String request) {
        String response = "";
        try (Socket socket = new Socket(address, port)) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(request);
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line).append("\n");
            }
            response = responseBuilder.toString();
        } catch (IOException e) {
            throw new ServerException(e);
        }
        return response;
    }
}
