package com.github.koen_mulder.file_rename_helper.gui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

// TODO: This panel serves no purpose anymore
public class StatusBarPanel extends JPanel {

    private static final long serialVersionUID = 8547601904662234829L;
    
    private JProgressBar progressBar;

    public StatusBarPanel() {
        
        progressBar = new JProgressBar();
        
        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap(1049, Short.MAX_VALUE)
                    .addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                    .addGap(6))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(separator, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 14, Short.MAX_VALUE)
                        .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        setLayout(groupLayout);
        
    }
}
