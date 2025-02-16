package com.github.koen_mulder.file_rename_helper.processing;

public class FileProcessingItem {

    private final String originalAbsoluteFilePath;
    private final String originalFileName;
    
    private EFileProcessingItemState state;
    
    public FileProcessingItem(String absoluteFilePath, String fileName) {
        originalAbsoluteFilePath = absoluteFilePath;
        originalFileName = fileName;
        
        setState(EFileProcessingItemState.BACKLOG);
    }

    public String getOriginalAbsoluteFilePath() {
        return originalAbsoluteFilePath;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public EFileProcessingItemState getState() {
        return state;
    }

    public void setState(EFileProcessingItemState state) {
        this.state = state;
    }
    
}
