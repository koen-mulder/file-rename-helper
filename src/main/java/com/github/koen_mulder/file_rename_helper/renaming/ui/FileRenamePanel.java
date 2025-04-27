package com.github.koen_mulder.file_rename_helper.renaming.ui;

import javax.swing.JPanel;

import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.AutocompleteDictionary;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.AutocompleteTextField;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.filename.FilenameCompletionCellRenderer;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.filename.FilenameCompletionService;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.filepath.FilepathCompletionCellRenderer;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.filepath.FilepathCompletionService;

import javax.swing.JTree;
import javax.swing.JButton;

@SuppressWarnings("serial") // Same-version serialization only
public class FileRenamePanel extends JPanel {

    private AutocompleteTextField newFilenameField;
    private JTextField fileExtensionField;
    private AutocompleteTextField newFileLocationField;
    
    private JButton renameButton;

    private FilenameCompletionService filenameCompletionService;
    private FilepathCompletionService filepathCompletionService;
    
    private AutocompleteDictionary filenameDictionary;
    private AutocompleteDictionary filepathDictionary;
    
    /**
     * Panel for selecting a file, viewing filename suggestions and composing a new filename.
     */
    public FileRenamePanel() {
        
        JLabel newFilenameLabel = new JLabel("New filename:");
        
        filenameCompletionService = new FilenameCompletionService();
        FilenameCompletionCellRenderer filenameCellRenderer = new FilenameCompletionCellRenderer();
        
        newFilenameField = new AutocompleteTextField(filenameCompletionService,
                filenameCellRenderer);
        newFilenameField.setColumns(10);
        
        fileExtensionField = new JTextField();
        fileExtensionField.setEnabled(false);
        fileExtensionField.setEditable(false);
        fileExtensionField.setColumns(10);
        
        JLabel newFileLocationLabel = new JLabel("New file location:");
        
        filepathCompletionService = new FilepathCompletionService();
        FilepathCompletionCellRenderer filepathCellRenderer = new FilepathCompletionCellRenderer();
        
        newFileLocationField = new AutocompleteTextField(filepathCompletionService,filepathCellRenderer); 
        newFileLocationField.setColumns(10);
        
        JTree tree = new JTree();
        
        renameButton = new JButton("Rename, relocate & open next file");
        
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(tree, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                        .addComponent(newFileLocationField, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                        .addComponent(newFilenameLabel)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(newFilenameField, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                            .addGap(1)
                            .addComponent(fileExtensionField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                        .addComponent(newFileLocationLabel)
                        .addComponent(renameButton, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                    .addContainerGap())
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(newFilenameLabel)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(newFilenameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(fileExtensionField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(newFileLocationLabel)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(newFileLocationField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(renameButton)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(tree, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                    .addContainerGap())
        );
        setLayout(groupLayout);

    }
    
    public void registerRenameButtonActionListener(ActionListener actionListener) {
        renameButton.addActionListener(actionListener);
    }
    
    public void setFileExtension(String fileExtension) {
        fileExtensionField.setText(fileExtension);
    }
    
    private void clearPanel() {
        newFilenameField.setText("");
        fileExtensionField.setText("");
        newFileLocationField.setText("");
    }
    
    public void closeFile() {
        clearPanel();
        clearFilenameDictionary();
        clearFilepathDictionary();
    }

    public void openFile(FileProcessingItem fileItem) {
        clearPanel();
        setFileExtension(fileItem.getOriginalFileExtension());
        clearFilenameDictionary();
        clearFilepathDictionary();
        updateFilenameDictionary(fileItem);
        updateFilepathDictionary(fileItem);
    }

    public void updateFilenameDictionary(FileProcessingItem fileItem) {
        if (filenameDictionary == null) {
             filenameDictionary = new AutocompleteDictionary();
        }
        filenameDictionary.addCompletions(fileItem.getUsedKeywords());
        filenameDictionary.addCompletions(fileItem.getKeywordSuggestions());
        filenameDictionary.addCompletions(fileItem.getDateSuggestions());
        filenameDictionary.addSuggestions(fileItem.getFilenameSuggestions());
        newFilenameField.setDictionary(filenameDictionary);
    }
    
    public void updateFilepathDictionary(FileProcessingItem fileItem) {
        if (filepathDictionary == null) {
            filepathDictionary = new AutocompleteDictionary();
        }
        filepathDictionary.addCompletions(fileItem.getKeywordSuggestions());
        filepathDictionary.addSuggestions(fileItem.getFilepathSuggestions());
        newFileLocationField.setDictionary(filepathDictionary);
    }
    
    public void clearFilenameDictionary() {
        filenameDictionary = null;
        newFilenameField.clearDictionary();
    }
    
    public void clearFilepathDictionary() {
        filepathDictionary = null;
        newFileLocationField.clearDictionary();
    }
}
