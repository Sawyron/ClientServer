package com.app.domain.controllers;

import com.app.services.GraphicEntityFactory;
import com.app.ui.graphicentity.GraphicEntityView;

import java.util.Random;

public class GraphicController {
    private final GraphicEntityView view;
    private final GraphicEntityFactory factory;

    public GraphicController(GraphicEntityView view, GraphicEntityFactory factory) {
        this.view = view;
        this.factory = factory;
        this.view.addStartActionListener((e) -> {
            Random random = new Random();
            this.view.addEntity(this.factory.createEntity(
                    random.nextInt(this.view.getWidth()),
                    random.nextInt(this.view.getHeight()))
            );
        });
    }

    public void run() {
        view.run();
    }
}
