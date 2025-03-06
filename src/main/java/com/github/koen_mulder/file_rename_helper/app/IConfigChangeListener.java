/**
 * 
 */
package com.github.koen_mulder.file_rename_helper.app;

/**
 * The ConfigChangeListener interface is implemented by components that need to
 * react to config change events of a {@link IConfigChangePublisher}.
 */
public interface IConfigChangeListener extends java.util.EventListener{
    /**
     * This method will be called when a config item is changed in the {@link IConfigChangePublisher}.
     * 
     * @param configId The id of the config item triggering the change event
     */
    void onConfigChanged(EConfigIdentifier configId);
}
