package com.app.domain;

import java.awt.*;

public class ImageGraphicEntity extends GraphicEntity {
    private final Image image;

    public ImageGraphicEntity(int x, int y, Image image) {
        super(x, y);
        this.image = image;
    }

    public ImageGraphicEntity(int x, int y, int dx, int dy, Image image) {
        super(x, y, dx, dy);
        this.image = image;
    }

    @Override
    public void paint(Graphics g) {
        double scale = 0.15;
        int width = (int) (g.getClipBounds().width * scale);
        int height = (int) (g.getClipBounds().height * scale);
        g.drawImage(image, super.getX(), super.getY(), width, height, null);
    }
}
