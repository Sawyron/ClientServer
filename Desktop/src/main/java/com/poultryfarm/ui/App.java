package com.poultryfarm.ui;

import com.poultryfarm.clients.Client;
import com.poultryfarm.clients.TcpClient;
import com.poultryfarm.clients.UdpClient;
import com.poultryfarm.controllers.EntityController;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.services.FabricEntitySpawn;
import com.poultryfarm.services.ImageGraphicEntityFactory;
import com.poultryfarm.services.JMessageService;
import com.poultryfarm.services.MessageService;
import com.poultryfarm.services.network.EntityClient;
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

            EntityController controller = new EntityController(model, frame, messageService, entityClient);
            controller.addServerClient(tcpClient, "TCP");
            controller.addServerClient(updClient, "UDP");
            controller.addEntitySpawn(new FabricEntitySpawn("Bird", 10_000, 5_000, birdFactory));
            controller.addEntitySpawn(new FabricEntitySpawn("Nestling", 10_000, 5_000, nestlingFactory));
            controller.addFileEntitySerializer("Binary file (*.bn)", "bn", new BinarySerializer());
            controller.addFileEntitySerializer("Text file (*.txt)", "txt", new TextEntitySerializer());
            controller.addFileEntitySerializer("XML file (*.xml)", "xml", new XmlSerializer());
            controller.addFileEntitySerializer("Object file (*.obj)", "obj", new ObjectSerializer());
            controller.run();
        } catch (Exception e) {
            e.printStackTrace();
            messageService.showMessage(e.getMessage());
        }
    }
}
