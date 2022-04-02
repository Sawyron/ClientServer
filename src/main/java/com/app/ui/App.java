package com.app.ui;

import com.app.domain.controllers.GraphicController;
import com.app.services.ImageGraphicEntityFactory;
import com.app.ui.habitat.HabitatFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class App {
    public static void main(String[] args) {
        Image birdImage = new ImageIcon(Objects.requireNonNull(App.class.getResource("/bird.png"))).getImage();
        ImageGraphicEntityFactory factory = new ImageGraphicEntityFactory(birdImage);
        HabitatFrame frame = new HabitatFrame(500, 400);
        GraphicController controller = new GraphicController(frame, factory);
        controller.run();
    }
}
