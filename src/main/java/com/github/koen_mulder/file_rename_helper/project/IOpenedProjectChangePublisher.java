package com.github.koen_mulder.file_rename_helper.project;

public interface IOpenedProjectChangePublisher {
    
    /**
     * Adds a listener to the list of listeners that will be notified when a project is opened or closed.
     *
     * @param listener The listener to add
     */
    void addOpenedProjectChangeListener(IOpenedProjectChangeListener listener);

    /**
     * Removes a listener from the list of listeners that will be notified when a project is opened or closed.
     *
     * @param listener The listener to remove
     */
    void removeOpenedProjectChangeListener(IOpenedProjectChangeListener listener);
}
