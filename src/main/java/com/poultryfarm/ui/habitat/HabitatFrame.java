package com.poultryfarm.ui.habitat;

import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.ui.UIException;
import com.poultryfarm.ui.graphicentity.AreaPointClickedListener;
import com.poultryfarm.ui.graphicentity.EntityClickedListener;
import com.poultryfarm.ui.graphicentity.GraphicEntityView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HabitatFrame extends JFrame implements GraphicEntityView {
    private final EntityPanel entityPanel = new EntityPanel();
    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JButton pauseButton = new JButton("Pause");
    private final JButton resumeButton = new JButton("Resume");

    public HabitatFrame(int width, int height) {
        setSize(width, height);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(entityPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        startButton.setFocusable(false);
        stopButton.setFocusable(false);
        pauseButton.setFocusable(false);
        resumeButton.setFocusable(false);
        bottomPanel.add(startButton);
        bottomPanel.add(stopButton);
        bottomPanel.add(pauseButton);
        bottomPanel.add(resumeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        startButton.addActionListener((e) -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);
        });
        stopButton.addActionListener((e) -> {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(false);
        });
        resumeButton.addActionListener((e) -> {
            resumeButton.setEnabled(false);
            pauseButton.setEnabled(true);
        });
        pauseButton.addActionListener((e) -> {
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(true);
        });

        setTitle("Poultry farm");
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            throw new UIException(e.getMessage(), e);
        }
    }

    @Override
    public void addStartActionListener(ActionListener l) {
        startButton.addActionListener(l);
    }

    @Override
    public void removeStartActionListener(ActionListener l) {
        startButton.removeActionListener(l);
    }

    @Override
    public void addStopActionListener(ActionListener l) {
        stopButton.addActionListener(l);
    }

    @Override
    public void addPauseActionListener(ActionListener l) {
        pauseButton.addActionListener(l);
    }

    @Override
    public void addResumeActionLister(ActionListener l) {
        resumeButton.addActionListener(l);
    }

    @Override
    public void addWindowAction(WindowListener l) {
        addWindowListener(l);
    }

    @Override
    public void run() {
        setVisible(true);
    }

    @Override
    public void update() {
        entityPanel.repaint();
    }

    @Override
    public void addEntityRightButtonClickedListener(EntityClickedListener l) {
        entityPanel.addEntityRightButtonClickedListener(l);
    }

    @Override
    public boolean removeEntityRightButtonClickedListener(EntityClickedListener l) {
        return entityPanel.removeEntityRightClickedListener(l);
    }

    @Override
    public void addEntityLeftButtonClickedListener(EntityClickedListener l) {
        entityPanel.addEntityLeftButtonClickedListener(l);
    }

    @Override
    public boolean removeEntityLeftButtonClickedListener(EntityClickedListener l) {
        return entityPanel.removeEntityLeftButtonClickedListener(l);
    }

    @Override
    public void addAreaPointLeftButtonClickedListener(AreaPointClickedListener l) {
        entityPanel.addAreaPointLeftClickedListener(l);
    }

    @Override
    public boolean removeAreaPointLeftButtonClickedListener(AreaPointClickedListener l) {
        return entityPanel.removeAreaPointLeftClickedListener(l);
    }

    @Override
    public void addAreaPointRightButtonClickedListener(AreaPointClickedListener l) {
        entityPanel.addAreaPointRightClickedListener(l);
    }

    @Override
    public boolean removeAreaPointRightButtonClickedListener(AreaPointClickedListener l) {
        return entityPanel.removeAreaPointRightClickedListener(l);
    }

    @Override
    public void clearClickListeners() {
        entityPanel.clearMouseListeners();
    }

    @Override
    public void addEntity(GraphicEntity entity, String id) {
        entityPanel.addEntity(entity, id);
    }

    @Override
    public void clearEntities() {
        entityPanel.clear();
    }

    @Override
    public GraphicEntity getEntityById(String id) {
        return entityPanel.getEntityById(id);
    }

    @Override
    public void removeEntity(String id) {
        entityPanel.removeEntity(id);
    }

    @Override
    public void moveEntities() {
        entityPanel.moveEntities();
    }
}
