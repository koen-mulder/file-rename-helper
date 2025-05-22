package com.github.koen_mulder.file_rename_helper.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.github.koen_mulder.file_rename_helper.processing.gui.FileProcessingPanel;
import com.github.koen_mulder.file_rename_helper.project.ActiveProjectController;
import com.github.koen_mulder.file_rename_helper.project.IOpenedProjectChangeListener;
import com.github.koen_mulder.file_rename_helper.project.Project;
import com.github.koen_mulder.file_rename_helper.renaming.ui.ViewAndRenamePanel;

/**
 * Main application window.
 */
@SuppressWarnings("serial") // Same-version serialization only
class ApplicationWindow extends JFrame {

    private static final String NO_ACTIVE_PROJECT_CARD = "No active project card";
    private static final String ACTIVE_PROJECT_CARD = "Active project card";
    
    private ApplicationController applicationController;
    
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    private Component noActiveProjectCard;
    private Component activeProjectCard;
    

    /**
     * Create the application.
     */
    public ApplicationWindow(ApplicationController applicationController) {
        
        this.applicationController = applicationController;
        
        // Window
        setTitle("File rename helper");
        setBounds(new Rectangle(0, 0, 1280, 720));
        setExtendedState(JFrame.NORMAL);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        
        // Menu bar
        setJMenuBar(new MenuBar(applicationController, this));
        
        noActiveProjectCard = createNoActiveProjectCard();

        // Active project/No active project card layout for main panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(noActiveProjectCard, NO_ACTIVE_PROJECT_CARD);
        add(cardPanel, BorderLayout.CENTER);

        addListeners();
    }
    
    public JPanel createNoActiveProjectCard() {
        JPanel noActiveProjectPanel = new JPanel(new BorderLayout());
        
        JLabel noActiveProjectLabel = new JLabel(
                "Open or create a project to start renaming files.");
        noActiveProjectLabel.setHorizontalAlignment(JLabel.CENTER);
        noActiveProjectLabel.setEnabled(false);
        noActiveProjectPanel.add(noActiveProjectLabel, BorderLayout.CENTER);
        
        return noActiveProjectPanel;
    }
    
    private JSplitPane createActiveProjectCard(ActiveProjectController activeProjectController) {
      // Panel for file processing
      FileProcessingPanel processListPanel = new FileProcessingPanel(activeProjectController);
      
      // View and rename split pane
      ViewAndRenamePanel viewAndRenameSplitPane = new ViewAndRenamePanel(
              activeProjectController, this);
      
      // Splitter for file processing and file view/renaming
      JSplitPane processListAndFileSplitPane = new JSplitPane();
      processListAndFileSplitPane.setDividerLocation(200);
      processListAndFileSplitPane.setLeftComponent(processListPanel);
      processListAndFileSplitPane.setRightComponent(viewAndRenameSplitPane);

      
      return processListAndFileSplitPane;
    }
    
    protected void showOpenedProject(Project project) {
        if (activeProjectCard != null) {
            cardPanel.remove(activeProjectCard);
        }
        
        ActiveProjectController activeProjectController = applicationController
                .getProjectController().getActiveProjectController();
        activeProjectCard = createActiveProjectCard(activeProjectController);
        cardPanel.add(activeProjectCard, ACTIVE_PROJECT_CARD);
        
        cardLayout.show(cardPanel, ACTIVE_PROJECT_CARD);
    }
    
    protected void showNoProjectPlaceholder(Project project) {
        cardLayout.show(cardPanel, NO_ACTIVE_PROJECT_CARD);
        if (activeProjectCard != null) {
            cardPanel.remove(activeProjectCard);
            activeProjectCard = null;
        }
    }
    
    private void addListeners() {
        applicationController.getProjectController().getOpenedProjectChangePublisher()
        .addOpenedProjectChangeListener(new IOpenedProjectChangeListener() {
            @Override
            public void onProjectOpened(Project project) {
                showOpenedProject(project);
            }

            @Override
            public void onProjectClosed(Project project) {
                showNoProjectPlaceholder(project);
            }
        });
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                applicationController.closeApplication(e.getWindow(), true);
            }
        });
    }
}
