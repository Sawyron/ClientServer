package com.poultryfarm.apps;

import com.poultryfarm.clients.EntityServerClient;
import com.poultryfarm.server.Server;
import com.poultryfarm.server.tcp.TcpServer;
import com.transfer.serializers.EntitySerializer;
import com.transfer.serializers.TextEntitySerializer;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpServerApp {


    public static void main(String[] args) {
        try {
            Server tcpServer = new TcpServer(new ServerSocket(8080));
            EntitySerializer serializer = new TextEntitySerializer();
            EntityServerClient tcpClient = new EntityServerClient(tcpServer, serializer, "text", 1000);
            tcpClient.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
