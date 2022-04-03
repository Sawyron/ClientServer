package com.app.services;

import com.app.domain.GraphicEntity;
import com.app.domain.ImageGraphicEntity;

import java.awt.*;

public class ImageGraphicEntityFactory implements GraphicEntityFactory {
    private final Image image;
    private final long lifeTimeInMs;
    private int height = 50;
    private int width = 50;

    public ImageGraphicEntityFactory(Image image, long lifeTimeInMs) {
        this.image = image;
        this.lifeTimeInMs = lifeTimeInMs;
    }

    @Override
    public GraphicEntity createEntity(int x, int y) {
        return new ImageGraphicEntity(x, y, width, height, image);
    }

    @Override
    public GraphicEntity createEntity(int x, int y, int dx, int dy) {
        return new ImageGraphicEntity(x, y, dx, dy, width, height, image);
    }

    @Override
    public long getEntityLifeTimeInMs() {
        return lifeTimeInMs;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
