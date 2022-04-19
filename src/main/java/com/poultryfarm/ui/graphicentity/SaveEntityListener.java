package com.poultryfarm.ui.graphicentity;

@FunctionalInterface
public interface SaveEntityListener {
    void onEntitySaving(String path);
}
