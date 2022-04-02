package com.app.domain.models;

import java.awt.*;

public abstract class GraphicEntity {
    private int x;
    private int y;
    private int dx;
    private int dy;

    public GraphicEntity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GraphicEntity(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    abstract public void paint(Graphics g);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
}
