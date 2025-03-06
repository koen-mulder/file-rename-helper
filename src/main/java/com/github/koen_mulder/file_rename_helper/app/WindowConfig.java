package com.github.koen_mulder.file_rename_helper.app;

import java.awt.Rectangle;

import javax.swing.JFrame;

/**
 * Configuration items related to Window configuration. Used for storing and retrieving
 * configuration using the {@link WindowConfigManager}.
 */
class WindowConfig {

    //TODO: Make config items Atomic reference to manage thread-safe mutable objects
    //TODO: Make names of method arguments consistent with the rest of the codebase
    
    // GUI config
    private int extendedState = JFrame.NORMAL;
    private Rectangle windowBounds = new Rectangle(100, 100, 1280, 720);
    private int splitPaneDividerLocation = 600;

    public void setWindowExtendedState(int extendedState) {
        this.extendedState = extendedState;
    }

    int getWindowExtendedState() {
        return extendedState;
    }
    
    Rectangle getWindowBounds() {
        return windowBounds;    }
    
    void setWindowBounds(Rectangle newBounds) {
        windowBounds = newBounds;
    }

    int getSplitPaneDividerLocation() {
        return splitPaneDividerLocation;
    }

    void setSplitPaneDividerLocation(int dividerLocation) {
        splitPaneDividerLocation = dividerLocation;
    }
}
