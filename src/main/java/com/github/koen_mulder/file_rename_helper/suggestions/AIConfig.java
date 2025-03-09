package com.github.koen_mulder.file_rename_helper.suggestions;

/**
 * Object for storing AI configuration accessible by {@link AIConfigManager}.
 */
class AIConfig {

    private String modelName = "llama3.2:3b";
    private String ollamaEndpoint = "http://localhost:11434";
    private String embeddingStoreFile = "embedding_store.json";

    private String systemMessage = "Return a config error";

    private String filenamePrompt = "Return a config error";
    private String additionalFilenamePrompt = "Return a config error";

    private String keywordsPrompt = "Return a config error";
    private String additionalKeywordsPrompt = "Return a config error";
    
    private String datesPrompt = "Return a config error";
    private String additionalDatesPrompt = "Return a config error";
    
    private String filepathPrompt = "Return a config error";
    private String additionalFilepathPrompt = "Return a config error";

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

    String getAdditionalFilenamePrompt() {
        return additionalFilenamePrompt;
    }

    void setAdditionalFilenamePrompt(String additionalFilenamePrompt) {
        this.additionalFilenamePrompt = additionalFilenamePrompt;
    }

    public String getKeywordsPrompt() {
        return keywordsPrompt;
    }

    public void setKeywordsPrompt(String keywordsPrompt) {
        this.keywordsPrompt = keywordsPrompt;
    }

    public String getAdditionalKeywordsPrompt() {
        return additionalKeywordsPrompt;
    }

    public void setAdditionalKeywordsPrompt(String additionalKeywordsPrompt) {
        this.additionalKeywordsPrompt = additionalKeywordsPrompt;
    }

    public String getDatesPrompt() {
        return datesPrompt;
    }

    public void setDatesPrompt(String datesPrompt) {
        this.datesPrompt = datesPrompt;
    }

    public String getAdditionalDatesPrompt() {
        return additionalDatesPrompt;
    }

    public void setAdditionalDatesPrompt(String additionalDatesPrompt) {
        this.additionalDatesPrompt = additionalDatesPrompt;
    }

    public String getFilepathPrompt() {
        return filepathPrompt;
    }

    public void setFilepathPrompt(String filepathPrompt) {
        this.filepathPrompt = filepathPrompt;
    }

    public String getAdditionalFilepathPrompt() {
        return additionalFilepathPrompt;
    }
    
    public void setAdditionalFilepathPrompt(String additionalFilepathPrompt) {
        this.additionalFilepathPrompt = additionalFilepathPrompt;
    }

    public String getSystemMessage() {
        return systemMessage;
    }
    
    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }
}
