package com.github.koen_mulder.file_rename_helper.interfaces;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelEvent;

public interface IFileProcessingModelListener extends java.util.EventListener {

    /**
     * This fine grain notification tells listeners the exact range
     * of rows that changed.
     *
     * @param e a {@code FileProcessingModelEvent} to notify listener that a model has changed
     */
    public void tableChanged(FileProcessingModelEvent e);
    
}
