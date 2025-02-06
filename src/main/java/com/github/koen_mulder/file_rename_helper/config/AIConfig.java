package com.github.koen_mulder.file_rename_helper.config;

public class AIConfig {

    private String modelName = "llama3.2:3b";
    private String ollamaEndpoint = "http://localhost:11434";
    private String embeddingStoreFile = "embedding_store.json";

    private String filenamePrompt = "Je hebt een PDF bestand ontvangen met deze informatie. Geeft 10 suggesties voor"
            + "de naamgeving van dit document.";

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getOllamaEndpoint() {
        return ollamaEndpoint;
    }

    public void setOllamaEndpoint(String ollamaEndpoint) {
        this.ollamaEndpoint = ollamaEndpoint;
    }

    public String getEmbeddingStoreFile() {
        return embeddingStoreFile;
    }

    public void setEmbeddingStoreFile(String embeddingStoreFile) {
        this.embeddingStoreFile = embeddingStoreFile;
    }

    String getFilenamePrompt() {
        return filenamePrompt;
    }

    void setFilenamePrompt(String filenamePrompt) {
        this.filenamePrompt = filenamePrompt;
    }
}
