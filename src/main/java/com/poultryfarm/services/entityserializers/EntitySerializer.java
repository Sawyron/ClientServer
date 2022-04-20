package com.poultryfarm.services.entityserializers;

import com.poultryfarm.domain.TransferEntity;

import java.io.File;
import java.util.Collection;
import java.util.List;

public interface EntitySerializer {
    void saveEntities(Collection<TransferEntity> entities, File file);

    List<TransferEntity> loadEntities(File file);
}
