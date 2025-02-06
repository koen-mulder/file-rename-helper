package com.github.koen_mulder.file_rename_helper.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.github.koen_mulder.file_rename_helper.interfaces.ConfigChangeListener;
import com.github.koen_mulder.file_rename_helper.interfaces.ConfigChangePublisher;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

/**
 * ConfigManager is a Singleton class responsible for managing the application's
 * configuration. The configuration values are maintained in a thread-safe
 * manner, ensuring that updates and retrievals can occur safely from multiple
 * threads.
 * 
 * <p>
 * This class follows the Singleton design pattern, meaning that only one
 * instance of the ConfigManager is created and used throughout the application.
 * The instance is eagerly initialized when the class is loaded. The
 * configuration items are stored as atomic references, which provide
 * thread-safe access to the values.
 * </p>
 */
abstract class ConfigManager<T> implements ConfigChangePublisher {

    private final Gson gson;
    private String configFilePath;
    private boolean isConfigChanged = false;

    protected final T config;
    
    private ArrayList<ConfigChangeListener> listeners = Lists.newArrayList();

    // Private constructor to prevent instantiation
    protected ConfigManager(String configFilePath, Class<T> classOfT) {
        this.configFilePath = configFilePath;

        // Initialize default values
        gson = new Gson();
        config = loadConfig(classOfT);
    }
    
    protected abstract T getNewConfigInstance();

    // Load configuration from JSON file
    private T loadConfig(Class<T> classOfT) {
        try (FileReader reader = new FileReader(configFilePath)) {
            T fromJson = gson.fromJson(reader,classOfT);
            if (fromJson == null) {
                return getNewConfigInstance(); // Return default config
            }
            return fromJson;
        } catch (IOException e) {
            // TODO: Log error, take appropriate action like showing a dialog to the user
            // about how to proceed.
            System.out.println("Could not load config, using default values.");
            return getNewConfigInstance(); // Return default config
        }
    }

    // Save configuration to file
    public synchronized void saveConfig() {
        try (FileWriter writer = new FileWriter(configFilePath)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            System.out.println("Could not save config.");
        }
    }

    public boolean isConfigChanged() {
        return isConfigChanged;
    }
    
    protected void setConfigChanged() {
        isConfigChanged = true;
    }
    
    protected void setConfigChanged(EConfigIdentifier configId) {
        isConfigChanged = true;
        notifyConfigChangeListeners(configId);
    }

    @Override
    public void addConfigChangeListener(ConfigChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeConfigChangeListener(ConfigChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyConfigChangeListeners(EConfigIdentifier configId) {
        for (ConfigChangeListener listener : listeners) {
            listener.onConfigChanged(configId);
        }
    }
}