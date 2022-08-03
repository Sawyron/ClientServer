package com.poultryfarm.controllers;

import com.transfer.serializers.EntitySerializer;

import java.io.File;

public interface FileTransferEntityController {
    void loadEntities(File file);

    void saveEntities(File file);

    void addFileEntitySerializer(String description, String extension, EntitySerializer serializer);
}
