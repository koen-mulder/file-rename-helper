package com.github.koen_mulder.file_rename_helper.gui.rename;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.github.koen_mulder.file_rename_helper.interfaces.FileSelectionListener;
import javax.swing.JSeparator;

public class FileSelectionPanel extends JPanel implements FileSelectionListener {

    private static final long serialVersionUID = 165050960316961796L;

    private JTextField fileNameField;

    public FileSelectionPanel(SelectFileButtonAction selectFileButtonAction) {

        JLabel selectFileLabel = new JLabel("Select file:");

        // Textfield containing the filepath
        fileNameField = new JTextField();
        fileNameField.setEditable(false);

        // Add a MouseListener to the JTextField
        fileNameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Check for double-click
                    selectFileButtonAction
                            .actionPerformed(new ActionEvent(fileNameField, ActionEvent.ACTION_PERFORMED, null));
                }
            }
        });
        
        // "Select File"-button
        JButton selectFileButton = new JButton(selectFileButtonAction);
        
        JSeparator separator = new JSeparator();

        // Set layout
        GroupLayout gl_fileSelectionPanel = new GroupLayout(this);
        gl_fileSelectionPanel.setHorizontalGroup(
            gl_fileSelectionPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_fileSelectionPanel.createSequentialGroup()
                    .addComponent(selectFileLabel)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(fileNameField, GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(selectFileButton))
                .addComponent(separator, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );
        gl_fileSelectionPanel.setVerticalGroup(
            gl_fileSelectionPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_fileSelectionPanel.createSequentialGroup()
                    .addGroup(gl_fileSelectionPanel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(selectFileButton)
                        .addComponent(selectFileLabel)
                        .addComponent(fileNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(8)
                    .addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(270, Short.MAX_VALUE))
        );
        setLayout(gl_fileSelectionPanel);

    }

    @Override
    public void onFileSelected(String filePath) {
        fileNameField.setText(filePath);
    }
}
