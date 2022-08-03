package com.poultryfarm.ui.controllers;

import com.poultryfarm.services.MessageService;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;
import com.transfer.domain.TransferEntity;
import com.transfer.serializers.EntitySerializer;
import com.transfer.serializers.ExtensionFileEntitySerializer;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileTransferEntityControllerImpl implements FileTransferEntityController {

    private final GraphicEntityView view;
    private final TransferEntityController transferController;
    private final List<ExtensionFileEntitySerializer> fIleEntitySerializers = new LinkedList<>();
    private final MessageService messageService;

    public FileTransferEntityControllerImpl(GraphicEntityView view, TransferEntityController transferController, MessageService messageService) {
        this.view = view;
        this.transferController = transferController;
        this.messageService = messageService;
    }

    @Override
    public void loadEntities(File file) {
        ExtensionFileEntitySerializer serializer = fIleEntitySerializers.stream()
                .filter(s -> file.getName().endsWith(s.getExtension()))
                .findFirst()
                .orElse(null);
        if (serializer == null) {
            messageService.showMessage("No serializer for this extension");
            return;
        }
        List<TransferEntity> transferEntities = serializer.loadEntities(file);
        transferController.addTransferEntities(transferEntities);
    }

    @Override
    public void saveEntities(File file) {
        ExtensionFileEntitySerializer serializer = fIleEntitySerializers.stream()
                .filter(s -> file.getName().endsWith(s.getExtension()))
                .findFirst()
                .orElse(null);
        if (serializer == null) {
            messageService.showExclamation("No serializer for this extension");
            return;
        }
        List<TransferEntity> transferEntities = transferController.getTransferEntities();
        serializer.saveEntities(transferEntities, file);
    }

    @Override
    public void addFileEntitySerializer(String description, String extension, EntitySerializer serializer) {
        view.addFileFilter(new FileNameExtensionFilter(description, extension), extension);
        fIleEntitySerializers.add(new ExtensionFileEntitySerializer(serializer, description, extension));
    }
}
