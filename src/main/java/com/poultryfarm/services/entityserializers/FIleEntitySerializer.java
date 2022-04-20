package com.poultryfarm.services.entityserializers;

import com.poultryfarm.domain.TransferEntity;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class FIleEntitySerializer implements EntitySerializer {
    private final EntitySerializer serializer;
    private final String description;
    private final String extension;


    public FIleEntitySerializer(EntitySerializer serializer, String description, String extension) {
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
        serializer.saveEntities(entities, file);
    }

    @Override
    public List<TransferEntity> loadEntities(File file) {
        return serializer.loadEntities(file);
    }
}
