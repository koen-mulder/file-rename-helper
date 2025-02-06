package com.github.koen_mulder.file_rename_helper.gui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.github.koen_mulder.file_rename_helper.config.AIConfigManager;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextPane;

public class ConfigurationPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private AIConfigManager aiConfigManager;

    private JTextPane promptField;
    private JTextField modelNameField;
    private JTextField ollamaEndpointField;
    private JTextField embeddingStoreField;

    private final Action applyConfigChangesAction = new ApplyConfigChangesSwingAction();


    public ConfigurationPanel() {
        aiConfigManager = AIConfigManager.getInstance();

        JLabel promptLabel = new JLabel("Rename prompt:");

        promptField = new JTextPane();
        promptField.setText(aiConfigManager.getFilenamePrompt());

        JLabel modelNameLabel = new JLabel("Model name");

        modelNameField = new JTextField();
        modelNameField.setText(aiConfigManager.getModelName());
        modelNameField.setColumns(10);

        JLabel ollamaEndpointLabel = new JLabel("Ollama endpoint");

        ollamaEndpointField = new JTextField();
        ollamaEndpointField.setText(aiConfigManager.getOllamaEndpoint());
        ollamaEndpointField.setColumns(10);

        JLabel embeddingStoreLabel = new JLabel("Embedding store");

        embeddingStoreField = new JTextField();
        embeddingStoreField.setText(aiConfigManager.getEmbeddingStoreFile());
        embeddingStoreField.setColumns(10);

        JButton embeddingStoreButton = new JButton("Select file");

        JButton btnNewButton_1 = new JButton("Save config to file");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyConfigChangesAction.actionPerformed(e);
                aiConfigManager.saveConfig();
            }
        });

        JButton btnNewButton_2 = new JButton("Apply config changes");
        btnNewButton_2.setAction(applyConfigChangesAction);

        JSeparator separator = new JSeparator();
        GroupLayout gl_configurationPanel = new GroupLayout(this);
        gl_configurationPanel.setHorizontalGroup(
            gl_configurationPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_configurationPanel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_configurationPanel.createParallelGroup(Alignment.LEADING)
                        .addComponent(separator, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
                        .addGroup(Alignment.TRAILING, gl_configurationPanel.createSequentialGroup()
                            .addGroup(gl_configurationPanel.createParallelGroup(Alignment.LEADING)
                                .addComponent(embeddingStoreLabel)
                                .addComponent(ollamaEndpointLabel)
                                .addComponent(modelNameLabel))
                            .addGap(18)
                            .addGroup(gl_configurationPanel.createParallelGroup(Alignment.TRAILING)
                                .addComponent(modelNameField, GroupLayout.DEFAULT_SIZE, 801, Short.MAX_VALUE)
                                .addComponent(ollamaEndpointField, GroupLayout.DEFAULT_SIZE, 801, Short.MAX_VALUE)
                                .addGroup(gl_configurationPanel.createSequentialGroup()
                                    .addComponent(embeddingStoreField, GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(embeddingStoreButton))))
                        .addComponent(promptField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
                        .addComponent(btnNewButton_1, Alignment.TRAILING)
                        .addComponent(btnNewButton_2, Alignment.TRAILING)
                        .addComponent(promptLabel))
                    .addContainerGap())
        );
        gl_configurationPanel.setVerticalGroup(
            gl_configurationPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_configurationPanel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_configurationPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(modelNameLabel)
                        .addComponent(modelNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(gl_configurationPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(ollamaEndpointLabel)
                        .addComponent(ollamaEndpointField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(gl_configurationPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(embeddingStoreLabel)
                        .addComponent(embeddingStoreField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(embeddingStoreButton))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(promptLabel)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(promptField, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(btnNewButton_2)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                    .addComponent(btnNewButton_1)
                    .addContainerGap())
        );
        setLayout(gl_configurationPanel);
    }

    private class ApplyConfigChangesSwingAction extends AbstractAction {

        private static final long serialVersionUID = -416755385397833694L;

        public ApplyConfigChangesSwingAction() {
            putValue(NAME, "Apply config changes");
            putValue(SHORT_DESCRIPTION, "Apply config changes to application but do not save them to file.");
        }

        public void actionPerformed(ActionEvent e) {
            aiConfigManager.setModelName(modelNameField.getText());
            aiConfigManager.setOllamaEndpoint(ollamaEndpointField.getText());
            aiConfigManager.setEmbeddingStoreFile(embeddingStoreField.getText());
            aiConfigManager.setFilenamePrompt(promptField.getText());
        }
    }
}
