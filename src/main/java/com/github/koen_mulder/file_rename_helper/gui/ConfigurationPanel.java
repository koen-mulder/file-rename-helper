package com.github.koen_mulder.file_rename_helper.gui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ConfigurationPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public ConfigurationPanel() {
        
        JLabel promptLabel = new JLabel("Rename prompt:");
        
        JTextArea promptField = new JTextArea();
        promptField.setColumns(10);
        GroupLayout gl_configurationPanel = new GroupLayout(this);
        gl_configurationPanel.setHorizontalGroup(
            gl_configurationPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_configurationPanel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_configurationPanel.createParallelGroup(Alignment.LEADING)
                        .addComponent(promptField, GroupLayout.DEFAULT_SIZE, 1239, Short.MAX_VALUE)
                        .addComponent(promptLabel))
                    .addContainerGap())
        );
        gl_configurationPanel.setVerticalGroup(
            gl_configurationPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_configurationPanel.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(promptLabel)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(promptField, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(378, Short.MAX_VALUE))
        );
        setLayout(gl_configurationPanel);
    }
}
