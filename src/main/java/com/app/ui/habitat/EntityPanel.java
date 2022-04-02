package com.app.ui.habitat;


import com.app.domain.models.GraphicEntity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityPanel extends JPanel {
    private final List<GraphicEntity> entityList = new ArrayList<>();

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (GraphicEntity entity : entityList) {
            entity.paint(g);
        }
    }

    public void addEntity(GraphicEntity entity) {
        entityList.add(entity);
        repaint();
    }
}
