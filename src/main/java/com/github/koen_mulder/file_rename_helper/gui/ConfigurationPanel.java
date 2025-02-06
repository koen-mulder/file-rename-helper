package com.github.koen_mulder.file_rename_helper.gui;

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
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.github.koen_mulder.file_rename_helper.config.AIConfigManager;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConfigurationPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private AIConfigManager aiConfigManager;

    private JTextPane initialFilenamePromptField;
    private JTextArea additionalFilenamePromptField;

    private JTextField modelNameField;
    private JTextField ollamaEndpointField;
    private JTextField embeddingStoreField;

    private final Action applyConfigChangesAction = new ApplyConfigChangesSwingAction();

    public ConfigurationPanel() {
        aiConfigManager = AIConfigManager.getInstance();

        JButton btnNewButton_1 = new JButton("Apply config changes and save config to file for next sessions");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyConfigChangesAction.actionPerformed(e);
                aiConfigManager.saveConfig();
            }
        });

        JButton btnNewButton_2 = new JButton("Apply config changes to current session");
        btnNewButton_2.setAction(applyConfigChangesAction);

        JPanel modelConfigPanel = new JPanel();
        modelConfigPanel.setBorder(BorderFactory.createTitledBorder("Model configuration"));

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Filename prompt configuration", TitledBorder.LEADING, TitledBorder.TOP,
                null, null));

        GroupLayout gl_configurationPanel = new GroupLayout(this);
        gl_configurationPanel.setHorizontalGroup(gl_configurationPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_configurationPanel.createSequentialGroup().addContainerGap().addGroup(gl_configurationPanel
                        .createParallelGroup(Alignment.LEADING)
                        .addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
                        .addComponent(modelConfigPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 899,
                                Short.MAX_VALUE)
                        .addGroup(Alignment.TRAILING,
                                gl_configurationPanel.createSequentialGroup().addComponent(btnNewButton_2)
                                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(btnNewButton_1)))
                        .addContainerGap()));
        gl_configurationPanel.setVerticalGroup(gl_configurationPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_configurationPanel.createSequentialGroup().addContainerGap()
                        .addComponent(modelConfigPanel, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(panel, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(gl_configurationPanel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(btnNewButton_1).addComponent(btnNewButton_2))
                        .addContainerGap(159, Short.MAX_VALUE)));

        JLabel promptLabel = new JLabel("Initial filename suggestion prompt:");

        JScrollPane initialPromptScrollPane = new JScrollPane();

        JLabel lblNewLabel = new JLabel("Additional filename suggestion prompt:");

        JScrollPane scrollPane = new JScrollPane();
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
                gl_panel.createSequentialGroup().addContainerGap().addGroup(gl_panel
                        .createParallelGroup(Alignment.TRAILING)
                        .addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 867, Short.MAX_VALUE)
                        .addComponent(initialPromptScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 867,
                                Short.MAX_VALUE)
                        .addComponent(promptLabel, Alignment.LEADING).addComponent(lblNewLabel, Alignment.LEADING))
                        .addContainerGap()));
        gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
                .createSequentialGroup().addContainerGap().addComponent(promptLabel)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(initialPromptScrollPane, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(lblNewLabel)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE).addContainerGap()));

        additionalFilenamePromptField = new JTextArea();
        additionalFilenamePromptField.setText(aiConfigManager.getAdditionalFilenamePrompt());
        scrollPane.setViewportView(additionalFilenamePromptField);

        initialFilenamePromptField = new JTextPane();
        initialPromptScrollPane.setViewportView(initialFilenamePromptField);
        initialFilenamePromptField.setText(aiConfigManager.getFilenamePrompt());
        panel.setLayout(gl_panel);

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
        setLayout(gl_configurationPanel);
    }

    private class ApplyConfigChangesSwingAction extends AbstractAction {

        private static final long serialVersionUID = -416755385397833694L;

        public ApplyConfigChangesSwingAction() {
            putValue(NAME, "Apply config changes to current session");
            putValue(SHORT_DESCRIPTION, "Apply config changes to application but do not save them to file.");
        }

        public void actionPerformed(ActionEvent e) {
            aiConfigManager.setModelName(modelNameField.getText());
            aiConfigManager.setOllamaEndpoint(ollamaEndpointField.getText());
            aiConfigManager.setEmbeddingStoreFile(embeddingStoreField.getText());
            aiConfigManager.setFilenamePrompt(initialFilenamePromptField.getText());
            aiConfigManager.setAdditionalFilenamePrompt(additionalFilenamePromptField.getText());
        }
    }
}
