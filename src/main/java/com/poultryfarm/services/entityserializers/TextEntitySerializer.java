package com.poultryfarm.services.entityserializers;

import com.poultryfarm.domain.TransferEntity;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TextEntitySerializer implements EntitySerializer {
    @Override
    public void saveEntities(Collection<TransferEntity> entities, OutputStream out) {
        Writer writer = new BufferedWriter(new OutputStreamWriter(out));
        try {
            writer.write(Integer.toString(entities.size()));
            writer.write('\n');
            for (TransferEntity entity : entities) {
                entity.writeToWriter(writer);
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TransferEntity> loadEntities(InputStream in) {
        List<TransferEntity> entities = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        int size = 0;
        try {
            size = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < size; i++) {
            TransferEntity entity = new TransferEntity();
            entity.readFromReader(reader);
            entities.add(entity);
        }
        return entities;
    }
}
