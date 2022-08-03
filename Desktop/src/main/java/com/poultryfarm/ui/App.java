package com.poultryfarm.ui;

import com.poultryfarm.clients.Client;
import com.poultryfarm.clients.TcpClient;
import com.poultryfarm.clients.UdpClient;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.services.*;
import com.poultryfarm.services.async.TaskExecutor;
import com.poultryfarm.services.network.EntityClient;
import com.poultryfarm.ui.controllers.*;
import com.poultryfarm.ui.habitat.HabitatFrame;
import com.transfer.serializers.BinarySerializer;
import com.transfer.serializers.ObjectSerializer;
import com.transfer.serializers.TextEntitySerializer;
import com.transfer.serializers.XmlSerializer;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.Objects;

public class App {
    public static void main(String[] args) {
        MessageService messageService = new JMessageService();
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

            Image birdImage = new ImageIcon(Objects.requireNonNull(App.class.getResource("/bird.png"))).getImage();
            Image nestlingImage = new ImageIcon(Objects.requireNonNull(App.class.getResource("/nestling.gif"))).getImage();
            ImageGraphicEntityFactory birdFactory = new ImageGraphicEntityFactory(birdImage);
            birdFactory.setImageSize(100, 120);
            ImageGraphicEntityFactory nestlingFactory = new ImageGraphicEntityFactory(nestlingImage);

            HabitatFrame frame = new HabitatFrame(600, 500);
            HabitatModel model = new HabitatModel();
            Client tcpClient = new TcpClient(8080, InetAddress.getLocalHost());
            Client updClient = new UdpClient(8081, InetAddress.getLocalHost());
            EntityClient entityClient = new EntityClient(tcpClient, new TextEntitySerializer(), "text");

            ExceptionHandler handler = (e) -> {
                messageService.showError(e.getMessage());
            };
            TaskExecutor executor = new TaskExecutor(handler);

            EntityHabitatController habitatController = new EntityHabitatControllerImpl(frame, model);
            EntitySpawnController spawnController = new EntitySpawnControllerImpl(frame, model, handler);
            EntityViewController viewController = new EntityViewControllerImpl(frame, model, habitatController, spawnController, executor);
            TransferEntityController transferController = new TransferEntityControllerImpl(frame, model, spawnController, executor);
            FileTransferEntityController fileController = new FileTransferEntityControllerImpl(frame, transferController, messageService);
            NetworkEntityController networkController = new NetworkEntityControllerImpl(frame, entityClient, transferController, executor, messageService, handler);
            MainController mainController = new MainController(frame, viewController, habitatController, fileController, networkController);

            networkController.addServerClient(tcpClient, "TCP");
            networkController.addServerClient(updClient, "UDP");
            spawnController.addEntitySpawn(new FabricEntitySpawn("Bird", 10_000, 5_000, birdFactory));
            spawnController.addEntitySpawn(new FabricEntitySpawn("Nestling", 10_000, 5_000, nestlingFactory));
            fileController.addFileEntitySerializer("Binary file (*.bn)", "bn", new BinarySerializer());
            fileController.addFileEntitySerializer("Text file (*.txt)", "txt", new TextEntitySerializer());
            fileController.addFileEntitySerializer("XML file (*.xml)", "xml", new XmlSerializer());
            fileController.addFileEntitySerializer("Object file (*.obj)", "obj", new ObjectSerializer());

            mainController.run();
        } catch (Exception e) {
            e.printStackTrace();
            messageService.showMessage(e.getMessage());
        }
    }
}
