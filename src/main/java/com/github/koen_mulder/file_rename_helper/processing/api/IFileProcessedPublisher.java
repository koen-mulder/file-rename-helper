package com.github.koen_mulder.file_rename_helper.processing.api;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;

/**
 * This interface is used for signalling components that a file has been processed for suggestions.
 */
public interface IFileProcessedPublisher {

    /**
     * Adds a listener that will be notified of the action.
     * 
     * @param listener The listener to be added.
     */
    void addFileProcessedListener(IFileProcessedListener listener);

    /**
     * Removes a previously added listener.
     * 
     * @param listener The listener to be removed.
     */
    void removeFileProcessedListener(IFileProcessedListener listener);

    /**
     * Notifies all registered listeners that a file has been processed for suggestions.
     * 
     * @param fileItem containing file information and naming suggestions for the file that has been
     *                 processed.
     */
    void notifyFileProcessedListeners(FileProcessingItem fileItem);
}
