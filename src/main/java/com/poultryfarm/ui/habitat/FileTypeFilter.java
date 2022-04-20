package com.poultryfarm.ui.habitat;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileTypeFilter extends FileFilter {
    private final String description;
    private final String extension;

    public FileTypeFilter(String description, String extension) {
        this.description = description;
        this.extension = extension;
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return f.getName().endsWith(extension);
    }

    @Override
    public String getDescription() {
        return description + " " + String.format("(*%s)", extension);
    }
}
