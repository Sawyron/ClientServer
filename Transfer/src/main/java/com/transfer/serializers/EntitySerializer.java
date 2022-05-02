package com.transfer.serializers;

import com.transfer.domain.TransferEntity;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

public interface EntitySerializer {
    void saveEntities(Collection<TransferEntity> entities, OutputStream out);

    List<TransferEntity> loadEntities(InputStream in);
}
