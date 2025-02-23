package com.github.koen_mulder.file_rename_helper.controller;

import java.util.ArrayList;

import com.github.koen_mulder.file_rename_helper.interfaces.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IOpenFileActionPublisher;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.google.common.collect.Lists;

/**
 * This class maintains a list of listeners that are interested in receiving
 * notifications when a file is opened for manual processing.
 */
public class OpenFileActionPublisher implements IOpenFileActionPublisher {

    private ArrayList<IOpenFileActionListener> listeners = Lists.newArrayList();


    @Override
    public void addOpenFileActionListener(IOpenFileActionListener listener) {
        listeners.add(listener);
        
    }

    @Override
    public void removeOpenFileActionListener(IOpenFileActionListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyOpenFileActionListeners(FileProcessingItem fileItem) {
        for (IOpenFileActionListener listener : listeners) {
            listener.onOpenFileAction(fileItem);
        }        
    }

}
