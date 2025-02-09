package com.github.koen_mulder.file_rename_helper.controller;

import javax.swing.JTextField;

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
        newFilenameField.setText(content);
    }

    public void setCaretPositionBeforeExtention() {
        int extensionIndex = newFilenameField.getText().lastIndexOf(".");
        if (extensionIndex > 0) {
            newFilenameField.setCaretPosition(extensionIndex);
        }
    }
}
