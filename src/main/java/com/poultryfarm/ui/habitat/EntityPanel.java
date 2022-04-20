package com.poultryfarm.ui.habitat;


import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.ui.graphicentity.AreaPointClickedListener;
import com.poultryfarm.ui.graphicentity.EntityClickedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

public class EntityPanel extends JPanel {
    private final Map<String, GraphicEntity> entityIdMap = new HashMap<>();
    private final List<EntityClickedListener> entityRightListeners = new LinkedList<>();
    private final List<EntityClickedListener> entityLeftListeners = new LinkedList<>();
    private final List<AreaPointClickedListener> areaPointLeftClickedListeners = new LinkedList<>();
    private final List<AreaPointClickedListener> areaPointRightClickedListeners = new LinkedList<>();

    public EntityPanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String id = null;
                synchronized (EntityPanel.this) {
                    for (Map.Entry<String, GraphicEntity> pair : entityIdMap.entrySet()) {
                        if (pair.getValue().isPointInside(e.getX(), e.getY())) {
                            id = pair.getKey();
                            break;
                        }
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (id != null) {
                        invokeEntityRightListeners(id);
                    } else {
                        invokeAreaPointRightClickListeners(e.getX(), e.getY());
                    }
                }
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (id != null) {
                        invokeEntityLeftClickListeners(id);
                    } else {
                        invokeAreaPointLeftListeners(e.getX(), e.getY());
                    }
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        synchronized (this) {
            super.paint(g);
            for (GraphicEntity entity : entityIdMap.values()) {
                entity.paint(g);
            }
        }
    }

    public GraphicEntity getEntityById(String id) {
        return entityIdMap.get(id);
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

    public synchronized Collection<GraphicEntity> getEntities() {
        return List.copyOf(entityIdMap.values());
    }

    private synchronized void invokeEntityRightListeners(String id) {
        for (EntityClickedListener listener : entityRightListeners) {
            listener.EntityClicked(id);
        }
    }

    private synchronized void invokeEntityLeftClickListeners(String id) {
        for (EntityClickedListener listener : entityLeftListeners) {
            listener.EntityClicked(id);
        }
    }

    private synchronized void invokeAreaPointRightClickListeners(int x, int y) {
        for (AreaPointClickedListener listener : areaPointRightClickedListeners) {
            listener.pointClicked(x, y);
        }
    }

    private synchronized void invokeAreaPointLeftListeners(int x, int y) {
        for (AreaPointClickedListener listener : areaPointLeftClickedListeners) {
            listener.pointClicked(x, y);
        }
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

    public synchronized void addEntityRightButtonClickedListener(EntityClickedListener l) {
        entityRightListeners.add(l);
    }

    public synchronized boolean removeEntityRightClickedListener(EntityClickedListener l) {
        return entityRightListeners.remove(l);
    }

    public void clearMouseListeners() {
        entityRightListeners.clear();
    }

    public void addAreaPointLeftClickedListener(AreaPointClickedListener l) {
        areaPointLeftClickedListeners.add(l);
    }

    boolean removeAreaPointLeftClickedListener(AreaPointClickedListener l) {
        return areaPointLeftClickedListeners.remove(l);
    }

    public void addEntityLeftButtonClickedListener(EntityClickedListener l) {
        entityLeftListeners.add(l);
    }

    public boolean removeEntityLeftButtonClickedListener(EntityClickedListener l) {
        return entityLeftListeners.remove(l);
    }

    public void addAreaPointRightClickedListener(AreaPointClickedListener l) {
        areaPointRightClickedListeners.add(l);
    }

    public boolean removeAreaPointRightClickedListener(AreaPointClickedListener l) {
        return areaPointRightClickedListeners.remove(l);
    }
}
