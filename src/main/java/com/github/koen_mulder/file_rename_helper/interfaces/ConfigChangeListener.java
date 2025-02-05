/**
 * 
 */
package com.github.koen_mulder.file_rename_helper.interfaces;

import com.github.koen_mulder.file_rename_helper.config.EConfigIdentifier;

/**
 * The ConfigChangeListener interface is implemented by components that need to
 * react to config change events of a {@link ConfigChangePublisher}.
 */
public interface ConfigChangeListener {
    /**
     * This method will be called when a config item is changed in the {@link ConfigChangePublisher}.
     * 
     * @param configId The id of the config item triggering the change event
     */
    void onConfigChanged(EConfigIdentifier configId);
}
