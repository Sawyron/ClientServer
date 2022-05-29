package com.poultryfarm.ui.habitat;

import com.poultryfarm.domain.GraphicEntity;
import com.poultryfarm.ui.graphicentity.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.List;
import java.util.*;

public class HabitatFrame extends JFrame implements GraphicEntityView {
    private final EntityPanel entityPanel = new EntityPanel();
    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JButton pauseButton = new JButton("Pause");
    private final JButton resumeButton = new JButton("Resume");
    private final JButton sendButton = new JButton("Send");
    private final JButton receiveButton = new JButton("Receive");
    private final JButton getByIndexButton = new JButton("Get");
    private final JButton removeByIndexButton = new JButton("Remove");
    private final JButton countButton = new JButton("Count");
    private final JFileChooser fileDialog = new JFileChooser();
    private final JSpinner getIndexSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
    private final JSpinner removeIndexSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
    private final JComboBox<String> serverComboBox = new JComboBox<>();
    private final List<LoadEntityListener> loadEntityListeners = new LinkedList<>();
    private final List<SaveEntityListener> saveEntityListeners = new LinkedList<>();

    private final Map<FileFilter, String> filterExtensions = new HashMap<>();


    public HabitatFrame(int width, int height) {
        setSize(width, height);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(entityPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));
        JPanel flowControlPanel = new JPanel();
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        startButton.setFocusable(false);
        stopButton.setFocusable(false);
        pauseButton.setFocusable(false);
        resumeButton.setFocusable(false);
        flowControlPanel.add(startButton);
        flowControlPanel.add(stopButton);
        flowControlPanel.add(pauseButton);
        flowControlPanel.add(resumeButton);
        bottomPanel.add(flowControlPanel);
        JPanel networkPanel = new JPanel();
        sendButton.setFocusable(false);
        receiveButton.setFocusable(false);
        getByIndexButton.setFocusable(false);
        receiveButton.setFocusable(false);
        removeByIndexButton.setFocusable(false);
        JPanel getRemovePanel = new JPanel();
        networkPanel.add(sendButton);
        networkPanel.add(receiveButton);
        getRemovePanel.add(getByIndexButton);
        getRemovePanel.add(getIndexSpinner);
        getRemovePanel.add(removeByIndexButton);
        getRemovePanel.add(removeIndexSpinner);
        networkPanel.add(serverComboBox);
        networkPanel.add(countButton);
        bottomPanel.add(networkPanel);
        bottomPanel.add(getRemovePanel);
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        add(bottomPanel, BorderLayout.SOUTH);

        setTitle("Poultry farm");
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenu());
        setJMenuBar(menuBar);

        setStoppedState();
    }

    private JMenu createMenu() {
        JMenu menu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> {
            int result = fileDialog.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                FileFilter filter = fileDialog.getFileFilter();
                String extension = filterExtensions.get(filter);
                File file = fileDialog.getSelectedFile();
                if (extension != null) {
                    if (!file.getName().endsWith("." + extension)) {
                        file = new File(file.getAbsolutePath() + "." + extension);
                    }
                }
                invokeSaveEntityListeners(file);
            }
        });
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> {
            int result = fileDialog.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                invokeLoadEntityListeners(fileDialog.getSelectedFile());
            }
        });
        menu.add(saveItem);
        menu.add(loadItem);
        return menu;
    }

    private void invokeLoadEntityListeners(File file) {
        for (LoadEntityListener listener : loadEntityListeners) {
            listener.onEntityLoading(file);
        }
    }

    private void invokeSaveEntityListeners(File file) {
        for (SaveEntityListener listener : saveEntityListeners) {
            listener.onEntitySaving(file);
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
    public void addSendEntitiesActionListener(ActionListener l) {
        sendButton.addActionListener(l);
    }

    @Override
    public void addReceiveEntitiesActionListener(ActionListener l) {
        receiveButton.addActionListener(l);
    }

    @Override
    public void addGetEntityIndexListener(IndexListener l) {
        getByIndexButton.addActionListener((e) -> {
            Integer index = (Integer) getIndexSpinner.getValue();
            l.onIndexSelected(index);
        });
    }

    @Override
    public void addRemovingEntityIndexListener(IndexListener l) {
        removeByIndexButton.addActionListener((e) -> {
            Integer index = (Integer) removeIndexSpinner.getValue();
            l.onIndexSelected(index);
        });
    }

    @Override
    public void addCountButtonActionListener(ActionListener l) {
        countButton.addActionListener(l);
    }

    @Override
    public void addServerName(String name) {
        serverComboBox.addItem(name);
    }

    @Override
    public void addServerNameListener(StringListener l) {
        serverComboBox.addItemListener((e -> {
            String serverName = (String) serverComboBox.getSelectedItem();
            l.onStringSelected(serverName);
        }));
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
    public void setActiveState() {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);
    }

    @Override
    public void setStoppedState() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
    }

    @Override
    public void setPausedState() {
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(true);
    }

    @Override
    public void setResumedState() {
        resumeButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }

    @Override
    public void addEntity(GraphicEntity entity, String id) {
        entityPanel.addEntity(entity, id);
    }

    @Override
    public Collection<GraphicEntity> getEntities() {
        return entityPanel.getEntities();
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
    public void addLoadEntityListener(LoadEntityListener l) {
        loadEntityListeners.add(l);
    }

    @Override
    public void removeLoadEntityListener(LoadEntityListener l) {
        loadEntityListeners.remove(l);
    }

    @Override
    public void addSaveEntityListener(SaveEntityListener l) {
        saveEntityListeners.add(l);
    }

    @Override
    public void removeSaveEntityListener(SaveEntityListener l) {
        saveEntityListeners.remove(l);
    }

    @Override
    public void moveEntities() {
        entityPanel.moveEntities();
    }

    @Override
    public void addFileFilter(FileFilter filter, String extension) {
        fileDialog.addChoosableFileFilter(filter);
        filterExtensions.put(filter, extension);
    }
}
