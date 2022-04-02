package com.app.ui.habitat;


import com.app.domain.models.GraphicEntity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        repaint();
    }

    void removeEntity(String id) {
        GraphicEntity removed = entityIdMap.remove(id);
    }

    void clear() {
        entityIdMap.clear();
        repaint();
    }
}
