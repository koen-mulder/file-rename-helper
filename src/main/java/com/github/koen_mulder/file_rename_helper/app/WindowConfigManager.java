package com.github.koen_mulder.file_rename_helper.app;

import java.awt.Rectangle;

/**
 * Config manager for the window configuration.
 * 
 * @see ConfigManager
 * @extends ConfigManager
 */
class WindowConfigManager extends ConfigManager<WindowConfig> {
    
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