package com.github.koen_mulder.file_rename_helper.app;

import java.awt.Component;

import com.github.koen_mulder.file_rename_helper.project.ProjectController;

public class ApplicationController {

    private final ProjectController projectController;
    
    public ApplicationController() {
        projectController = new ProjectController();
    }
    
    public ProjectController getProjectController() {
        return projectController;
    }
    
    public void closeApplication(Component parentComponent, boolean force) {
        projectController.closeProject(parentComponent, force);
        System.exit(0);
    }
}
