package com.github.koen_mulder.file_rename_helper.project;

import java.awt.Component;

import javax.swing.JFrame;

import com.github.koen_mulder.file_rename_helper.project.gui.NewProjectDialog;
import com.github.koen_mulder.file_rename_helper.project.gui.NewProjectDialog.NewProjectDialogFinishingState;

public class ProjectController {
    
    private OpenedProjectChangePublisher openedProjectChangePublisher;

    
    
    private ActiveProjectController activeProjectController = null;
    
    public ProjectController() {
        openedProjectChangePublisher = new OpenedProjectChangePublisher();
    }
    
    public IOpenedProjectChangePublisher getOpenedProjectChangePublisher() {
        return openedProjectChangePublisher;
    }
    
    public ActiveProjectController getActiveProjectController() {
        if (activeProjectController == null) {
            throw new IllegalStateException(
                    "No active project controller available. Please open a project first.");
        }
        return activeProjectController;
    }

    public void closeProject(Component parentComponent) {
        // TODO: Do checks to see if the project can be closed
        openedProjectChangePublisher
                .notifyProjectClosed(activeProjectController.getCurrentProject());
        activeProjectController = null;
    }

    public void saveProjectAs(Component parentComponent) {
        // TODO Auto-generated method stub
        
    }

    public void saveProject(Component parentComponent) {
        // TODO Auto-generated method stub
        
    }

    public void openProject(Component parentComponent) {
        // TODO: Actually open a project
        Project project = new Project();
        activeProjectController = new ActiveProjectController(project );
        openedProjectChangePublisher.notifyProjectOpened(project);
    }

    public void createNewProject(JFrame frame) {
        NewProjectDialog newProjectDialog = new NewProjectDialog(frame);
        if (newProjectDialog.showNewProjectDialog() == NewProjectDialogFinishingState.CREATE) {
            
            ProjectCreationData projectCreationData = newProjectDialog.getProjectCreationData();
            Project project = new Project(projectCreationData);
            ProjectService.saveNewProject(project);
            // TODO: Actually create a new project
            activeProjectController = new ActiveProjectController(project);
            openedProjectChangePublisher.notifyProjectOpened(project);
        }
        
    }
}
