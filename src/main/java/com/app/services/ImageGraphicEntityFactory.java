package com.app.services;

import com.app.domain.models.GraphicEntity;
import com.app.domain.models.ImageGraphicEntity;

import java.awt.*;

public class ImageGraphicEntityFactory implements GraphicEntityFactory {
    private final Image image;

    public ImageGraphicEntityFactory(Image image) {
        this.image = image;
    }

    @Override
    public GraphicEntity createEntity(int x, int y) {
        return new ImageGraphicEntity(x, y, image);
    }

    @Override
    public GraphicEntity createEntity(int x, int y, int dx, int dy) {
        return new ImageGraphicEntity(x, y, dx, dy, image);
    }
}
