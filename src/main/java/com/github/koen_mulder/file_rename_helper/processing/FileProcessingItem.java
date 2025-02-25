package com.github.koen_mulder.file_rename_helper.processing;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.controller.AIController.FilenameSuggestions;

public class FileProcessingItem {

    private final String originalAbsoluteFilePath;
    private final String originalFileName;
    
    private final List<FilenameSuggestions> suggestions = Lists.newArrayList();
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

    public void addSuggestions(FilenameSuggestions suggestions) {
        this.suggestions.add(suggestions);
    }
    
    public List<FilenameSuggestions> getSuggestions() {
        return suggestions;
    }
    
    public Path getTemporaryFilePath() {
        return Path.of("file-rename-helper-temp_" + originalFileName);
    }
    
    /**
     * This equals method checks whether the files that are being processed is the same.
     */
    @Override
    public boolean equals(Object obj) {
        // Check if the objects are the same instance
        if (this == obj) {
            return true;
        }
        
        // Check if the 'obj' is null or of a different class
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        // Compare the relevant fields for equality.
        FileProcessingItem otherItem = (FileProcessingItem) obj;
        return Objects.equals(originalAbsoluteFilePath, otherItem.originalAbsoluteFilePath);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(originalAbsoluteFilePath);
    }
    
}
