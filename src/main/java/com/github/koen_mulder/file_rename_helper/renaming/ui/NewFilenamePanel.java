package com.github.koen_mulder.file_rename_helper.renaming.ui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionListener;

/**
 * Panel containing the input field for the new filename.
 */
class NewFilenamePanel extends JPanel implements IOpenFileActionListener {

    private static final long serialVersionUID = -8735277668945036089L;
    private JTextField newFilenameField;
    private FileProcessingItem activeFileItem;

    
    /**
     * Panel containing the input field for the new filename.
     */
    public NewFilenamePanel() {
        // Create label and field
        JLabel newFilenameLabel = new JLabel("New filename:");
        newFilenameField = new JTextField();

        // Set layout
        GroupLayout gl_newFilenamePanel = new GroupLayout(this);
        gl_newFilenamePanel.setHorizontalGroup(gl_newFilenamePanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_newFilenamePanel.createSequentialGroup().addComponent(newFilenameLabel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(newFilenameField, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)));
        gl_newFilenamePanel.setVerticalGroup(gl_newFilenamePanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_newFilenamePanel.createSequentialGroup()
                        .addGroup(gl_newFilenamePanel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(newFilenameLabel).addComponent(newFilenameField,
                                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(15, Short.MAX_VALUE)));
        setLayout(gl_newFilenamePanel);
    }
    
    /**
     * @return the textfield for the new filename
     */
    public JTextField getNewFilenameField() {
        return newFilenameField;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        newFilenameField.setEnabled(enabled);
    }

    private void clearNewFilenameField() {
        newFilenameField.setText("");
    }

    @Override
    public void onOpenFileAction(FileProcessingItem fileItem) {
        if (fileItem == null) {
            // Clear panel by "opening" a null file
            activeFileItem = fileItem;
            clearNewFilenameField();
            setEnabled(false);
        } else if (activeFileItem == null || !activeFileItem.equals(fileItem)) {
            activeFileItem = fileItem;
            clearNewFilenameField();
            setEnabled(true);
        }
        
    }

}
