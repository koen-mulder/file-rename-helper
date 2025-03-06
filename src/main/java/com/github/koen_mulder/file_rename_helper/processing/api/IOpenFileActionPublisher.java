/**
 * 
 */
package com.github.koen_mulder.file_rename_helper.processing.api;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;

/**
 * This interface is used for signalling components that a file needs to be opened for manual renaming.
 */
public interface IOpenFileActionPublisher {
    /**
     * Adds a listener that will be notified of the action.
     * 
     * @param listener The listener to be added.
     */
    void addOpenFileActionListener(IOpenFileActionListener listener);

    /**
     * Removes a previously added listener.
     * 
     * @param listener The listener to be removed.
     */
    void removeOpenFileActionListener(IOpenFileActionListener listener);

    /**
     * Notifies all registered listeners that a file is selected for renaming.
     * 
     * @param fileItem containing file information and naming suggestions for the file that needs to be opened
     */
    void notifyOpenFileActionListeners(FileProcessingItem fileItem);
}
