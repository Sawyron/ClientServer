package com.poultryfarm.services.entityserializers;

import com.poultryfarm.domain.TransferEntity;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BinarySerializer implements EntitySerializer {
    @Override
    public void saveEntities(Collection<TransferEntity> entities, File file) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            out.writeInt(entities.size());
            for (TransferEntity entity : entities) {
                entity.writeToOutputStream(out);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TransferEntity> loadEntities(File file) {
        List<TransferEntity> entities = new LinkedList<>();
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                TransferEntity entity = new TransferEntity();
                entity.readFromInputStream(in);
                entities.add(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
}
