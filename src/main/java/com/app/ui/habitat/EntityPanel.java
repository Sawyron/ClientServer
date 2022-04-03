package com.app.ui.habitat;


import com.app.domain.GraphicEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EntityPanel extends JPanel {
    private final Map<String, GraphicEntity> entityIdMap = new HashMap<>();
    private final List<EntityClickedListener> listeners = new LinkedList<>();

    public EntityPanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String id = null;
                for (Map.Entry<String, GraphicEntity> pair : entityIdMap.entrySet()) {
                    if (pair.getValue().isPointInside(e.getX(), e.getY())) {
                        id = pair.getKey();
                        break;
                    }
                }
                if (id != null)
                    invokeListeners(id);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        synchronized (this) {
            for (GraphicEntity entity : entityIdMap.values()) {
                entity.paint(g);
            }
        }
    }

    public synchronized void moveEntities() {
        for (GraphicEntity entity : entityIdMap.values()) {
            entity.move();
            if (entity.isMovingOutOfAreaWidth(this.getWidth()))
                entity.setDx(-entity.getDx());
            if (entity.isMovingOutOfAreaHeight(this.getHeight()))
                entity.setDy(-entity.getDy());
        }
        repaint();
    }

    public synchronized void addEntity(GraphicEntity entity, String id) {
        entityIdMap.put(id, entity);
        repaint();
    }

    public synchronized void removeEntity(String id) {
        entityIdMap.remove(id);
        repaint();
    }

    public synchronized void clear() {
        entityIdMap.clear();
    }

    public synchronized void addEntityClickedListener(EntityClickedListener l) {
        listeners.add(l);
    }

    public synchronized boolean removeEntityClickedListener(EntityClickedListener l) {
        return listeners.remove(l);
    }

    private synchronized void invokeListeners(String id) {
        for (EntityClickedListener listener : listeners) {
            listener.EntityClicked(id);
        }
    }
}
