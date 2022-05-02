package com.poultryfarm.ui.graphicentity;

import java.io.File;

@FunctionalInterface
public interface SaveEntityListener {
    void onEntitySaving(File file);
}
