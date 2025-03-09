package com.github.koen_mulder.file_rename_helper.suggestions;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;

/**
 * Service for providing AI filename suggestions.
 */
public interface IAISuggestionService {

    /**
     * Get suggestions for renaming a file.
     * 
     * @param item The file to get suggestions for.
     * @return Suggestions for renaming a file.
     */
    Suggestions getFilenameSuggestions(FileProcessingItem item);

    /**
     * Get more suggestions for renaming a file.
     * 
     * @param item The file to get suggestions for.
     * @return More suggestions for renaming a file.
     */
    Suggestions getAdditionalFilenameSuggestions(FileProcessingItem item);

    /**
     * Get suggestions for important keywords.
     * 
     * @param item The file to get suggestions for.
     * @return Suggestions for important keywords.
     */
    Suggestions getImportantKeywordsSuggestions(FileProcessingItem item);

    /**
     * Get more suggestions for important keywords.
     * 
     * @param item The file to get suggestions for.
     * @return More suggestions for important keywords.
     */
    Suggestions getAdditionalImportantKeywordsSuggestions(FileProcessingItem item);

    /**
     * Get suggestions for important dates.
     * 
     * @param item The file to get suggestions for.
     * @return Suggestions for important dates.
     */
    Suggestions getImportantDatesSuggestions(FileProcessingItem item);
    
    /**
     * Get more suggestions for important dates.
     * 
     * @param item The file to get suggestions for.
     * @return More suggestions for important dates.
     */
    Suggestions getAdditionalImportantDatesSuggestions(FileProcessingItem item);
    
    /**
     * Get suggestions for file paths.
     * 
     * @param item The file to get suggestions for.
     * @return Suggestions for file paths.
     */
    Suggestions getFilepathSuggestions(FileProcessingItem item);

    /**
     * Get more suggestions for file paths.
     * 
     * @param item The file to get suggestions for.
     * @return More suggestions for file paths.
     */
    Suggestions getAdditionalFilepathSuggestions(FileProcessingItem item);

}
