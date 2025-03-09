package com.github.koen_mulder.file_rename_helper.processing;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.suggestions.Suggestions;

/**
 * Represents a file that is being processed by the application.
 */
public class FileProcessingItem {

    // File information
    private final String originalAbsoluteFilePath;
    private final String originalFileName;
    
    // Suggestions for renaming the file
    private final List<String> filenameSuggestions = Lists.newArrayList();
    private final List<String> filepathSuggestions = Lists.newArrayList();
    private final List<String> keywordSuggestions = Lists.newArrayList();
    private final List<String> dateSuggestions = Lists.newArrayList();

    // Processing state of the file
    private EFileProcessingItemState state;
    
    public FileProcessingItem(String absoluteFilePath, String fileName) {
        originalAbsoluteFilePath = absoluteFilePath;
        originalFileName = fileName;
        
        setState(EFileProcessingItemState.NEW);
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

    
    public void addFilenameSuggestions(Suggestions suggestions) {
        filenameSuggestions.addAll(suggestions.suggestions());
    }
    
    public List<String> getFilenameSuggestions() {
        return filenameSuggestions;
    }
    
    public void addFilepathSuggestions(Suggestions suggestions) {
        filepathSuggestions.addAll(suggestions.suggestions());
    }
    
    public List<String> getFilepathSuggestions() {
        return filepathSuggestions;
    }
    
    public void addKeywordSuggestions(Suggestions suggestions) {
        keywordSuggestions.addAll(suggestions.suggestions());
    }
    
    public List<String> getKeywordSuggestions() {
        return keywordSuggestions;
    }
    
    public void addDateSuggestions(Suggestions suggestions) {
        dateSuggestions.addAll(suggestions.suggestions());
    }
    
    public List<String> getDateSuggestions() {
        return dateSuggestions;
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
    
    @Override
    public String toString() {
        return String.format("%s (%s, %s)", originalFileName, state, originalAbsoluteFilePath);
    }
}
