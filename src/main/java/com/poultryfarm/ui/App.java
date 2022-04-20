package com.poultryfarm.ui;

import com.poultryfarm.controllers.EntityController;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.services.FabricEntitySpawn;
import com.poultryfarm.services.ImageGraphicEntityFactory;
import com.poultryfarm.services.JMessageService;
import com.poultryfarm.services.MessageService;
import com.poultryfarm.ui.habitat.HabitatFrame;

import javax.swing.*;
import java.awt.*;
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
            HabitatFrame frame = new HabitatFrame(500, 400);
            HabitatModel model = new HabitatModel();
            EntityController controller = new EntityController(model, frame);
            controller.addEntitySpawn(new FabricEntitySpawn("Bird", 10_000, 5_000, birdFactory));
            controller.addEntitySpawn(new FabricEntitySpawn("Nestling", 10_000, 5_000, nestlingFactory));
            controller.run();
        } catch (Exception e) {
            e.printStackTrace();
            messageService.showMessage(e.getMessage());
        }
    }
}
