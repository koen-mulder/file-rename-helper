package com.github.koen_mulder.file_rename_helper.controller;

import java.util.ArrayList;

import com.github.koen_mulder.file_rename_helper.interfaces.FileSelectionListener;
import com.github.koen_mulder.file_rename_helper.interfaces.FileSelectionPublisher;
import com.google.common.collect.Lists;

public class FileSelectionController implements FileSelectionPublisher {

    private ArrayList<FileSelectionListener> fileSelectionListeners = Lists.newArrayList();
    
    @Override
    public void addFileSelectionListener(FileSelectionListener listener) {
        this.fileSelectionListeners.add(listener);

    }

    @Override
    public void removeFileSelectionListener(FileSelectionListener listener) {
        this.fileSelectionListeners.remove(listener);
    }

    @Override
    public void notifyFileSelectionListeners(String filePath) {
        for (FileSelectionListener listener : fileSelectionListeners) {
            listener.onFileSelected(filePath);
        }
    }
}