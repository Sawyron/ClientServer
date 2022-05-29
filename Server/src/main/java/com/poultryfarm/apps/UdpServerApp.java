package com.poultryfarm.apps;

import com.poultryfarm.clients.EntityServerClient;
import com.poultryfarm.server.Server;
import com.poultryfarm.server.udp.UdpServer;
import com.transfer.serializers.EntitySerializer;
import com.transfer.serializers.TextEntitySerializer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpServerApp {
    public static void main(String[] args) {
        try {
            Server updServer = new UdpServer(new DatagramSocket(8081, InetAddress.getLocalHost()));
            EntitySerializer serializer = new TextEntitySerializer();
            EntityServerClient updClient = new EntityServerClient(updServer, serializer, "text", 1000);
            updClient.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
