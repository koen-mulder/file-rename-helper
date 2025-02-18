package com.github.koen_mulder.file_rename_helper.interfaces;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;

/**
 * Implementing components want to be able to respond when the Open File (for renaming) Action is performed.
 */
public interface IOpenFileActionListener {
    
    /**
     * This method will be called when a File needs to be opened for manual renaming.
     * 
     * @param fileItem containing file information and naming suggestions for the file that needs to be opened
     */
    void onOpenFileAction(FileProcessingItem fileItem);

}
