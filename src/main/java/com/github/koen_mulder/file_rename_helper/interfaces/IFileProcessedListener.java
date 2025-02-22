package com.github.koen_mulder.file_rename_helper.interfaces;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;

/**
 * Implementing components want to be notified of files having been processed for suggestions.
 */
public interface IFileProcessedListener extends java.util.EventListener {

    /**
     * This notification tells listeners when a file has been processed and suggestions have been
     * generated.
     *
     * @param fileItem a {@code FileProcessingItem} to notify listener that suggestions have been
     *                 generated
     */
    public void onFileProcessed(FileProcessingItem fileItem);
}
