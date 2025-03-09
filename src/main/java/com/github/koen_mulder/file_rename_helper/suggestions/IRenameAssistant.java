package com.github.koen_mulder.file_rename_helper.suggestions;

import dev.langchain4j.service.UserMessage;

/**
 * Interface for creating an AI service for rename suggestions.
 * 
 * @see dev.langchain4j.service.AiServices
 */
interface IRenameAssistant {

    /**
     * Get suggestions for renaming a file.
     * 
     * @param userMessage The request for suggestions including the (partial) content and relevant
     *                    context of the file to get suggestions for.
     * @return Suggestions for renaming a file.
     */
    Suggestions getFilenameSuggestions(@UserMessage String userMessage);

    /**
     * Get more suggestions for renaming a file.
     * 
     * @param userMessage The request for more filename suggestions.
     * @return More suggestions for renaming a file.
     */
    Suggestions getAdditionalFilenameSuggestions(@UserMessage String userMessage);

    /**
     * Get suggestions for important keywords.
     * 
     * @param userMessage The request for keyword suggestions.
     * @return Suggestions for important keywords.
     */
    Suggestions getImportantKeywordsSuggestions(@UserMessage String userMessage);

    /**
     * Get more suggestions for important keywords.
     * 
     * @param userMessage The request for more keyword suggestions.
     * @return More suggestions for important keywords.
     */
    Suggestions getAdditionalImportantKeywordsSuggestions(@UserMessage String userMessage);

    /**
     * Get suggestions for important dates.
     * 
     * @param userMessage The request for date suggestions.
     * @return Suggestions for important dates.
     */
    Suggestions getImportantDatesSuggestions(@UserMessage String userMessage);
    
    /**
     * Get more suggestions for important dates.
     * 
     * @param userMessage The request for more date suggestions.
     * @return More suggestions for important dates.
     */
    Suggestions getAdditionalImportantDatesSuggestions(@UserMessage String userMessage);
    
    /**
     * Get suggestions for file paths.
     * 
     * @param userMessage The request for file path suggestions.
     * @return Suggestions for file paths.
     */
    Suggestions getFilepathSuggestions(@UserMessage String userMessage);

    /**
     * Get more suggestions for file paths.
     * 
     * @param userMessage The request for more file path suggestions.
     * @return More suggestions for file paths.
     */
    Suggestions getAdditionalFilepathSuggestions(@UserMessage String userMessage);

}
