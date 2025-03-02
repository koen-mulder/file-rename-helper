package com.github.koen_mulder.file_rename_helper.processing;

/**
 * Enum representing the state of a {@link FileProcessingItem}.
 */
public enum EFileProcessingItemState {
    // File has been renamed by user
    RENAMED,
    // File has been processed and suggestions have been generated
    PROCESSED,
    // File is currently being processed
    PROCESSING,
    // File was processed but needs to be re-processed (for more suggestions)
    REQUEUED,
    // File is in the backlog and has not been processed yet
    BACKLOG,
    // File is new and has not been added to the backlog yet
    NEW;
}
