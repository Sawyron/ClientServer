package com.poultryfarm.services;

import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.domain.ImageGraphicEntity;

import java.awt.*;

public class ImageGraphicEntityFactory implements GraphicEntityFactory {
    private final Image image;
    private int height = 50;
    private int width = 50;

    public ImageGraphicEntityFactory(Image image) {
        this.image = image;
    }

    @Override
    public GraphicEntity createEntity(int x, int y) {
        return new ImageGraphicEntity(x, y, width, height, image);
    }

    @Override
    public GraphicEntity createEntity(int x, int y, int dx, int dy) {
        return new ImageGraphicEntity(x, y, dx, dy, width, height, image);
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
