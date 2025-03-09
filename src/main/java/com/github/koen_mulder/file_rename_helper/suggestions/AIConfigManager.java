package com.github.koen_mulder.file_rename_helper.suggestions;

import com.github.koen_mulder.file_rename_helper.app.ConfigManager;
import com.github.koen_mulder.file_rename_helper.app.EConfigIdentifier;

/**
 * Config manager for AI-related settings.
 */
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

    public String getKeywordsPrompt() {
        return config.getKeywordsPrompt();
    }
    
    public void setKeywordsPrompt(String prompt) {
        if (!config.getKeywordsPrompt().equals(prompt)) {
            config.setKeywordsPrompt(prompt);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.KEYWORDS_PROMPT);
        }
    }

    public String getAdditionalKeywordsPrompt() {
        return config.getAdditionalKeywordsPrompt();
    }
    
    public void setAdditionalKeywordsPrompt(String prompt) {
        if (!config.getAdditionalKeywordsPrompt().equals(prompt)) {
            config.setAdditionalKeywordsPrompt(prompt);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.ADDITIONAL_KEYWORDS_PROMPT);
        }
    }

    public String getDatesPrompt() {
        return config.getDatesPrompt();
    }
    
    public void setDatesPrompt(String prompt) {
        if (!config.getDatesPrompt().equals(prompt)) {
            config.setDatesPrompt(prompt);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.DATES_PROMPT);
        }
    }

    public String getAdditionalDatesPrompt() {
        return config.getAdditionalDatesPrompt();
    }
    
    public void setAdditionalDatesPrompt(String prompt) {
        if (!config.getAdditionalDatesPrompt().equals(prompt)) {
            config.setAdditionalDatesPrompt(prompt);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.ADDITIONAL_DATES_PROMPT);
        }
    }

    public String getFilepathPrompt() {
        return config.getFilepathPrompt();
    }
    
    public void setFilepathPrompt(String prompt) {
        if (!config.getFilepathPrompt().equals(prompt)) {
            config.setFilepathPrompt(prompt);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.FILEPATH_PROMPT);
        }
    }

    public String getAdditionalFilepathPrompt() {
        return config.getAdditionalFilepathPrompt();
    }
    
    public void setAdditionalFilepathPrompt(String prompt) {
        if (!config.getAdditionalFilepathPrompt().equals(prompt)) {
            config.setAdditionalFilepathPrompt(prompt);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.ADDITIONAL_FILEPATH_PROMPT);
        }
    }

    public String getSystemMessage() {
        return config.getSystemMessage();
    }
    
    public void setSystemMessage(String message) {
        if (!config.getSystemMessage().equals(message)) {
            config.setSystemMessage(message);
            setConfigChanged();
            notifyConfigChangeListeners(EConfigIdentifier.SYSTEM_MESSAGE);
        }
    }

}