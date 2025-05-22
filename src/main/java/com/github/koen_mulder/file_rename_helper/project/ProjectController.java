package com.github.koen_mulder.file_rename_helper.project;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.koen_mulder.file_rename_helper.project.gui.NewProjectDialog;
import com.github.koen_mulder.file_rename_helper.project.gui.NewProjectDialog.NewProjectDialogFinishingState;

public class ProjectController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

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
        closeProject(parentComponent, false);
    }
    
    public void closeProject(Component parentComponent, boolean force) {
        if (activeProjectController == null) {
            // Nothing to close
            return;
        }

        Project currentProject = activeProjectController.getCurrentProject();
        
        if (currentProject.isChanged()) {
            int closeOption = force ? JOptionPane.YES_NO_OPTION: JOptionPane.YES_NO_CANCEL_OPTION;
            int result = JOptionPane.showConfirmDialog(parentComponent,
                    "The project has unsaved changes. Do you want to save the project before closing?",
                    "Unsaved changes", closeOption);
            if (result == JOptionPane.YES_OPTION) {
                saveProject(parentComponent);
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        
        openedProjectChangePublisher
                .notifyProjectClosed(activeProjectController.getCurrentProject());
        activeProjectController = null;
    }

    public void saveProject(Component parentComponent) {
        if (activeProjectController == null) {
            throw new IllegalStateException(
                    "State exception: Trying to save a project when no project is active.");
        }
        
        Project currentProject = activeProjectController.getCurrentProject();
        try {
            ProjectService.saveProject(currentProject);
            logger.info("Project saved: {}", currentProject);
        } catch (ProjectException e) {
            JOptionPane.showMessageDialog(parentComponent, buildErrorMessage(e),
                    "Error saving project", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openProject(Component parentComponent) {
        // Check if there is an active project and if it has unsaved changes
        if (activeProjectController != null) {
            closeProject(parentComponent);
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a project to open");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);
        // Filter to only show PDF files
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return String.format("File rename helper project (%s)",
                        ProjectService.PROJECT_FILE_NAME);
            }

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().equals(ProjectService.PROJECT_FILE_NAME);
            }
        });
        
        if (fileChooser.showOpenDialog(parentComponent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
                Project project;
                try {
                    project = ProjectService.openProject(selectedFile);
                    activeProjectController = new ActiveProjectController(project);
                    openedProjectChangePublisher.notifyProjectOpened(project);
                    logger.info("Project opened: {}", project);
                } catch (ProjectException e) {
                    JOptionPane.showMessageDialog(parentComponent, buildErrorMessage(e),
                            "Error opening project", JOptionPane.ERROR_MESSAGE);
                }
        }
    }

    public void createNewProject(JFrame frame) {
        NewProjectDialog newProjectDialog = new NewProjectDialog(frame);
        if (newProjectDialog.showNewProjectDialog() == NewProjectDialogFinishingState.CREATE) {
            
            ProjectCreationData projectCreationData = newProjectDialog.getProjectCreationData();
            Project project = new Project(projectCreationData);
            try {
                ProjectService.saveNewProject(project);
                activeProjectController = new ActiveProjectController(project);
                openedProjectChangePublisher.notifyProjectOpened(project);
                logger.info("New project created: {}", project);
            } catch (ProjectException e) {
                JOptionPane.showMessageDialog(frame, buildErrorMessage(e),
                        "Error creating new project", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String buildErrorMessage(ProjectException e) {
        String message;
        if (e.getCause() != null) {
            message = String.format("<html><p>%s</p><p></p><p>Reason: %s</p></html>",
                    e.getMessage(), e.getCause().getMessage());
        } else {
            message = String.format("<html><p>%s</p></html>", e.getMessage());
        }
        return message;
    }
}
