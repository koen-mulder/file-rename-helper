package com.github.koen_mulder.file_rename_helper.suggestions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial") // Same-version serialization only
public class ConfigurationPanel extends JPanel {

    private AIConfigManager aiConfigManager;

    private JTextField modelNameField;
    private JTextField ollamaEndpointField;
    private JTextField embeddingStoreField;

    private final Action applyConfigChangesAction = new ApplyConfigChangesSwingAction();

    private JTextArea systemMessageField;

    private JTextArea filenamePromptField;

    private JTextArea additionalFilenamePromptField;

    private JTextArea keywordsPromptField;

    private JTextArea additionalKeywordsPromptField;

    private JTextArea datesPromptField;

    private JTextArea additionalDatesPromptField;

    private JTextArea filepathPromptField;

    private JTextArea additionalFilepathPromptField;

    public ConfigurationPanel() {
        aiConfigManager = AIConfigManager.getInstance();

        JButton saveChangesButton = new JButton("Apply config changes and save config to file for next sessions");
        saveChangesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyConfigChangesAction.actionPerformed(e);
                aiConfigManager.saveConfig();
            }
        });

        JButton applyChangesButton = new JButton("Apply config changes to current session");
        applyChangesButton.setAction(applyConfigChangesAction);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getVerticalScrollBar().setUnitIncrement(7);

        GroupLayout gl_configurationPanel = new GroupLayout(this);
        gl_configurationPanel.setHorizontalGroup(
            gl_configurationPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_configurationPanel.createSequentialGroup()
                    .addGap(47)
                    .addComponent(applyChangesButton)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(saveChangesButton)
                    .addContainerGap())
                .addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );
        gl_configurationPanel.setVerticalGroup(
            gl_configurationPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_configurationPanel.createSequentialGroup()
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(gl_configurationPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(applyChangesButton)
                        .addComponent(saveChangesButton))
                    .addContainerGap())
        );
        
        JPanel panel_1 = new JPanel();
        scrollPane.setViewportView(panel_1);
        
                JPanel modelConfigPanel = new JPanel();
                modelConfigPanel.setBorder(BorderFactory.createTitledBorder("Model configuration"));
                
                        JLabel modelNameLabel = new JLabel("Model name");
                        
                                modelNameField = new JTextField();
                                modelNameField.setText(aiConfigManager.getModelName());
                                modelNameField.setColumns(10);
                                
                                        JLabel ollamaEndpointLabel = new JLabel("Ollama endpoint");
                                        
                                                JLabel embeddingStoreLabel = new JLabel("Embedding store");
                                                
                                                        ollamaEndpointField = new JTextField();
                                                        ollamaEndpointField.setText(aiConfigManager.getOllamaEndpoint());
                                                        ollamaEndpointField.setColumns(10);
                                                        
                                                                embeddingStoreField = new JTextField();
                                                                embeddingStoreField.setText(aiConfigManager.getEmbeddingStoreFile());
                                                                embeddingStoreField.setColumns(10);
                                                                
                                                                        JButton embeddingStoreButton = new JButton("Select file");
                                                                        GroupLayout gl_modelConfigPanel = new GroupLayout(modelConfigPanel);
                                                                        gl_modelConfigPanel.setHorizontalGroup(gl_modelConfigPanel.createParallelGroup(Alignment.LEADING)
                                                                                .addGroup(gl_modelConfigPanel.createSequentialGroup().addGroup(gl_modelConfigPanel
                                                                                        .createParallelGroup(Alignment.LEADING)
                                                                                        .addGroup(gl_modelConfigPanel.createSequentialGroup().addContainerGap()
                                                                                                .addComponent(ollamaEndpointLabel))
                                                                                        .addGroup(gl_modelConfigPanel.createSequentialGroup().addContainerGap()
                                                                                                .addGroup(gl_modelConfigPanel.createParallelGroup(Alignment.LEADING)
                                                                                                        .addComponent(embeddingStoreLabel).addComponent(modelNameLabel))
                                                                                                .addGap(18)
                                                                                                .addGroup(gl_modelConfigPanel.createParallelGroup(Alignment.LEADING)
                                                                                                        .addGroup(gl_modelConfigPanel.createSequentialGroup()
                                                                                                                .addComponent(embeddingStoreField, GroupLayout.DEFAULT_SIZE, 684,
                                                                                                                        Short.MAX_VALUE)
                                                                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                                                                .addComponent(embeddingStoreButton))
                                                                                                        .addComponent(modelNameField, GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                                                                                                        .addComponent(ollamaEndpointField, GroupLayout.DEFAULT_SIZE, 769,
                                                                                                                Short.MAX_VALUE))))
                                                                                        .addContainerGap()));
                                                                        gl_modelConfigPanel
                                                                                .setVerticalGroup(
                                                                                        gl_modelConfigPanel.createParallelGroup(Alignment.LEADING)
                                                                                                .addGroup(gl_modelConfigPanel.createSequentialGroup().addContainerGap()
                                                                                                        .addGroup(gl_modelConfigPanel.createParallelGroup(Alignment.BASELINE)
                                                                                                                .addComponent(modelNameLabel).addComponent(ollamaEndpointField,
                                                                                                                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                                                                                        GroupLayout.PREFERRED_SIZE))
                                                                                                        .addPreferredGap(ComponentPlacement.RELATED)
                                                                                                        .addGroup(gl_modelConfigPanel.createParallelGroup(Alignment.BASELINE)
                                                                                                                .addComponent(ollamaEndpointLabel)
                                                                                                                .addComponent(modelNameField, GroupLayout.PREFERRED_SIZE,
                                                                                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                                                        .addPreferredGap(ComponentPlacement.RELATED)
                                                                                                        .addGroup(gl_modelConfigPanel.createParallelGroup(Alignment.BASELINE)
                                                                                                                .addComponent(embeddingStoreLabel)
                                                                                                                .addComponent(embeddingStoreField, GroupLayout.PREFERRED_SIZE,
                                                                                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(embeddingStoreButton))
                                                                                                        .addContainerGap(58, Short.MAX_VALUE)));
                                                                        modelConfigPanel.setLayout(gl_modelConfigPanel);
        
                JPanel panel = new JPanel();
                panel.setBorder(new TitledBorder(null, "Filename prompt configuration", TitledBorder.LEADING, TitledBorder.TOP,
                        null, null));
                
                // System Message configuration
                JLabel systemMessageLabel = new JLabel("System message:");
                JScrollPane systemMessageScrollPane = new JScrollPane();
                systemMessageField = new JTextArea(aiConfigManager.getSystemMessage());
                systemMessageField.setWrapStyleWord(true);
                systemMessageField.setLineWrap(true);
                systemMessageScrollPane.setViewportView(systemMessageField);
                
                // Filename prompt configuration
                JLabel filenamePromptLabel = new JLabel("Filename prompt:");
                JScrollPane filenamePromptScrollPane = new JScrollPane();
                filenamePromptField = new JTextArea(aiConfigManager.getFilenamePrompt());
                filenamePromptField.setLineWrap(true);
                filenamePromptField.setWrapStyleWord(true);
                filenamePromptScrollPane.setViewportView(filenamePromptField);
                
                // Filename additional prompt configuration
                JLabel additionalFilenamePromptLabel = new JLabel("Additional filename prompt:");
                JScrollPane additionalFilenamePromptScrollPane = new JScrollPane();
                additionalFilenamePromptField = new JTextArea(aiConfigManager.getAdditionalFilenamePrompt());
                additionalFilenamePromptField.setLineWrap(true);
                additionalFilenamePromptField.setWrapStyleWord(true);
                additionalFilenamePromptScrollPane.setViewportView(additionalFilenamePromptField);
                
                // Keywords prompt configuration
                JLabel keywordsPromptLabel = new JLabel("Keywords prompt:");
                JScrollPane keywordsPromptScrollPane = new JScrollPane();
                keywordsPromptField = new JTextArea(aiConfigManager.getKeywordsPrompt());
                keywordsPromptField.setLineWrap(true);
                keywordsPromptField.setWrapStyleWord(true);
                keywordsPromptScrollPane.setViewportView(keywordsPromptField);
                
                // Additional keywords prompt configuration
                JLabel additionalKeywordsPromptLabel = new JLabel("Additional keywords prompt:");
                JScrollPane additionalKeywordsPromptScrollPane = new JScrollPane();
                additionalKeywordsPromptField = new JTextArea(aiConfigManager.getAdditionalKeywordsPrompt());
                additionalKeywordsPromptField.setLineWrap(true);
                additionalKeywordsPromptField.setWrapStyleWord(true);
                additionalKeywordsPromptScrollPane.setViewportView(additionalKeywordsPromptField);
                
                // Dates prompt configuration
                JLabel datesPromptLabel = new JLabel("Dates prompt:");
                JScrollPane datesPromptScrollPane = new JScrollPane();
                datesPromptField = new JTextArea(aiConfigManager.getDatesPrompt());
                datesPromptField.setLineWrap(true);
                datesPromptField.setWrapStyleWord(true);
                datesPromptScrollPane.setViewportView(datesPromptField);
                
                // Additional dates prompt configuration
                JLabel additionalDatesPromptLabel = new JLabel("Additional dates prompt:");
                JScrollPane additionalDatesPromptScrollPane = new JScrollPane();
                additionalDatesPromptField = new JTextArea(aiConfigManager.getAdditionalDatesPrompt());
                additionalDatesPromptField.setLineWrap(true);
                additionalDatesPromptField.setWrapStyleWord(true);
                additionalDatesPromptScrollPane.setViewportView(additionalDatesPromptField);
                
                // Filepath prompt configuration
                JLabel filepathPromptLabel = new JLabel("Filepath prompt:");
                JScrollPane filepathPromptScrollPane = new JScrollPane();
                filepathPromptField = new JTextArea(aiConfigManager.getFilepathPrompt());
                filepathPromptField.setLineWrap(true);
                filepathPromptField.setWrapStyleWord(true);
                filepathPromptScrollPane.setViewportView(filepathPromptField);
                
                // Additional filepath prompt configuration
                JLabel additionalFilepathPromptLabel = new JLabel("Additional filepath prompt:");
                JScrollPane additionalFilepathPromptScrollPane = new JScrollPane();
                additionalFilepathPromptField = new JTextArea(aiConfigManager.getAdditionalFilepathPrompt());
                additionalFilepathPromptField.setLineWrap(true);
                additionalFilepathPromptField.setWrapStyleWord(true);
                additionalFilepathPromptScrollPane.setViewportView(additionalFilepathPromptField);
                
                   GroupLayout gl_panel = new GroupLayout(panel);
                   gl_panel.setHorizontalGroup(
                       gl_panel.createParallelGroup(Alignment.LEADING)
                           .addGroup(gl_panel.createSequentialGroup()
                               .addContainerGap()
                               .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                                   .addComponent(systemMessageLabel)
                                   .addComponent(systemMessageScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                                   .addComponent(filenamePromptLabel)
                                   .addComponent(filenamePromptScrollPane, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                                   .addComponent(additionalFilenamePromptLabel)
                                   .addComponent(additionalFilenamePromptScrollPane, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                                   .addComponent(keywordsPromptLabel)
                                   .addComponent(keywordsPromptScrollPane, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                                   .addComponent(additionalKeywordsPromptLabel)
                                   .addComponent(additionalKeywordsPromptScrollPane, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                                   .addComponent(datesPromptLabel)
                                   .addComponent(datesPromptScrollPane, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                                   .addComponent(additionalDatesPromptLabel)
                                   .addComponent(additionalDatesPromptScrollPane, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                                   .addComponent(filepathPromptLabel)
                                   .addComponent(filepathPromptScrollPane, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                                   .addComponent(additionalFilepathPromptLabel)
                                   .addComponent(additionalFilepathPromptScrollPane, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)

                                   )
                               .addContainerGap())
                   );
                   gl_panel.setVerticalGroup(
                       gl_panel.createParallelGroup(Alignment.LEADING)
                           .addGroup(gl_panel.createSequentialGroup()
                               .addContainerGap()
                               .addComponent(systemMessageLabel)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(systemMessageScrollPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(filenamePromptLabel)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(filenamePromptScrollPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(additionalFilenamePromptLabel)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(additionalFilenamePromptScrollPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(keywordsPromptLabel)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(keywordsPromptScrollPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(additionalKeywordsPromptLabel)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(additionalKeywordsPromptScrollPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(datesPromptLabel)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(datesPromptScrollPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(additionalDatesPromptLabel)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(additionalDatesPromptScrollPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(filepathPromptLabel)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(filepathPromptScrollPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(additionalFilepathPromptLabel)
                               .addPreferredGap(ComponentPlacement.RELATED)
                               .addComponent(additionalFilepathPromptScrollPane, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)

                               )
                   );
                   
                   
                   panel.setLayout(gl_panel);
        GroupLayout gl_panel_1 = new GroupLayout(panel_1);
        gl_panel_1.setHorizontalGroup(
            gl_panel_1.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_1.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
                        .addComponent(modelConfigPanel, GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        gl_panel_1.setVerticalGroup(
            gl_panel_1.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_1.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(modelConfigPanel, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(panel, GroupLayout.PREFERRED_SIZE, 1297, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_1.setLayout(gl_panel_1);
        setLayout(gl_configurationPanel);
    }

    private class ApplyConfigChangesSwingAction extends AbstractAction {

        public ApplyConfigChangesSwingAction() {
            putValue(NAME, "Apply config changes to current session");
            putValue(SHORT_DESCRIPTION, "Apply config changes to application but do not save them to file.");
        }

        public void actionPerformed(ActionEvent e) {
            aiConfigManager.setModelName(modelNameField.getText());
            aiConfigManager.setOllamaEndpoint(ollamaEndpointField.getText());
            aiConfigManager.setEmbeddingStoreFile(embeddingStoreField.getText());
            aiConfigManager.setSystemMessage(systemMessageField.getText());
            aiConfigManager.setFilenamePrompt(filenamePromptField.getText());
            aiConfigManager.setAdditionalFilenamePrompt(additionalFilenamePromptField.getText());
            aiConfigManager.setKeywordsPrompt(keywordsPromptField.getText());
            aiConfigManager.setAdditionalKeywordsPrompt(additionalKeywordsPromptField.getText());
            aiConfigManager.setDatesPrompt(datesPromptField.getText());
            aiConfigManager.setAdditionalDatesPrompt(additionalDatesPromptField.getText());
            aiConfigManager.setFilepathPrompt(filepathPromptField.getText());
            aiConfigManager.setAdditionalFilepathPrompt(additionalFilepathPromptField.getText());
        }
    }
}
