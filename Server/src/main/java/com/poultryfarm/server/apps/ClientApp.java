package com.poultryfarm.server.apps;

import com.transfer.domain.TransferEntity;
import com.transfer.serializers.EntitySerializer;
import com.transfer.serializers.TextEntitySerializer;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClientApp {
    public static void main(String[] args) {
        //startTcpClient();
        startUdpClient();
    }

    private static void startTcpClient() {
        List<TransferEntity> entities = Arrays.asList(
                new TransferEntity(10, 20, 30, 40, "Cat"),
                new TransferEntity(15, 25, 35, 45, "Dog"),
                new TransferEntity(20, 30, 40, 50, "Bird")
        );
        EntitySerializer serializer = new TextEntitySerializer();
        try (Socket client = new Socket("localhost", 8080)) {
            String message = "POST/birds\n";
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            serializer.saveEntities(entities, byteArrayOutputStream);
            message += byteArrayOutputStream.toString();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startUdpClient() {
        List<TransferEntity> entities = Arrays.asList(
                new TransferEntity(10, 20, 30, 40, "Cat"),
                new TransferEntity(15, 25, 35, 45, "Dog"),
                new TransferEntity(20, 30, 40, 50, "Bird")
        );
        EntitySerializer serializer = new TextEntitySerializer();
        String message = "POST/birds\n";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
        try {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serializer.saveEntities(entities, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        System.out.println(new String(data));
        try (DatagramSocket socket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(data, 0, data.length, InetAddress.getByName("localhost"), 8080);
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
