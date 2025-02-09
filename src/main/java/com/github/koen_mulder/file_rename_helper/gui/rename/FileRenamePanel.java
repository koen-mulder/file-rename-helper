package com.github.koen_mulder.file_rename_helper.gui.rename;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.controller.NewFilenameFieldController;
import com.github.koen_mulder.file_rename_helper.interfaces.FileSelectionPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.FormEventPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionPublisher;

/**
 * Panel for selecting a file, viewing filename suggestions and composing a new filename. 
 */
public class FileRenamePanel extends JPanel {

    private static final long serialVersionUID = 5393373407385885597L;

    public AIController aiController;

    /**
     * Panel for selecting a file, viewing filename suggestions and composing a new filename.
     * 
     * @param aiController for requesting suggestions
     * @param fileSelectionPublisher for notifying components (like the PDF viewer) that a file is selected
     * @param suggestionPublisher for notifying components there are new suggestions
     * @param formEventPublisher for notifying form components of the form state
     */
    public FileRenamePanel(AIController aiController, FileSelectionPublisher fileSelectionPublisher,
            SuggestionPublisher suggestionPublisher, FormEventPublisher formEventPublisher) {
        this.aiController = aiController;

        // Create action for opening file chooser
        SelectFileButtonAction selectFileButtonAction = new SelectFileButtonAction(aiController, fileSelectionPublisher,
                suggestionPublisher, formEventPublisher, this);

        // Create panel displaying the current selected filename
        FileSelectionPanel fileSelectionPanel = new FileSelectionPanel(selectFileButtonAction);
        fileSelectionPublisher.addFileSelectionListener(fileSelectionPanel);

        // Create panel with the input field for the new filename
        NewFilenamePanel newFilenamePanel = new NewFilenamePanel();

        // Create controller for other panels to interact with the new filename input
        NewFilenameFieldController newFilenameFieldController = new NewFilenameFieldController(newFilenamePanel.getNewFilenameField());

        // Create panel showing filename suggestions
        SuggestedFilenameListPanel suggestedFilenameListPanel = new SuggestedFilenameListPanel(aiController,
                suggestionPublisher, newFilenameFieldController);
        
        // Create panel with relevant word and date suggestions
        KeywordButtonPanel importantKeywordPanel = new KeywordButtonPanel(newFilenameFieldController);
        formEventPublisher.addFormEventListener(importantKeywordPanel);
        suggestionPublisher.addSuggestionListener(importantKeywordPanel);
        suggestionPublisher.addSuggestionListener(suggestedFilenameListPanel);
        formEventPublisher.addFormEventListener(suggestedFilenameListPanel);
        
        // Create panel for manipulating the new filename
        ReplaceCharacterPanel replaceCharacterPanel = new ReplaceCharacterPanel(newFilenameFieldController);
        formEventPublisher.addFormEventListener(replaceCharacterPanel);

        // Create panel for manipulating the new filename
        MiscButtonPanel removeCharactersPanel = new MiscButtonPanel(newFilenameFieldController);

        // Set layout
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup().addContainerGap()
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(fileSelectionPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 789,
                                        Short.MAX_VALUE)
                                .addComponent(suggestedFilenameListPanel, Alignment.TRAILING,
                                        GroupLayout.PREFERRED_SIZE, 789, Short.MAX_VALUE)
                                .addComponent(newFilenamePanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 789,
                                        Short.MAX_VALUE)
                                .addComponent(importantKeywordPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 789,
                                        Short.MAX_VALUE)
                                .addComponent(replaceCharacterPanel, GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                                .addComponent(removeCharactersPanel, GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE))
                        .addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup().addContainerGap()
                .addComponent(fileSelectionPanel, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(suggestedFilenameListPanel, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(newFilenamePanel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(importantKeywordPanel, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(replaceCharacterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED).addComponent(removeCharactersPanel,
                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(346, Short.MAX_VALUE)));

        setLayout(groupLayout);
    }
}
