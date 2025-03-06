package com.github.koen_mulder.file_rename_helper.renaming;

import javax.swing.JTextField;

/**
 * Controller for the new filename field.
 * 
 * This class is responsible for handling operations on the new filename field. It helps us avoid
 * passing a GUI component around the codebase. It allows us to add extra functionality to basic
 * JTextField operations, like removing extra whitespace.
 */
public class NewFilenameFieldController {

    private JTextField newFilenameField;

    public NewFilenameFieldController(JTextField newFilenameField) {
        this.newFilenameField = newFilenameField;
    }

    public void replace(String target, String replacement) {
        newFilenameField.setText(newFilenameField.getText().replace(target, replacement));
    }

    public void replaceSelection(String content) {
        newFilenameField.replaceSelection(content);
    }

    public void grabFocus() {
        newFilenameField.grabFocus();
    }

    public String getText() {
        return newFilenameField.getText();
    }

    public void setText(String content) {
        newFilenameField.setText(content.strip());
    }

    public void setCaretPositionBeforeExtention() {
        int extensionIndex = newFilenameField.getText().lastIndexOf(".");
        if (extensionIndex > 0) {
            newFilenameField.setCaretPosition(extensionIndex);
        }
    }

    public String getNewFilename() {
        return newFilenameField.getText();
    }
}
