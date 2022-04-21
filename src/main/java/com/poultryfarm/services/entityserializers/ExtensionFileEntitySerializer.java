package com.poultryfarm.services.entityserializers;

import com.poultryfarm.domain.TransferEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ExtensionFileEntitySerializer implements FileEntitySerializer {
    private final EntitySerializer serializer;
    private final String description;
    private final String extension;


    public ExtensionFileEntitySerializer(EntitySerializer serializer, String description, String extension) {
        this.description = description;
        this.extension = extension;
        this.serializer = serializer;
    }

    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public void saveEntities(Collection<TransferEntity> entities, File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            serializer.saveEntities(entities, fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TransferEntity> loadEntities(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return serializer.loadEntities(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
