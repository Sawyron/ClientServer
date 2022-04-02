package com.app.ui;

import com.app.domain.controllers.EntityController;
import com.app.domain.models.HabitatModel;
import com.app.services.ImageGraphicEntityFactory;
import com.app.ui.habitat.HabitatFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class App {
    public static void main(String[] args) {
        Image birdImage = new ImageIcon(Objects.requireNonNull(App.class.getResource("/bird.png"))).getImage();
        ImageGraphicEntityFactory factory = new ImageGraphicEntityFactory(birdImage, 5000);
        HabitatFrame frame = new HabitatFrame(500, 400);
        HabitatModel model = new HabitatModel();
        EntityController controller = new EntityController(model, frame);
        controller.addAliveEntityType(factory, 500);
        controller.run();
    }
}
