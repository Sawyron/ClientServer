package com.app.ui.habitat;

import com.app.domain.models.GraphicEntity;
import com.app.ui.graphicentity.GraphicEntityView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HabitatFrame extends JFrame implements GraphicEntityView {
    private final EntityPanel entityPanel = new EntityPanel();
    private final JButton startButton = new JButton("Start");

    public HabitatFrame(int width, int height) {
        setSize(width, height);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(entityPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        startButton.setFocusable(false);
        bottomPanel.add(startButton);
        add(bottomPanel, BorderLayout.SOUTH);

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
    public void run() {
        setVisible(true);
    }

    @Override
    public void addEntity(GraphicEntity entity) {
        entityPanel.addEntity(entity);
    }
}
