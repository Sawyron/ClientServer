package com.app.ui.graphicentity;

import com.app.domain.models.GraphicEntity;

import java.awt.event.ActionListener;

public interface GraphicEntityView {
    void addEntity(GraphicEntity entity);
    void addStartActionListener(ActionListener l);
    void removeStartActionListener(ActionListener l);
    int getWidth();
    int getHeight();
    void run();
}
