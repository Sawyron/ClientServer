package com.transfer.serializers;

import com.transfer.domain.TransferEntity;

import java.io.File;
import java.util.Collection;
import java.util.List;

public interface FileEntitySerializer {
    void saveEntities(Collection<TransferEntity> entities, File file);

    List<TransferEntity> loadEntities(File file);
}
