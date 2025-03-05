package com.github.koen_mulder.file_rename_helper.app;

import java.awt.Rectangle;

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
public class WindowConfigManager extends ConfigManager<WindowConfig> {
    
    // Eagerly initialize the Singleton instance
    private static final WindowConfigManager instance = new WindowConfigManager();

    private static final String CONFIG_FILE = "window_config.json";

    // Private constructor to prevent instantiation
    private WindowConfigManager() {
        super(CONFIG_FILE, WindowConfig.class);
    }

    // Public method to get the instance
    public static WindowConfigManager getInstance() {
        return instance;
    }
    
    @Override
    protected WindowConfig getNewConfigInstance() {
        return new WindowConfig();
    }

    public int getWindowExtendedState() {
        return config.getWindowExtendedState();
    }
    
    public void setWindowExtendedState(int extendedState) {
        if (extendedState != getWindowExtendedState()) {
            setConfigChanged();
            config.setWindowExtendedState(extendedState);
        }
    }
    
    public Rectangle getWindowBounds() {
        return config.getWindowBounds();
    }
    
    public void setWindowBounds(Rectangle newBounds) {
        if (newBounds != null && !newBounds.equals(getWindowBounds())) {
            setConfigChanged();
            config.setWindowBounds(newBounds);
        }
    }
    
    public int getSplitPaneDividerLocation() {
        return config.getSplitPaneDividerLocation();
    }
    
    public void setSplitPaneDividerLocation(int dividerLocation) {
        if (dividerLocation != getSplitPaneDividerLocation()) {
            config.setSplitPaneDividerLocation(dividerLocation);
            setConfigChanged();
        }
    }
}