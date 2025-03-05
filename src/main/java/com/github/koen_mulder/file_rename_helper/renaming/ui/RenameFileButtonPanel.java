package com.github.koen_mulder.file_rename_helper.renaming.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.koen_mulder.file_rename_helper.processing.EFileProcessingItemState;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.renaming.NewFilenameFieldController;

/**
 * Panel containing a button to rename the active file.
 */
@SuppressWarnings("serial") // Same-version serialisation only
public class RenameFileButtonPanel extends JPanel implements IOpenFileActionListener {

    private FileProcessingItem activeFileItem;
    private JButton renameFileButton;

    public RenameFileButtonPanel(NewFilenameFieldController newFilenameFieldController) {
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        renameFileButton = new JButton(
                new RenameFileButtonAction(newFilenameFieldController, this));
        renameFileButton.setEnabled(false);
        add(renameFileButton);
    }

    @Override
    public void onOpenFileAction(FileProcessingItem fileItem) {
        activeFileItem = fileItem;
        // Enable the rename file button if a file is selected
        renameFileButton.setEnabled(fileItem != null);
    }

    /**
     * Action for the rename file button.
     */
    public class RenameFileButtonAction extends AbstractAction {

        private JFileChooser fileChooser;
        private NewFilenameFieldController newFilenameFieldController;
        private Component parent;

        public RenameFileButtonAction(NewFilenameFieldController newFilenameFieldController,
                Component parent) {
            this.newFilenameFieldController = newFilenameFieldController;
            this.parent = parent;

            putValue(Action.NAME, "Rename file..");
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S); // Keyboard shortcut Alt+S
            putValue(Action.SHORT_DESCRIPTION,
                    "Select a directory to move the file to with the new filename.");

            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select directory to move file to");
            // Currently only allow directories to be selected
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            fileChooser.setAcceptAllFileFilterUsed(true);
            // Allow only selecting one directory at a time
            fileChooser.setMultiSelectionEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            
            // Select directory to move file to
            int result = fileChooser.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                Path source = Path.of(activeFileItem.getOriginalAbsoluteFilePath());
                Path target = Path.of(selectedFile.getAbsolutePath() + File.separator
                        + newFilenameFieldController.getNewFilename());

                // Check if file with new name already exists
                if (Files.exists(target)) {
                    // Ask user if they want to replace the file
                    int replaceResult = JOptionPane.showConfirmDialog(parent,
                            "File with this name already exists", "Replace?",
                            JOptionPane.YES_NO_CANCEL_OPTION);

                    if (replaceResult == JOptionPane.CANCEL_OPTION
                            || replaceResult == JOptionPane.CLOSED_OPTION
                            || replaceResult == JOptionPane.NO_OPTION) {
                        // User cancelled or closed the dialog so do not replace file and return
                        return;
                    }
                }

                try {
                    // Try to move the file to the new location
                    Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
                } catch (IOException error) {
                    JOptionPane.showMessageDialog(parent,
                            "Error renaming file: " + error.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                
                activeFileItem.setState(EFileProcessingItemState.RENAMED);
            }
        }
    }
}
