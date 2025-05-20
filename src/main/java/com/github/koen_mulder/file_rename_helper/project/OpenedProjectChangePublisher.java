package com.github.koen_mulder.file_rename_helper.project;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

public class OpenedProjectChangePublisher implements IOpenedProjectChangePublisher {

    private final List<IOpenedProjectChangeListener> listeners = Lists.newArrayList();
    
    @Override
    public void addOpenedProjectChangeListener(IOpenedProjectChangeListener listener) {
        listeners.add(listener);        
    }

    @Override
    public void removeOpenedProjectChangeListener(IOpenedProjectChangeListener listener) {
        listeners.remove(listener);        
    }
    
    void notifyProjectOpened(Project project) {
        for (IOpenedProjectChangeListener listener : listeners) {
            listener.onProjectOpened(project);
        }
    }

    void notifyProjectClosed(Project project) {
        for (IOpenedProjectChangeListener listener : listeners) {
            listener.onProjectClosed(project);
        }
    }
}
