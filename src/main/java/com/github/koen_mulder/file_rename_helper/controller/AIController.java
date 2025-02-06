package com.github.koen_mulder.file_rename_helper.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.function.Function;

import com.github.koen_mulder.file_rename_helper.config.AIConfigManager;
import com.github.koen_mulder.file_rename_helper.config.EConfigIdentifier;
import com.github.koen_mulder.file_rename_helper.interfaces.ConfigChangeListener;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class AIController implements ConfigChangeListener {

    interface RenameAssistant {

        String chat(String userMessage);
    }

    private ChatLanguageModel model;
    private InMemoryEmbeddingStore<TextSegment> embeddingStore;
    private RenameAssistant assistant;
    
    private AIConfigManager aiConfigManager = AIConfigManager.getInstance();
    
    private String systemMessage;
    private String modelName;
    private String ollamaEndpoint;
    private String embeddingStoreFile;
    

    public AIController() {
        aiConfigManager.addConfigChangeListener(this);
        
        systemMessage = aiConfigManager.getFilenamePrompt();
        modelName = aiConfigManager.getModelName();
        ollamaEndpoint = aiConfigManager.getOllamaEndpoint();
        embeddingStoreFile = aiConfigManager.getEmbeddingStoreFile();
        
        initializeAIServices();
    }

    private void initializeAIServices() {
        model = OllamaChatModel.builder()
                .baseUrl(ollamaEndpoint)
                .temperature(0.2)
                .logRequests(true)
                .logResponses(true)
                .modelName(modelName)
                .build();
        
        embeddingStore = loadEmbeddingStore(embeddingStoreFile);
        
        Function<Object, String> systemMessageProvider = new Function<Object, String>() {
            @Override
            public String apply(Object t) {
                return systemMessage;
            }
        };
        
        assistant = AiServices.builder(RenameAssistant.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .systemMessageProvider(systemMessageProvider)
                .build();
    }

    public String generatePossibleFileNames(String filePath) {
        // Load document
        Document document = FileSystemDocumentLoader.loadDocument(filePath);
        
        String answer = assistant.chat(document.toString());
        return answer;
    }
    
    public void ingestRenamedPdf(String newFilePath) {
        
        // Load document
        Document document = FileSystemDocumentLoader.loadDocument(newFilePath);
        
        // Ingest document
        EmbeddingStoreIngestor.ingest(document, embeddingStore);
    }
    
    /**
     * @return WindowListener for making sure the embedding store is saved
     */
    public WindowListener getWindowListener() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveEmbeddingStore(embeddingStoreFile);
            }
        };
    }
    
    private InMemoryEmbeddingStore<TextSegment> loadEmbeddingStore(String filePath) {
        File file = new File(filePath);
        InMemoryEmbeddingStore<TextSegment> store = null;
        
        if (file.exists()) {
            store = InMemoryEmbeddingStore.fromFile(filePath);
        } else {
            store = new InMemoryEmbeddingStore<>();
        }
        
        return store;
    }
    
    private void saveEmbeddingStore(String filePath) {
        embeddingStore.serializeToFile(filePath);
    }

    @Override
    public void onConfigChanged(EConfigIdentifier configId) {
        switch (configId) {
        case EConfigIdentifier.FILENAME_PROMPT:
            systemMessage = aiConfigManager.getFilenamePrompt();
            break;
        case EConfigIdentifier.MODEL_NAME:
            modelName = aiConfigManager.getModelName();
            break;
        case EConfigIdentifier.OLLAMA_ENDPOINT:
            ollamaEndpoint = aiConfigManager.getOllamaEndpoint();
            break;
        case EConfigIdentifier.EMBEDDING_STORE_FILE_PATH:
            embeddingStoreFile = aiConfigManager.getEmbeddingStoreFile();
            break;
        default:
            break;
        }
        
        initializeAIServices();
    }
}
