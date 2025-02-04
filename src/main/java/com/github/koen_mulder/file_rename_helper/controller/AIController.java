package com.github.koen_mulder.file_rename_helper.controller;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModelFactory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class AIController {

    interface RenameAssistant {

        String chat(String userMessage);
    }
    
    private static final String MODEL_NAME = "llama3.2:3b";
    private static final String OLLAMA_ENDPOINT = "http://localhost:11434";
    
    private ChatLanguageModel model;

    public AIController() {
     model = OllamaChatModel.builder()
              .baseUrl(OLLAMA_ENDPOINT)
              .temperature(0.2)
              .logRequests(true)
              .logResponses(true)
              .modelName(MODEL_NAME)
              .build();
    }
    
    public String generatePossibleFileNames(String filePath) {
        
        Document document = FileSystemDocumentLoader.loadDocument(filePath);
        
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.builder().embeddingModel(new BgeSmallEnV15QuantizedEmbeddingModelFactory().create())
                .embeddingStore(embeddingStore).build().ingest(document);
        
        RenameAssistant assistant = AiServices.builder(RenameAssistant.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();
        
        String answer = assistant.chat("Geeft 10 suggesties voor de naamgeving van dit document.");
        
        return answer;
    }
}
