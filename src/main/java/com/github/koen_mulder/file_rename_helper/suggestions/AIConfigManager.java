package com.github.koen_mulder.file_rename_helper.suggestions;

import com.github.koen_mulder.file_rename_helper.app.ConfigManager;

public class AIConfigManager extends ConfigManager<AIConfig> {
    
    // Eagerly initialize the Singleton instance
    private static final AIConfigManager instance = new AIConfigManager();

    private static final String CONFIG_FILE = "ai_config.json";

    // Private constructor to prevent instantiation
    private AIConfigManager() {
        super(CONFIG_FILE, AIConfig.class);
    }

    // Public method to get the instance
    public static AIConfigManager getInstance() {
        return instance;
    }
    
    @Override
    protected AIConfig getNewConfigInstance() {
        return new AIConfig();
    }
    
    public String getModelName() {
        return config.getModelName();
    }
    
    public void setModelName(String modelName) {
        if (!getModelName().equals(modelName)) {
            config.setModelName(modelName);
            notifyConfigChangeListeners(EConfigIdentifier.MODEL_NAME);
        }
    }
   
    public String getOllamaEndpoint() {
        return config.getOllamaEndpoint();
    }
    
    public void setOllamaEndpoint(String ollamaEndpoint) {
        if (!getOllamaEndpoint().equals(ollamaEndpoint)) {
            config.setOllamaEndpoint(ollamaEndpoint);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.OLLAMA_ENDPOINT);
        }
    }
    
    public String getEmbeddingStoreFile() {
        return config.getEmbeddingStoreFile();
    }
    
    public void setEmbeddingStoreFile(String filePath) {
        if (!getEmbeddingStoreFile().equals(filePath)) {
            config.setEmbeddingStoreFile(filePath);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.EMBEDDING_STORE_FILE_PATH);
        }
    }

    public String getFilenamePrompt() {
        return config.getFilenamePrompt();
    }
    
    public void setFilenamePrompt(String prompt) {
        if (!config.getFilenamePrompt().equals(prompt)) {
            config.setFilenamePrompt(prompt);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.FILENAME_PROMPT);
        }
    }

    public String getAdditionalFilenamePrompt() {
        return config.getAdditionalFilenamePrompt();
    }
    
    public void setAdditionalFilenamePrompt(String prompt) {
        if (!config.getAdditionalFilenamePrompt().equals(prompt)) {
            config.setAdditionalFilenamePrompt(prompt);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.ADDITIONAL_FILENAME_PROMPT);
        }
    }

}