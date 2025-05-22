package com.github.koen_mulder.file_rename_helper.app;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.github.koen_mulder.file_rename_helper.project.IOpenedProjectChangeListener;
import com.github.koen_mulder.file_rename_helper.project.Project;
import com.github.koen_mulder.file_rename_helper.project.ProjectController;

@SuppressWarnings("serial") // Same-version serialization only
public class MenuBar extends JMenuBar {

    private ApplicationController applicationController;
    private ProjectController projectController;

    private JMenuItem saveProjectMenuItem;
    private JMenuItem closeProjectMenuItem;

    private JMenuItem projectConfigMenuItem;
    private JMenuItem llmConfigMenuItem;
    private JMenuItem promptConfigMenuItem;
    
    private JFrame frame;

    public MenuBar(ApplicationController applicationController, JFrame frame) {
        this.applicationController = applicationController;
        this.frame = frame;
        
        projectController = applicationController.getProjectController();

        JMenu fileMenu = createFileMenu();
        add(fileMenu);

        JMenu configMenu = createConfigMenu();
        add(configMenu);
        addOpenedProjectListener();
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newProjectMenuItem = new JMenuItem("New Project...");
        newProjectMenuItem.addActionListener(e -> {
            projectController.createNewProject(frame);
        });
        fileMenu.add(newProjectMenuItem);

        JMenuItem openProjectMenuItem = new JMenuItem("Open Project...");
        openProjectMenuItem.addActionListener(e -> {
            projectController.openProject(frame);
        });
        fileMenu.add(openProjectMenuItem);

        saveProjectMenuItem = new JMenuItem("Save Project");
        saveProjectMenuItem.setEnabled(false);
        saveProjectMenuItem.addActionListener(e -> {
            projectController.saveProject(this);
        });
        fileMenu.add(saveProjectMenuItem);

        closeProjectMenuItem = new JMenuItem("Close Project");
        closeProjectMenuItem.setEnabled(false);
        closeProjectMenuItem.addActionListener(e -> {
            projectController.closeProject(this);
        });
        fileMenu.add(closeProjectMenuItem);

        JSeparator separator = new JSeparator();
        fileMenu.add(separator);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> {
            applicationController.closeApplication(frame, false);
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private JMenu createConfigMenu() {
        JMenu configMenu = new JMenu("Configuration");
        
        projectConfigMenuItem = new JMenuItem("Project");
        projectConfigMenuItem.setEnabled(false);
        // TODO: Add action listener for project configuration
        configMenu.add(projectConfigMenuItem);
        
        llmConfigMenuItem = new JMenuItem("LLM/Service");
        llmConfigMenuItem.setEnabled(false);
        // TODO: Add action listener for LLM configuration
        configMenu.add(llmConfigMenuItem);

        promptConfigMenuItem = new JMenuItem("Promt");
        promptConfigMenuItem.setEnabled(false);
        // TODO: Add action listener for prompt configuration
        configMenu.add(promptConfigMenuItem);
        
        return configMenu;
    }

    private void projectOpened() {
        saveProjectMenuItem.setEnabled(true);
        closeProjectMenuItem.setEnabled(true);
        
        //TODO: Enable configuration menu items after implementing the configuration dialogs
//        projectConfigMenuItem.setEnabled(true);
//        llmConfigMenuItem.setEnabled(true);
//        promptConfigMenuItem.setEnabled(true);
    }

    private void projectClosed() {
        saveProjectMenuItem.setEnabled(false);
        closeProjectMenuItem.setEnabled(false);
        
        //TODO: Disable configuration menu items after implementing the configuration dialogs
//        projectConfigMenuItem.setEnabled(false);
//        llmConfigMenuItem.setEnabled(false);
//        promptConfigMenuItem.setEnabled(false);
    }

    private void addOpenedProjectListener() {
        projectController.getOpenedProjectChangePublisher()
                .addOpenedProjectChangeListener(new IOpenedProjectChangeListener() {

            @Override
            public void onProjectOpened(Project project) {
                projectOpened();
            }

            @Override
            public void onProjectClosed(Project project) {
                projectClosed();
            }
        });
    }

}
