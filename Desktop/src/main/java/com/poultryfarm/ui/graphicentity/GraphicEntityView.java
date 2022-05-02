package com.poultryfarm.ui.graphicentity;

import com.poultryfarm.domain.GraphicEntity;

import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.Collection;

public interface GraphicEntityView {
    void setActiveState();

    void setStoppedState();

    void setPausedState();

    void setResumedState();

    void addEntity(GraphicEntity entity, String id);

    Collection<GraphicEntity> getEntities();

    void clearEntities();

    GraphicEntity getEntityById(String id);

    void removeEntity(String id);

    void addFileFilter(FileFilter filter);

    void addLoadEntityListener(LoadEntityListener l);

    void removeLoadEntityListener(LoadEntityListener l);

    void addSaveEntityListener(SaveEntityListener l);

    void removeSaveEntityListener(SaveEntityListener l);

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
