package com.poultryfarm.services.network;

import com.poultryfarm.clients.Client;
import com.transfer.domain.TransferEntity;
import com.transfer.serializers.EntitySerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class EntityClient {
    private Client client;
    private EntitySerializer requestSerializer;
    private String serializerType;
    private final Map<String, EntitySerializer> entitySerializerMap = new HashMap<>();

    public EntityClient(Client client, EntitySerializer requestSerializer, String serializerType) {
        this.client = client;
        this.requestSerializer = requestSerializer;
        this.serializerType = serializerType;
        addSerializer(requestSerializer, serializerType);
    }

    public void sendEntities(Collection<TransferEntity> entities) {
        String request = "POST/sendEntities\n";
        request += "Content-type: " + serializerType + "\n";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        requestSerializer.saveEntities(entities, out);
        request += out.toString();
        client.sendData(request);
    }

    public List<TransferEntity> receiveEntities() {
        List<TransferEntity> entities = new LinkedList<>();
        String request = "GET/entities\n";
        String response = client.receiveString(request);
        Scanner scanner = new Scanner(response);
        String contentType = scanner.nextLine().split(" ")[1];
        EntitySerializer serializer = entitySerializerMap.get(contentType);
        if (serializer == null) {
            return entities;
        }
        StringBuilder dataBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            dataBuilder.append(scanner.nextLine()).append("\n");
        }
        ByteArrayInputStream in = new ByteArrayInputStream(dataBuilder.toString().getBytes());
        entities.addAll(serializer.loadEntities(in));
        return entities;
    }

    public int getEntitiesCount() {
        int count = 0;
        String request = "GET/count\n";
        String response = client.receiveString(request).replaceAll("\n", "");
        count = Integer.parseInt(response);
        return count;
    }

    public TransferEntity getEntityAt(int index) {
        TransferEntity entity = new TransferEntity();
        entity.setType("null");
        String request = "GET/getAt?index=" + index + "\n";
        String response = client.receiveString(request);
        Scanner scanner = new Scanner(response);
        String contentType = scanner.nextLine().split(" ")[1];
        EntitySerializer serializer = entitySerializerMap.get(contentType);
        if (serializer == null) {
            return entity;
        }
        StringBuilder dataBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            dataBuilder.append(scanner.nextLine()).append("\n");
        }
        ByteArrayInputStream in = new ByteArrayInputStream(dataBuilder.toString().getBytes());
        List<TransferEntity> transferEntities = serializer.loadEntities(in);
        if (transferEntities.size() > 0) {
            entity = transferEntities.get(0);
        }
        return entity;
    }

    public void removeAt(int index) {
        String request = "POST/deleteAt\n";
        request += index;
        client.sendData(request);
    }

    public void addSerializer(EntitySerializer serializer, String name) {
        entitySerializerMap.put(name, serializer);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setRequestSerializer(EntitySerializer requestSerializer, String serializerType) {
        this.requestSerializer = requestSerializer;
        this.serializerType = serializerType;
    }
}
