package com.poultryfarm.domain;

import java.awt.*;

/**
 * Abstract class representing an entity
 * that can be rendered on AWT graphics object
 */
public abstract class GraphicEntity {
    private int x;
    private int y;
    private int dx;
    private int dy;

    /**
     * @param x x coordinate
     * @param y y coordinate
     */
    public GraphicEntity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param dx x-asis speed
     * @param dy y-asis speed
     */
    public GraphicEntity(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Moves entity
     */
    public void move() {
        x += dx;
        y += dy;
    }

    /**
     * Paints entity on graphics object
     * @param g AWT graphics object
     */
    abstract public void paint(Graphics g);

    /**
     * Checks whether the provided point
     * is inside the entity
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     * @return Whether the point is inside the entity
     */
    abstract public boolean isPointInside(int x, int y);

    /**
     * Checks whether the entity is moving
     * out of provided area's by width
     * @param width Area's width
     * @return Whether the entity is moving
     * out of provided area's by width
     */
    abstract public boolean isMovingOutOfAreaWidth(int width);

    /**
     * Checks whether the entity is moving
     * out of provided area's by height
     * @param height Area's height
     * @return Whether the entity is moving
     * out of provided area's by height
     */
    abstract public boolean isMovingOutOfAreaHeight(int height);

    /**
     * Returns x coordinate of the entity
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets x coordinate of the entity
     * @param x x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns y coordinate of the entity
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets y coordinate of the entity
     * @param y y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns x-asis speed of the entity
     * @return x-asis speed
     */
    public int getDx() {
        return dx;
    }

    /**
     * Sets x-asis speed of the entity
     * @param dx x-asis speed
     */
    public void setDx(int dx) {
        this.dx = dx;
    }

    /**
     * Returns y-asis speed of the entity
     * @return y-asis speed
     */
    public int getDy() {
        return dy;
    }

    /**
     * Sets y-asis speed of the entity
     * @param dy y-asis speed
     */
    public void setDy(int dy) {
        this.dy = dy;
    }
}
