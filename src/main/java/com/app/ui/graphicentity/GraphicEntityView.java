package com.app.ui.graphicentity;

import com.app.domain.GraphicEntity;
import com.app.ui.habitat.EntityClickedListener;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public interface GraphicEntityView {
    void addEntity(GraphicEntity entity, String id);

    void clearEntities();

    void removeEntity(String id);

    void addStartActionListener(ActionListener l);

    void removeStartActionListener(ActionListener l);

    void addStopActionListener(ActionListener l);

    void addPauseActionListener(ActionListener l);

    void addResumeActionLister(ActionListener l);

    void addWindowAction(WindowListener l);

    int getWidth();

    int getHeight();

    void moveEntities();

    void run();

    void update();

    void addEntityClickedListener(EntityClickedListener l);

    boolean removeEntityClickedListener(EntityClickedListener l);
}
