package com.github.koen_mulder.file_rename_helper.project.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import com.github.koen_mulder.file_rename_helper.project.ProjectCreationData;

@SuppressWarnings("serial") // Same-version serialization only
public class NewProjectDialog extends JDialog {

    public enum NewProjectDialogFinishingState {
        CREATE, CANCEL
    }
    
    private JTextField workspaceLocationField;
    private JTextField archiveLocationField;

    private NewProjectDialogFinishingState finishingState = NewProjectDialogFinishingState.CANCEL;
    private ProjectCreationData result;
    
    public NewProjectDialog(Frame owner) {
        super(owner, "Create New Project");
        setModal(true);

        setLocationRelativeTo(owner); // Center relative to parent
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = createMainPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        pack();

    }
    
    public JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        
        JPanel workspaceLocationPanel = createWorkspaceLocationPanel();
        JPanel archiveLocationPanel = createArchiveLocationPanel();
        JPanel buttonPanel = createButtonPanel();
        
        GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
        gl_mainPanel.setHorizontalGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_mainPanel.createSequentialGroup().addContainerGap()
                        .addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_mainPanel.createSequentialGroup()
                                        .addComponent(workspaceLocationPanel,
                                                GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                                        .addContainerGap())
                                .addGroup(Alignment.TRAILING, gl_mainPanel.createSequentialGroup()
                                        .addComponent(archiveLocationPanel,
                                                GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                                        .addContainerGap())))
                .addComponent(buttonPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 550,
                        Short.MAX_VALUE));
        gl_mainPanel.setVerticalGroup(
                gl_mainPanel.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
                        gl_mainPanel.createSequentialGroup().addContainerGap()
                                .addComponent(workspaceLocationPanel, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(archiveLocationPanel, GroupLayout.PREFERRED_SIZE, 81,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED, 236, Short.MAX_VALUE)
                                .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

        mainPanel.setLayout(gl_mainPanel);
        
        return mainPanel;
    }

    public JPanel createWorkspaceLocationPanel() {
        JPanel workspaceLocationPanel = new JPanel();
        workspaceLocationPanel.setBorder(BorderFactory.createTitledBorder("Workspace location"));

        JLabel workspaceLocationLabel = new JLabel("Location:");

        workspaceLocationField = new JTextField();
        workspaceLocationField.setColumns(10);

        JButton workspaceLocationButton = new JButton(new BrowseWorkspaceAction(this));

        JLabel workspaceLocationInfo = new JLabel(
                "<html>Select the directory where the File Rename Helper project files will be stored. This includes LLM prompts, application settings, and RAG-related files.</html>");
        workspaceLocationInfo.setVerticalAlignment(SwingConstants.TOP);
        GroupLayout gl_workspaceLocationPanel = new GroupLayout(workspaceLocationPanel);
        gl_workspaceLocationPanel.setHorizontalGroup(
            gl_workspaceLocationPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(Alignment.LEADING, gl_workspaceLocationPanel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_workspaceLocationPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_workspaceLocationPanel.createSequentialGroup()
                            .addGap(10)
                            .addComponent(workspaceLocationInfo, GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE))
                        .addGroup(gl_workspaceLocationPanel.createSequentialGroup()
                            .addComponent(workspaceLocationLabel)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(workspaceLocationField, GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(workspaceLocationButton)))
                    .addContainerGap())
        );
        gl_workspaceLocationPanel.setVerticalGroup(
            gl_workspaceLocationPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_workspaceLocationPanel.createSequentialGroup()
                    .addGroup(gl_workspaceLocationPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(workspaceLocationLabel)
                        .addComponent(workspaceLocationField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(workspaceLocationButton))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(workspaceLocationInfo, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addContainerGap())
        );
        workspaceLocationPanel.setLayout(gl_workspaceLocationPanel);

        return workspaceLocationPanel;
    }

    public JPanel createArchiveLocationPanel() {
        JPanel archiveLocationPanel = new JPanel();
        archiveLocationPanel.setBorder(BorderFactory.createTitledBorder("Archive location"));

        JLabel archiveLocationLabel = new JLabel("Location:");

        archiveLocationField = new JTextField();
        archiveLocationField.setColumns(10);

        JButton archiveLocationButton = new JButton(new BrowseArchiveAction(this));

        JLabel archiveLocationInfo = new JLabel(
                "Archive location. Used for suggesting new file location.");
        archiveLocationInfo.setVerticalAlignment(SwingConstants.TOP);
        GroupLayout gl_archiveLocationPanel = new GroupLayout(archiveLocationPanel);
        gl_archiveLocationPanel.setHorizontalGroup(
            gl_archiveLocationPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(Alignment.LEADING, gl_archiveLocationPanel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_archiveLocationPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_archiveLocationPanel.createSequentialGroup()
                            .addGap(10)
                            .addComponent(archiveLocationInfo, GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE))
                        .addGroup(gl_archiveLocationPanel.createSequentialGroup()
                            .addComponent(archiveLocationLabel)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(archiveLocationField, GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(archiveLocationButton)))
                    .addContainerGap())
        );
        gl_archiveLocationPanel.setVerticalGroup(
            gl_archiveLocationPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_archiveLocationPanel.createSequentialGroup()
                    .addGroup(gl_archiveLocationPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(archiveLocationLabel)
                        .addComponent(archiveLocationField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(archiveLocationButton))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(archiveLocationInfo, GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addContainerGap())
        );
        archiveLocationPanel.setLayout(gl_archiveLocationPanel);

        return archiveLocationPanel;
    }

    public JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();

        JSeparator separator = new JSeparator();

        JButton cancelButton = new JButton(new CancelAction());

        JButton createProjectButton = new JButton(new CreateAction());
        
        GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
        gl_buttonPanel.setHorizontalGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
                .addComponent(separator, GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addGroup(Alignment.TRAILING,
                        gl_buttonPanel.createSequentialGroup().addContainerGap(356, Short.MAX_VALUE)
                                .addComponent(createProjectButton)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(cancelButton).addContainerGap()));
        gl_buttonPanel.setVerticalGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_buttonPanel.createSequentialGroup().addGap(2)
                        .addComponent(separator, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(gl_buttonPanel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(cancelButton).addComponent(createProjectButton))
                        .addContainerGap(22, Short.MAX_VALUE)));
        buttonPanel.setLayout(gl_buttonPanel);

        return buttonPanel;
    }
    
    public ProjectCreationData getProjectCreationData() {
        return result;
    }
    
    /**
     * Helper static method to show the dialog and get results.
     */
    public NewProjectDialogFinishingState showNewProjectDialog() {
        setVisible(true); // This call blocks until the dialog is disposed
        return finishingState;
    }
    
    private class BrowseWorkspaceAction extends AbstractAction {

        private JFileChooser fileChooser;
        
        private Component parent;
        
        public BrowseWorkspaceAction(Component parent) {
            putValue(Action.NAME, "Browse...");
            putValue(Action.SHORT_DESCRIPTION, "Select a workspace location for the project.");
            
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setMultiSelectionEnabled(false);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                workspaceLocationField.setText(selectedFile.getAbsolutePath());
                
                //TODO: Add logic to check if the selected file is a valid workspace location
            }
        }
    }
    
    private class BrowseArchiveAction extends AbstractAction {

        private JFileChooser fileChooser;
        
        private Component parent;
        
        public BrowseArchiveAction(Component parent) {
            putValue(Action.NAME, "Browse...");
            putValue(Action.SHORT_DESCRIPTION, "Select an archive location for the project.");
            
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setMultiSelectionEnabled(false);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                archiveLocationField.setText(selectedFile.getAbsolutePath());
            }
        }
    }
    
    private class CancelAction extends AbstractAction {
        
        public CancelAction() {
            putValue(Action.NAME, "Cancel");
            putValue(Action.SHORT_DESCRIPTION, "Cancel project creation and close the dialog.");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            finishingState = NewProjectDialogFinishingState.CANCEL;
            dispose();
        }
    }
    
    private class CreateAction extends AbstractAction {
        
        public CreateAction() {
            putValue(Action.NAME, "Create project");
            putValue(Action.SHORT_DESCRIPTION, "Create the project and close the dialog.");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            finishingState = NewProjectDialogFinishingState.CREATE;
            
            // TODO: Add logic to create the CreateProjectData object
            result = new ProjectCreationData(Path.of(workspaceLocationField.getText()),
                    Path.of(archiveLocationField.getText()));
            
            dispose();
        }
    }
}
