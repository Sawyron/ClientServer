package com.app.services;

import com.app.domain.GraphicEntity;
import com.app.domain.ImageGraphicEntity;

import java.awt.*;

public class ImageGraphicEntityFactory implements GraphicEntityFactory {
    private final Image image;
    private final long lifeTimeInMs;

    public ImageGraphicEntityFactory(Image image, long lifeTimeInMs) {
        this.image = image;
        this.lifeTimeInMs = lifeTimeInMs;
    }

    @Override
    public GraphicEntity createEntity(int x, int y) {
        return new ImageGraphicEntity(x, y, image);
    }

    @Override
    public GraphicEntity createEntity(int x, int y, int dx, int dy) {
        return new ImageGraphicEntity(x, y, dx, dy, image);
    }

    @Override
    public long getEntityLifeTimeInMs() {
        return lifeTimeInMs;
    }
}
