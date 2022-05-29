package com.poultryfarm.server.apps;

import com.poultryfarm.server.Server;
import com.poultryfarm.server.tcp.TcpServer;
import com.transfer.domain.TransferEntity;
import com.transfer.serializers.EntitySerializer;
import com.transfer.serializers.TextEntitySerializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

public class ServerApp {
    public static void main(String[] args) {
        try {
            Server server = new TcpServer(new ServerSocket(8080));
            //Server server = new UdpServer(new DatagramSocket(8080));
            server.addPostHandler("birds", (data) -> {
                ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes());
                EntitySerializer serializer = new TextEntitySerializer();
                List<TransferEntity> postEntities = serializer.loadEntities(in);
                for (TransferEntity postEntity : postEntities) {
                    System.out.println(postEntity);
                }
                //server.close();
            });
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
