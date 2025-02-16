package com.github.koen_mulder.file_rename_helper.interfaces;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModel;

public interface FileProcessingModelPublisher {
    
    /**
     * Adds a listener that will be notified when there has been a change in a {@link FileProcessingModel}
     */
    void addFileProcessingModelListener(FileProcessingModelListener listener);

    /**
     * Removes a previously added listener.
     * 
     * @param listener The listener to be removed.
     */
    void removeFileProcessingModelListener(FileProcessingModelListener listener);

}
