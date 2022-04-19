package com.poultryfarm.ui.graphicentity;

import com.poultryfarm.domain.GraphicEntity;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public interface GraphicEntityView {
    void addEntity(GraphicEntity entity, String id);

    void addEntitySerializer(String name, LoadEntityListener loadListener, SaveEntityListener saveListener);

    void clearEntities();

    GraphicEntity getEntityById(String id);

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

    void addEntityRightButtonClickedListener(EntityClickedListener l);

    boolean removeEntityRightButtonClickedListener(EntityClickedListener l);

    void addEntityLeftButtonClickedListener(EntityClickedListener l);

    boolean removeEntityLeftButtonClickedListener(EntityClickedListener l);

    void addAreaPointLeftButtonClickedListener(AreaPointClickedListener l);

    boolean removeAreaPointLeftButtonClickedListener(AreaPointClickedListener l);

    void addAreaPointRightButtonClickedListener(AreaPointClickedListener l);

    boolean removeAreaPointRightButtonClickedListener(AreaPointClickedListener l);

    void clearClickListeners();
}
