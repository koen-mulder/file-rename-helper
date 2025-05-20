package com.github.koen_mulder.file_rename_helper.app;

import com.github.koen_mulder.file_rename_helper.project.ProjectController;

public class ApplicationController {

    private final ProjectController projectController;
    
    public ApplicationController() {
        projectController = new ProjectController();
    }
    
    public ProjectController getProjectController() {
        return projectController;
    }
    
    public void closeApplication() {
        // TODO: Handle proper closing of the application (e.g. saving state, etc.)
        System.exit(0);
    }
}
