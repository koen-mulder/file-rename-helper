package com.github.koen_mulder.file_rename_helper.renaming;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelController;
import com.github.koen_mulder.file_rename_helper.processing.api.IFileProcessedListener;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionPublisher;
import com.github.koen_mulder.file_rename_helper.project.ActiveProjectController;
import com.github.koen_mulder.file_rename_helper.renaming.ui.FileRenamePanel;

public class FileRenameController {

    private final FileRenamePanel fileRenamePanel;

    private FileProcessingItem activeFileItem;

    public FileRenameController(FileRenamePanel fileRenamePanel,
            ActiveProjectController activeProjectController) {

        this.fileRenamePanel = fileRenamePanel;
        
        IOpenFileActionPublisher openFileActionPublisher = activeProjectController
                .getOpenFileActionPublisher();
        FileProcessingModelController fileProcessingModelController = activeProjectController
                .getProcessingController().getFileProcessingModelController();
        
        // Set listener to what happens when a file is opened
        openFileActionPublisher.addOpenFileActionListener(new IOpenFileActionListener() {
            
            @Override
            public void onOpenFileAction(FileProcessingItem fileItem) {
                if (fileItem == null) {
                    closeFile();
                } else {
                    openFile(fileItem);
                }
            }
        });
        
        // Set listener to what happens when a file is processed
        fileProcessingModelController.addFileProcessedListener(new IFileProcessedListener() {
            
            @Override
            public void onFileProcessed(FileProcessingItem fileItem) {
                if (fileItem != null && fileItem == activeFileItem) {
                    updateDictionaries(fileItem);
                }
            }
        });
        
        // Register listener to file rename button
        fileRenamePanel.registerRenameButtonActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                renameFile();
            }
        });
    }
    
    protected void closeFile() {
        fileRenamePanel.closeFile();
    }

    private void openFile(FileProcessingItem fileItem) {
        if (fileItem != null) {
            if (fileItem != activeFileItem) {
                fileRenamePanel.openFile(fileItem);
            }
        }
        activeFileItem = fileItem;
    }
    
    private void updateDictionaries(FileProcessingItem fileItem) {
        fileRenamePanel.updateFilenameDictionary(fileItem);
        fileRenamePanel.updateFilepathDictionary(fileItem);
    }
    
    private void renameFile() {
//     // Select directory to move file to
//        int result = fileChooser.showOpenDialog(parent);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = fileChooser.getSelectedFile();
//
//            Path source = Path.of(activeFileItem.getOriginalAbsoluteFilePath());
//            Path target = Path.of(selectedFile.getAbsolutePath() + File.separator
//                    + newFilenameFieldController.getNewFilename());
//
//            // Check if file with new name already exists
//            if (Files.exists(target)) {
//                // Ask user if they want to replace the file
//                int replaceResult = JOptionPane.showConfirmDialog(parent,
//                        "File with this name already exists", "Replace?",
//                        JOptionPane.YES_NO_CANCEL_OPTION);
//
//                if (replaceResult == JOptionPane.CANCEL_OPTION
//                        || replaceResult == JOptionPane.CLOSED_OPTION
//                        || replaceResult == JOptionPane.NO_OPTION) {
//                    // User cancelled or closed the dialog so do not replace file and return
//                    return;
//                }
//            }
//
//            try {
//                // Try to move the file to the new location
//                Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
//            } catch (IOException error) {
//                JOptionPane.showMessageDialog(parent,
//                        "Error renaming file: " + error.getMessage(), "Error",
//                        JOptionPane.ERROR_MESSAGE);
//            }
//            
//            activeFileItem.setState(EFileProcessingItemState.RENAMED);
//        }
//    }
    }

}
