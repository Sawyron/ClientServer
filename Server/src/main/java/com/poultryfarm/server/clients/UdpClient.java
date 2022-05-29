package com.poultryfarm.server.clients;

import com.poultryfarm.server.ServerException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient implements Client {
    private final int port;
    private final InetAddress address;
    private final byte[] buffer = new byte[1024];

    public UdpClient(int port, InetAddress address) {
        this.port = port;
        this.address = address;
    }

    @Override
    public void sendData(String data) {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] dataInBytes = data.getBytes();
            DatagramPacket packet = new DatagramPacket(dataInBytes, 0, dataInBytes.length, address, port);
            socket.send(packet);
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public String receiveString(String request) {
        String response = "";
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] requestInBytes = request.getBytes();
            DatagramPacket packet = new DatagramPacket(requestInBytes, 0, requestInBytes.length, address, port);
            socket.send(packet);
            packet = new DatagramPacket(buffer, 0, buffer.length, address, port);
            socket.receive(packet);
            response = new String(packet.getData(), 0, packet.getLength());
        } catch (IOException e) {
            throw new ServerException(e);
        }
        return response;
    }
}
