/**
 * 
 */
package com.github.koen_mulder.file_rename_helper.app;

/**
 * The ConfigChangePublisher interface is implemented by classes that manage 
 * configuration and notify registered {@link IConfigChangeListener}s when a config value is changed.
 */
public interface IConfigChangePublisher {
    
    /**
     * Adds a listener that will be notified when a config value is changed
     * 
     */
    void addConfigChangeListener(IConfigChangeListener listener);

    /**
     * Removes a previously added listener.
     * 
     * @param listener The listener to be removed.
     */
    void removeConfigChangeListener(IConfigChangeListener listener);

}
