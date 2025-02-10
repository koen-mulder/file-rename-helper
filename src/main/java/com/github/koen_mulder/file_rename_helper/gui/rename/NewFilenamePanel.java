package com.github.koen_mulder.file_rename_helper.gui.rename;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.github.koen_mulder.file_rename_helper.gui.EFormEvent;
import com.github.koen_mulder.file_rename_helper.interfaces.FormEventListener;

/**
 * Panel containing the input field for the new filename.
 */
public class NewFilenamePanel extends JPanel implements FormEventListener {

    private static final long serialVersionUID = -8735277668945036089L;
    private JTextField newFilenameField;

    
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
                        .addComponent(newFilenameField, GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)));
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
    public void onFormEvent(EFormEvent event) {
        if (event == EFormEvent.ENABLE || event == EFormEvent.DISABLE) {
            setEnabled(event == EFormEvent.ENABLE);
        } else if (event == EFormEvent.CLEAR) {
            clearNewFilenameField();
        }
    }

}
