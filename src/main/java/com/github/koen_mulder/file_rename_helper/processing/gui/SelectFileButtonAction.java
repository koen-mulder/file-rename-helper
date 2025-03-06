package com.github.koen_mulder.file_rename_helper.processing.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelController;
import com.google.common.collect.Lists;

/**
 * SwingAction for the "Select file" button. Handles opening a file chooser,
 * starting a SwingWorker for suggestions and notifying listeners.
 */
//TODO: Fix Javadoc
class SelectFileButtonAction extends AbstractAction {

    private static final long serialVersionUID = 7773619212631079877L;

    private JFileChooser fileChooser;

    private FileProcessingModelController fileProcessingModelController;
    private Component parent;


    /**
     * SwingAction for the "Select file" button. Handles opening a file chooser,
     * starting a SwingWorker for suggestions and notifying listeners.
     * @param parent component for the JDialog
     * @param fileSelectionPublisher for notifying components (like the PDF viewer) that a file is selected
     * @param suggestionPublisher for notifying components there are new suggestions
     * @param formEventPublisher for notifying form components of the form state
     */
    public SelectFileButtonAction(FileProcessingModelController fileProcessingModelController, Component parent) {

        this.fileProcessingModelController = fileProcessingModelController;
        this.parent = parent;

        putValue(Action.NAME, "Add files for processing");
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O); // Keyboard shortcut Alt+O
        putValue(Action.SHORT_DESCRIPTION, "Select a file to receive filename suggestions based on file content.");

        fileChooser = new JFileChooser();
        // Currently only allow PDF files to be selected
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        // Allow only processing one file at a time
        fileChooser.setMultiSelectionEnabled(true);
        // Temporary set current directory to test files
        fileChooser.setCurrentDirectory(new File("E:\\tools\\rename-pdf-files\\test-files\\unorganised"));
        // Filter to only show PDF files
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "PDF Files (.pdf)";
            }

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".pdf");
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        // Remove selection
        fileChooser.setSelectedFile(new File(""));
        fileChooser.setSelectedFiles(new File[]{ new File("") });

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            List<FileProcessingItem> items = Lists.newArrayList();
            for (File file : selectedFiles) {
                items.add(new FileProcessingItem(file.getAbsolutePath(), file.getName()));
            }
            fileProcessingModelController.add(items);
        }
    }
}
