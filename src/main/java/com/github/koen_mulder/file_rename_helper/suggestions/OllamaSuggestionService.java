package com.github.koen_mulder.file_rename_helper.suggestions;

import java.util.List;

import com.github.koen_mulder.file_rename_helper.app.EConfigIdentifier;
import com.github.koen_mulder.file_rename_helper.app.IConfigChangeListener;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;

/**
 * Service for providing AI filename suggestions using Ollama.
 */
public class OllamaSuggestionService implements IAISuggestionService {

    private final AIConfigManager aiConfigManager = AIConfigManager.getInstance();

    private ChatModel model;
    private IRenameAssistant assistant;
    
    public OllamaSuggestionService() {
        initialiseOllamaChatModel();
        initialiseRenameAssistant();
        
        aiConfigManager.addConfigChangeListener(new IConfigChangeListener() {
            
            @Override
            public void onConfigChanged(EConfigIdentifier configId) {
                switch (configId) {
                case EConfigIdentifier.MODEL_NAME:
                case EConfigIdentifier.OLLAMA_ENDPOINT:
                    initialiseOllamaChatModel();
                    initialiseRenameAssistant();
                default:
                    // Do nothing
                    break;
                }
            }
        });
    }
    
    private void initialiseOllamaChatModel() {
        model = OllamaChatModel.builder()
                .baseUrl(aiConfigManager.getOllamaEndpoint())
                .temperature(0.2)
                .logRequests(true)
                .logResponses(true)
                .modelName(aiConfigManager.getModelName())
                .responseFormat(ResponseFormat.JSON)
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .build();
    }
    
    private void initialiseRenameAssistant() {
        assistant = AiServices.builder(IRenameAssistant.class)
                .chatModel(model)
                .systemMessageProvider(message -> aiConfigManager.getSystemMessage())
                // TODO: use chat memory provider
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
    
    @Override
    public Suggestions getFilenameSuggestions(FileProcessingItem item) {
        // TODO Remove when chat memory is implemented
        initialiseRenameAssistant();
        
        // Get the prompt template from the configuration
        String promptTemplate = aiConfigManager.getFilenamePrompt();
        
        // Load document
        Document document = FileSystemDocumentLoader.loadDocument(item.getOriginalAbsoluteFilePath());
        
        // Replace the content placeholder with the actual content
        String userMessage = promptTemplate.replace("{file_content}", document.text());
        
        return assistant.getFilenameSuggestions(userMessage);
    }

    @Override
    public Suggestions getAdditionalFilenameSuggestions(FileProcessingItem item) {
        // Get the prompt template from the configuration
        String promptTemplate = aiConfigManager.getAdditionalFilenamePrompt();
        
        // Replace placeholders in the prompt template
        String userMessage = replacePlaceholders(promptTemplate, item);
        
        // Get more suggestions
        return assistant.getAdditionalFilenameSuggestions(userMessage);
    }

    @Override
    public Suggestions getImportantKeywordsSuggestions(FileProcessingItem item) {
        // Get the prompt template from the configuration
        String promptTemplate = aiConfigManager.getKeywordsPrompt();
        
        // Replace placeholders in the prompt template
        String userMessage = replacePlaceholders(promptTemplate, item);
        
        return assistant.getImportantKeywordsSuggestions(userMessage);
    }

    @Override
    public Suggestions getAdditionalImportantKeywordsSuggestions(FileProcessingItem item) {
        // Get the prompt template from the configuration
        String promptTemplate = aiConfigManager.getAdditionalKeywordsPrompt();
        
        // Replace placeholders in the prompt template
        String userMessage = replacePlaceholders(promptTemplate, item);
        
        return assistant.getAdditionalImportantKeywordsSuggestions(userMessage);
    }
    
    @Override
    public Suggestions getImportantDatesSuggestions(FileProcessingItem item) {
        // Get the prompt template from the configuration
        String promptTemplate = aiConfigManager.getDatesPrompt();
        
        // Replace placeholders in the prompt template
        String userMessage = replacePlaceholders(promptTemplate, item);
        
        return assistant.getImportantDatesSuggestions(userMessage);
    }

    @Override
    public Suggestions getAdditionalImportantDatesSuggestions(FileProcessingItem item) {
        // Get the prompt template from the configuration
        String promptTemplate = aiConfigManager.getAdditionalDatesPrompt();
        
        // Replace placeholders in the prompt template
        String userMessage = replacePlaceholders(promptTemplate, item);
        
        return assistant.getAdditionalImportantDatesSuggestions(userMessage);
    }

    @Override
    public Suggestions getFilepathSuggestions(FileProcessingItem item) {
        // Get the prompt template from the configuration
        String promptTemplate = aiConfigManager.getFilepathPrompt();
        
        // Replace placeholders in the prompt template
        String userMessage = replacePlaceholders(promptTemplate, item);
        
        return assistant.getFilepathSuggestions(userMessage);
    }

    @Override
    public Suggestions getAdditionalFilepathSuggestions(FileProcessingItem item) {
        // Get the prompt template from the configuration
        String promptTemplate = aiConfigManager.getAdditionalFilepathPrompt();
        
        // Replace placeholders in the prompt template
        String userMessage = replacePlaceholders(promptTemplate, item);
        
        return assistant.getAdditionalFilepathSuggestions(userMessage);
    }
    
    private String replacePlaceholders(String promptTemplate, FileProcessingItem item) {
        List<String> prevFilenames = item.getFilenameSuggestions();
        List<String> prevFilepaths = item.getFilepathSuggestions();
        List<String> prevKeywords = item.getKeywordSuggestions();
        List<String> prevDates = item.getDateSuggestions();
        
        return promptTemplate
                .replace("{filename_suggestions}", String.join(", ", prevFilenames))
                .replace("{filepath_suggestions}", String.join(", ", prevFilepaths))
                .replace("{keyword_suggestions}", String.join(", ", prevKeywords))
                .replace("{date_suggestions}", String.join(", ", prevDates));
    }

}
