package com.github.koen_mulder.file_rename_helper.project;

public interface IOpenedProjectChangeListener {

    /**
     * This method will be called when a project is opened in the {@link IOpenedProjectChangePublisher}.
     * 
     * @param project The project that was opened
     */
    void onProjectOpened(Project project);

    /**
     * This method will be called when a project is closed in the {@link IOpenedProjectChangePublisher}.
     * 
     * @param project The project that was closed
     */
    void onProjectClosed(Project project);
}
