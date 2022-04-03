package com.app.ui.habitat;


import com.app.domain.GraphicEntity;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EntityPanel extends JPanel {
    private final Map<String, GraphicEntity> entityIdMap = new HashMap<>();

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (GraphicEntity entity : entityIdMap.values()) {
            entity.paint(g);
        }
    }

    public void addEntity(GraphicEntity entity, String id) {
        entityIdMap.put(id, entity);
    }

    void removeEntity(String id) {
        entityIdMap.remove(id);
    }

    void clear() {
        entityIdMap.clear();
    }

    public void moveEntity() {
        for (GraphicEntity entity : entityIdMap.values()) {
            entity.move();
            if (entity.getX() >= this.getWidth() || entity.getX() <= 0)
            	entity.setDx(-entity.getDx());
            if (entity.getY() >= this.getHeight() || entity.getY() <= 0)
            	entity.setDy(-entity.getDy());
            	
        }
    }
}
