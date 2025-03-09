package com.github.koen_mulder.file_rename_helper.suggestions;

public class AIController  {

    private OllamaSuggestionService aiService;

    public AIController() {
        aiService = new OllamaSuggestionService();
    }
    
    public IAISuggestionService getAiService() {
        return aiService;
    }
}
