/**
 * 
 */
package com.github.koen_mulder.file_rename_helper.interfaces;

import com.github.koen_mulder.file_rename_helper.config.EConfigIdentifier;

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

    /**
     * Notifies all registered listeners of the config value change event.
     * 
     * @param configId The id of the config item triggering the change event
     */
    void notifyConfigChangeListeners(EConfigIdentifier configId);
}
