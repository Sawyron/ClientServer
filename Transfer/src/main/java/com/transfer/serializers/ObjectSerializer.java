package com.transfer.serializers;


import com.transfer.domain.TransferEntity;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ObjectSerializer implements EntitySerializer {
    @Override
    public void saveEntities(Collection<TransferEntity> entities, OutputStream out) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            List<TransferEntity> transferEntities = new LinkedList<>(entities);
            objectOutputStream.writeObject(transferEntities);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TransferEntity> loadEntities(InputStream in) {
        List<TransferEntity> entities = new LinkedList<>();
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            Collection<TransferEntity> transferEntities = (Collection<TransferEntity>) objectInputStream.readObject();
            if (transferEntities != null) {
                entities.addAll(transferEntities);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
}
