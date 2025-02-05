package com.github.koen_mulder.file_rename_helper.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.function.Function;

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

public class AIController  {

    interface RenameAssistant {

        String chat(String userMessage);
    }

    private static final String MODEL_NAME = "llama3.2:3b";
    private static final String OLLAMA_ENDPOINT = "http://localhost:11434";
    
    private static final String EMBEDDING_STORE_FILE = "embeddingStore.json";

    private ChatLanguageModel model;
    private InMemoryEmbeddingStore<TextSegment> embeddingStore;
    private RenameAssistant assistant;
    
    private String systemMessage = "Je hebt een PDF bestand ontvangen met deze informatie. Geeft 10 suggesties voor de naamgeving van dit document.";

    public AIController() {
        model = OllamaChatModel.builder().baseUrl(OLLAMA_ENDPOINT).temperature(0.2).logRequests(true).logResponses(true)
                .modelName(MODEL_NAME).build();
        
        embeddingStore = loadEmbeddingStore(EMBEDDING_STORE_FILE);
        
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
                saveEmbeddingStore(EMBEDDING_STORE_FILE);
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
}
