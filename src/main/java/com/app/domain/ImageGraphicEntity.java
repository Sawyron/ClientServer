package com.app.domain;

import java.awt.*;

public class ImageGraphicEntity extends GraphicEntity {
    private final Image image;
    private final int width;
    private final int height;

    public ImageGraphicEntity(int x, int y, int width, int height, Image image) {
        super(x, y);
        this.height = height;
        this.width = width;
        this.image = image;
    }

    public ImageGraphicEntity(int x, int y, int dx, int dy, int width, int height, Image image) {
        super(x, y, dx, dy);
        this.width = width;
        this.height = height;
        this.image = image;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, super.getX(), super.getY(), width, height, null);
    }
}
