package com.poultryfarm.clients;

import com.poultryfarm.server.Server;
import com.transfer.domain.TransferEntity;
import com.transfer.serializers.EntitySerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class EntityServerClient {
    private final Server server;
    private final EntitySerializer responseSerializer;
    private final int entityLimit;
    private final Map<String, EntitySerializer> serializerTypeMap = new HashMap<>();
    private final String serializerType;
    private final List<TransferEntity> entities = new LinkedList<>();

    public EntityServerClient(Server server, EntitySerializer responseSerializer, String serializerType, int entityLimit) {
        this.server = server;
        this.responseSerializer = responseSerializer;
        this.serializerType = serializerType;
        addEntitySerializer(responseSerializer, serializerType);
        this.entityLimit = entityLimit;
    }

    public void start() {
        server.addGetHandler("entities", (parameters) -> {
            String response = "Content-Type: " + serializerType + "\n";
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            synchronized (entities) {
                responseSerializer.saveEntities(entities, out);
            }
            response += out.toString();
            return response;
        });
        server.addGetHandler("count", (parameters) -> Integer.toString(entities.size()));
        server.addGetHandler("getAt", (parameters -> {
            String response = "Content-Type: " + serializerType + "\n";
            int position = Integer.parseInt(parameters.getProperty("index"));
            List<TransferEntity> transferEntities = new LinkedList<>();
            synchronized (entities) {
                if (position > 0 || position < entities.size()) {
                    TransferEntity entity = entities.get(position);
                    transferEntities.add(entity);
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            responseSerializer.saveEntities(transferEntities, out);
            response += out.toString();
            return response;
        }));
        server.addPostHandler("sendEntities", data -> {
            Scanner scanner = new Scanner(data);
            String contentType = scanner.nextLine().split(" ")[1];
            EntitySerializer entitySerializer = serializerTypeMap.get(contentType);
            if (entitySerializer == null) {
                return;
            }
            StringBuilder contentBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                contentBuilder.append(scanner.nextLine()).append("\n");
            }
            String content = contentBuilder.toString();
            ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
            List<TransferEntity> transferEntities = responseSerializer.loadEntities(in);
            synchronized (entities) {
                entities.addAll(transferEntities);
            }
        });
        server.addPostHandler("deleteAt", (data -> {
            int position = Integer.parseInt(data.replaceAll("\n", ""));
            synchronized (entities) {
                entities.remove(position);
            }
        }));
        server.start();
    }

    public void addEntitySerializer(EntitySerializer serializer, String type) {
        serializerTypeMap.put(type, serializer);
    }
}
