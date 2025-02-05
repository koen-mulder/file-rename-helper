package com.github.koen_mulder.file_rename_helper.config;

import java.awt.Rectangle;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
public class WindowConfigManager {
    // Eagerly initialize the Singleton instance
    private static final WindowConfigManager instance = new WindowConfigManager();

    private static final String CONFIG_FILE = "config.json";

    private WindowConfig config;
    private Gson gson;
    
    private boolean isConfigChanged = false;

    // Prompt config

    // Private constructor to prevent instantiation
    private WindowConfigManager() {
        // Initialize default values
        gson = new Gson();
        config = loadConfig();
    }

    // Public method to get the instance
    public static WindowConfigManager getInstance() {
        return instance;
    }

    // Load configuration from JSON file
    private WindowConfig loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            WindowConfig fromJson = gson.fromJson(reader, WindowConfig.class);
            if (fromJson == null) {
                return new WindowConfig(); // Return default config
            }
            return fromJson;
        } catch (IOException e) {
            //TODO: Log error, take appropriate action like showing a dialog to the user about how to proceed.
            System.out.println("Could not load config, using default values.");
            return new WindowConfig(); // Return default config
        }
    }

    // Save configuration to file
    public synchronized void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            System.out.println("Could not save config.");
        }
    }

    public int getWindowExtendedState() {
        return config.getWindowExtendedState();
    }
    
    public void setWindowExtendedState(int extendedState) {
        isConfigChanged = true;
        config.setWindowExtendedState(extendedState);
    }
    
    public Rectangle getWindowBounds() {
        return config.getWindowBounds();
    }
    
    public void setWindowBounds(Rectangle newBounds) {
        isConfigChanged = true;
        config.setWindowBounds(newBounds);
    }
    
    public int getSplitPaneDividerLocation() {
        return config.getSplitPaneDividerLocation();
    }
    
    public void setSplitPaneDividerLocation(int dividerLocation) {
        isConfigChanged = true;
        config.setSplitPaneDividerLocation(dividerLocation);
    }
    
    public boolean isConfigChanged() {
        return isConfigChanged;
    }
}