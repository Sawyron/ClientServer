package com.poultryfarm.ui;

import com.poultryfarm.controllers.EntityController;
import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.domain.HabitatModel;
import com.poultryfarm.domain.ImageGraphicEntity;
import com.poultryfarm.services.ImageGraphicEntityFactory;
import com.poultryfarm.services.JMessageService;
import com.poultryfarm.services.MessageService;
import com.poultryfarm.ui.habitat.HabitatFrame;
import com.thoughtworks.xstream.XStream;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
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
            controller.addAliveEntityType(birdFactory, 5_000);
            controller.addAliveEntityType(nestlingFactory, 2_000);
            controller.run();
        } catch (Exception e) {
            e.printStackTrace();
            messageService.showMessage(e.getMessage());
        }
    }
}
