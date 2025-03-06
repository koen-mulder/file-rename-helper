package com.github.koen_mulder.file_rename_helper.renaming.ui;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelController;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionPublisher;
import com.github.koen_mulder.file_rename_helper.renaming.NewFilenameFieldController;
import com.github.koen_mulder.file_rename_helper.suggestions.AIController;

/**
 * Panel for selecting a file, viewing filename suggestions and composing a new filename. 
 */
public class FileRenamePanel extends JPanel {

    private static final int PREFERRED_COMPONENT_WIDTH = 400;
    
    private static final long serialVersionUID = 5393373407385885597L;

    /**
     * Panel for selecting a file, viewing filename suggestions and composing a new filename.
     * 
     * @param aiController for requesting suggestions
     * @param fileSelectionPublisher for notifying components (like the PDF viewer) that a file is selected
     * @param suggestionPublisher for notifying components there are new suggestions
     * @param formEventPublisher for notifying form components of the form state
     */
    public FileRenamePanel(AIController aiController, IOpenFileActionPublisher openFileActionPublisher,
            FileProcessingModelController fileProcessingModelController) {

        // Create panel with the input field for the new filename
        NewFilenamePanel newFilenamePanel = new NewFilenamePanel();
        openFileActionPublisher.addOpenFileActionListener(newFilenamePanel);
        
        // Create controller for other panels to interact with the new filename input
        NewFilenameFieldController newFilenameFieldController = new NewFilenameFieldController(newFilenamePanel.getNewFilenameField());

        // Create panel showing filename suggestions
        SuggestedFilenameListPanel suggestedFilenameListPanel = new SuggestedFilenameListPanel(
                fileProcessingModelController, newFilenameFieldController);
        openFileActionPublisher.addOpenFileActionListener(suggestedFilenameListPanel);
        fileProcessingModelController.addFileProcessedListener(suggestedFilenameListPanel);
        
        // Create panel with relevant word and date suggestions
        KeywordButtonPanel importantKeywordPanel = new KeywordButtonPanel(newFilenameFieldController);
        openFileActionPublisher.addOpenFileActionListener(importantKeywordPanel);
        fileProcessingModelController.addFileProcessedListener(importantKeywordPanel);
        
        // Create panel for manipulating the new filename
        ReplaceCharacterPanel replaceCharacterPanel = new ReplaceCharacterPanel(newFilenameFieldController);
        openFileActionPublisher.addOpenFileActionListener(replaceCharacterPanel);

        // Create panel for manipulating the new filename
        MiscButtonPanel removeCharactersPanel = new MiscButtonPanel(newFilenameFieldController);
        openFileActionPublisher.addOpenFileActionListener(removeCharactersPanel);

        // Create panel with the rename button
        RenameFileButtonPanel renameButtonPanel = new RenameFileButtonPanel(newFilenameFieldController);
        openFileActionPublisher.addOpenFileActionListener(renameButtonPanel);

        // Content panel
        JPanel contentPanel = new JPanel();
        
        // Set layout
        GroupLayout groupLayout = new GroupLayout(contentPanel);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(suggestedFilenameListPanel, GroupLayout.PREFERRED_SIZE, PREFERRED_COMPONENT_WIDTH, Short.MAX_VALUE)
                        .addComponent(newFilenamePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, PREFERRED_COMPONENT_WIDTH, Short.MAX_VALUE)
                        .addComponent(importantKeywordPanel, GroupLayout.DEFAULT_SIZE, PREFERRED_COMPONENT_WIDTH, Short.MAX_VALUE)
                        .addComponent(replaceCharacterPanel, GroupLayout.DEFAULT_SIZE, PREFERRED_COMPONENT_WIDTH, Short.MAX_VALUE)
                        .addComponent(removeCharactersPanel, GroupLayout.DEFAULT_SIZE, PREFERRED_COMPONENT_WIDTH, Short.MAX_VALUE)
                        .addComponent(renameButtonPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, PREFERRED_COMPONENT_WIDTH, Short.MAX_VALUE))
                    .addContainerGap())
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(suggestedFilenameListPanel, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(newFilenamePanel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(importantKeywordPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(replaceCharacterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(removeCharactersPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(renameButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        
        contentPanel.setLayout(groupLayout);
        
        // Add content panel to scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
}
