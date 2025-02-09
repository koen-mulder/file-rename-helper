package com.github.koen_mulder.file_rename_helper.gui.rename;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.gui.EFormEvent;
import com.github.koen_mulder.file_rename_helper.gui.rename.workers.FilenameSuggestionWorker;
import com.github.koen_mulder.file_rename_helper.interfaces.FileSelectionPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.FormEventPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionPublisher;

/**
 * SwingAction for the "Select file" button. Handles opening a file chooser,
 * starting a SwingWorker for suggestions and notifying listeners.
 */
public class SelectFileButtonAction extends AbstractAction {

    private static final long serialVersionUID = 7773619212631079877L;

    private AIController aiController;
    private FileSelectionPublisher fileSelectionPublisher;
    private SuggestionPublisher suggestionPublisher;
    private FormEventPublisher formEventPublisher;
    private Component parent;

    private JFileChooser fileChooser;

    /**
     * SwingAction for the "Select file" button. Handles opening a file chooser,
     * starting a SwingWorker for suggestions and notifying listeners.
     * 
     * @param aiController for requesting suggestions
     * @param fileSelectionPublisher for notifying components (like the PDF viewer) that a file is selected
     * @param suggestionPublisher for notifying components there are new suggestions
     * @param formEventPublisher for notifying form components of the form state
     * @param parent component for the JDialog
     */
    public SelectFileButtonAction(AIController aiController, FileSelectionPublisher fileSelectionPublisher,
            SuggestionPublisher suggestionPublisher, FormEventPublisher formEventPublisher, Component parent) {

        this.aiController = aiController;
        this.fileSelectionPublisher = fileSelectionPublisher;
        this.suggestionPublisher = suggestionPublisher;
        this.formEventPublisher = formEventPublisher;
        this.parent = parent;

        putValue(Action.NAME, "Select File");
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O); // Keyboard shortcut Alt+O
        putValue(Action.SHORT_DESCRIPTION, "Select a file to receive filename suggestions based on file content.");

        fileChooser = new JFileChooser();
        // Currently only allow PDF files to be selected
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        // Allow only processing one file at a time
        fileChooser.setMultiSelectionEnabled(false);
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
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Now that the GUI is all in place, we can try opening a PDF
            fileSelectionPublisher.notifyFileSelectionListeners(selectedFile.getAbsolutePath());
            // Start worker for the slow process of LLM API calls
            new FilenameSuggestionWorker(aiController, suggestionPublisher, formEventPublisher,
                    selectedFile.getAbsolutePath()).execute();
            // Clear forms of previous suggestions
            formEventPublisher.notifyFormEventListeners(EFormEvent.CLEAR);
            formEventPublisher.notifyFormEventListeners(EFormEvent.DISABLE);
        }
    }
}
