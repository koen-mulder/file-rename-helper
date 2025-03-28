package com.github.koen_mulder.file_rename_helper.processing.api;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelEvent;

/**
 * The FileProcessingModelPublisher interface is implemented by classes that manage file processing
 * and notify registered IFileProcessingModelListener's when the model has changed.
 */
public interface IFileProcessingModelPublisher {

    /**
     * Adds a listener that will be notified when there has been a change in a
     * {@link FileProcessingModel}
     */
    void addFileProcessingModelListener(IFileProcessingModelListener listener);

    /**
     * Removes a previously added listener.
     * 
     * @param listener The listener to be removed.
     */
    void removeFileProcessingModelListener(IFileProcessingModelListener listener);

    /**
     * @param event notifying listeners of a model update.
     */
    void fireModelChanged(FileProcessingModelEvent event);
}
