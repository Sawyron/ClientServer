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

    @Override
    public boolean isPointInside(int x, int y) {
        return (x >= getX() && x <= getX() + width) && (y >= getY() && y <= y + height);
    }

    @Override
    public boolean isMovingOutOfAreaWidth(int width) {
        return (getX() + this.width >= width && getDx() > 0) || (getX() <= 0 && getDx() < 0);
    }

    @Override
    public boolean isMovingOutOfAreaHeight(int height) {
        return (getY() + this.height >= height && getDy() > 0) || (getY() <= 0 && getDy() < 0);
    }

}
