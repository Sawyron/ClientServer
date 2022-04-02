package com.app.ui.habitat;

import com.app.domain.models.GraphicEntity;
import com.app.ui.graphicentity.GraphicEntityView;

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
            JOptionPane.showMessageDialog(this, e.getMessage(), "Unable to set system look and fell", JOptionPane.WARNING_MESSAGE);
        }
    }

    public HabitatFrame() {
        this(400, 300);
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
    public void addEntity(GraphicEntity entity, String id) {
        entityPanel.addEntity(entity, id);
    }

    @Override
    public void clearEntities() {
        entityPanel.clear();
    }

    @Override
    public void removeEntity(String id) {
        entityPanel.removeEntity(id);
    }
}
