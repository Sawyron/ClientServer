package com.poultryfarm.services.entityserializers;

import com.poultryfarm.domain.TransferEntity;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BinarySerializer implements EntitySerializer {
    @Override
    public void saveEntities(Collection<TransferEntity> entities, OutputStream out) {
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        try {
            dataOutputStream.writeInt(entities.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (TransferEntity entity : entities) {
            entity.writeToOutputStream(out);
        }
    }

    @Override
    public List<TransferEntity> loadEntities(InputStream in) {
        List<TransferEntity> entities = new LinkedList<>();
        DataInputStream dataInputStream = new DataInputStream(in);
        int size = 0;
        try {
            size = dataInputStream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < size; i++) {
            TransferEntity entity = new TransferEntity();
            entity.readFromInputStream(in);
            entities.add(entity);
        }
        return entities;
    }
}
