package com.poultryfarm.services.entityserializers;

import com.poultryfarm.domain.TransferEntity;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TextEntitySerializer implements EntitySerializer {
    @Override
    public void saveEntities(Collection<TransferEntity> entities, File file) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            fileWriter.write(Integer.toString(entities.size()));
            fileWriter.newLine();
            for (TransferEntity entity : entities) {
                entity.writeToWriter(fileWriter);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TransferEntity> loadEntities(File file) {
        List<TransferEntity> entities = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int size = Integer.parseInt(reader.readLine());
            for (int i = 0; i < size; i++) {
                TransferEntity entity = new TransferEntity();
                entity.readFromReader(reader);
                entities.add(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
}
