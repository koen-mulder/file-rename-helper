package com.github.koen_mulder.file_rename_helper.suggestions;

public class AIController  {

    private IAISuggestionService aiService;

    public AIController() {
        aiService = AISuggestionServiceFactory.getService();
    }
    
    public IAISuggestionService getAiService() {
        return aiService;
    }
}
