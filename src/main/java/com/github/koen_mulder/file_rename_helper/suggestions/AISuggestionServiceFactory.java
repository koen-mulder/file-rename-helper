package com.github.koen_mulder.file_rename_helper.suggestions;

/**
 * Factory class for creating an instance of the AI suggestion service. 
 */
public class AISuggestionServiceFactory {
    
    public static IAISuggestionService getService() {
        return new OllamaSuggestionService();
    }
}
