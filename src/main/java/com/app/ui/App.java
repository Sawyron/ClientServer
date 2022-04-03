package com.app.ui;

import com.app.controllers.EntityController;
import com.app.domain.HabitatModel;
import com.app.services.ImageGraphicEntityFactory;
import com.app.services.JMessageService;
import com.app.services.MessageService;
import com.app.ui.habitat.HabitatFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class App {
    public static void main(String[] args) {
        MessageService messageService = new JMessageService();
        try {
            Image birdImage = new ImageIcon(Objects.requireNonNull(App.class.getResource("/bird.png"))).getImage();
            Image nestlingImage = new ImageIcon(Objects.requireNonNull(App.class.getResource("/nestling.gif"))).getImage();
            ImageGraphicEntityFactory birdFactory = new ImageGraphicEntityFactory(birdImage, 10_000);
            birdFactory.setImageSize(100, 120);
            ImageGraphicEntityFactory nestlingFactory = new ImageGraphicEntityFactory(nestlingImage, 10_000);
            HabitatFrame frame = new HabitatFrame(500, 400);
            HabitatModel model = new HabitatModel();
            EntityController controller = new EntityController(model, frame);
            controller.addAliveEntityType(birdFactory, 500);
            controller.addAliveEntityType(nestlingFactory, 500);
            controller.run();
        } catch (Exception e) {
            messageService.showMessage(e.getMessage());
        }
    }
}
