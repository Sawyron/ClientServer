package com.poultryfarm.ui.graphicentity;

import java.io.File;

@FunctionalInterface
public interface LoadEntityListener {
    void onEntityLoading(File file);
}
