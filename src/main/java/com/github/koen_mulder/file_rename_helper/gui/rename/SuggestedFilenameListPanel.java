package com.github.koen_mulder.file_rename_helper.gui.rename;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.controller.AIController.FilenameSuggestions;
import com.github.koen_mulder.file_rename_helper.controller.NewFilenameFieldController;
import com.github.koen_mulder.file_rename_helper.gui.EFormEvent;
import com.github.koen_mulder.file_rename_helper.gui.rename.workers.AdditionalSuggestionWorker;
import com.github.koen_mulder.file_rename_helper.interfaces.FormEventListener;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionListener;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionPublisher;

/**
 * Panel containing a list of filename suggestions and controls to interact with the list.
 */
public class SuggestedFilenameListPanel extends JPanel implements SuggestionListener, FormEventListener {

    private static final long serialVersionUID = -194287030076951038L;

    private JButton moreSuggestionsButton;
    private JButton clearSuggestionsButton;
    private JList<String> suggestedFilenameList;

    private DefaultListModel<String> listModel;

    /**
     * Panel containing a list of filename suggestions and controls to interact with the list.
     * 
     * @param aiController for requesting suggestions
     * @param suggestionPublisher for notifying components there are new suggestions
     * @param newFilenameFieldController controller for interacting with the new filename field
     */
    public SuggestedFilenameListPanel(AIController aiController, SuggestionPublisher suggestionPublisher,
            NewFilenameFieldController newFilenameFieldController) {
        JLabel listLabel = new JLabel("Select suggested filename:");

        // Create model for the suggestions
        listModel = new DefaultListModel<String>();

        // Create list with suggestions
        suggestedFilenameList = new JList<>();
        suggestedFilenameList.setModel(listModel);
        suggestedFilenameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestedFilenameList.addListSelectionListener(
                new SuggestionSelectionListener(suggestedFilenameList, newFilenameFieldController));
        suggestedFilenameList.setVisibleRowCount(10);

        // Create scroll pane so we can scroll through list of suggestions
        JScrollPane scrollPane = new JScrollPane(suggestedFilenameList);

        // Create buttons
        moreSuggestionsButton = new JButton(new MoreSuggestionsButtonAction(aiController, suggestionPublisher));
        clearSuggestionsButton = new JButton(new ClearSuggestionsButtonAction(listModel));

        // Disable fields because no suggestions have been loaded yet
        setEnabled(false);

        // Set layout
        GroupLayout gl_suggestedFilenamePanel = new GroupLayout(this);
        gl_suggestedFilenamePanel.setHorizontalGroup(gl_suggestedFilenamePanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_suggestedFilenamePanel.createSequentialGroup().addComponent(listLabel).addContainerGap(735,
                        Short.MAX_VALUE))
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                .addGroup(gl_suggestedFilenamePanel.createSequentialGroup().addComponent(moreSuggestionsButton)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(clearSuggestionsButton).addGap(189)));
        gl_suggestedFilenamePanel.setVerticalGroup(gl_suggestedFilenamePanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_suggestedFilenamePanel.createSequentialGroup().addComponent(listLabel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(gl_suggestedFilenamePanel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(moreSuggestionsButton).addComponent(clearSuggestionsButton))
                        .addContainerGap(56, Short.MAX_VALUE)));
        setLayout(gl_suggestedFilenamePanel);
    }

    @Override
    public void setEnabled(boolean enabled) {
        suggestedFilenameList.setEnabled(enabled);
        moreSuggestionsButton.setEnabled(enabled);
        clearSuggestionsButton.setEnabled(enabled);
    }

    /**
     * Empty the list of suggestions
     */
    private void clearList() {
        listModel.clear();
    }

    @Override
    public void onFormEvent(EFormEvent event) {
        if (event == EFormEvent.DISABLE) {
            setEnabled(false);
        } else if (event == EFormEvent.ENABLE) {
            setEnabled(true);
        } else if (event == EFormEvent.CLEAR) {
            clearList();
        }
    }

    @Override
    public void onSuggestionsGenerated(FilenameSuggestions suggestions) {
        // Add all suggestions
        listModel.addAll(suggestions.possibleFilenames());
    }

    private final class SuggestionSelectionListener implements ListSelectionListener {

        private JList<String> listField;
        private NewFilenameFieldController newFilenameFieldController;

        private SuggestionSelectionListener(JList<String> listField,
                NewFilenameFieldController newFilenameFieldController) {
            this.listField = listField;
            this.newFilenameFieldController = newFilenameFieldController;
        }

        public void valueChanged(ListSelectionEvent e) {

            if (e.getValueIsAdjusting()) {
                return; // Handle only the final selection change
            }

            String suggestedFilename = listField.getSelectedValue();

            if (suggestedFilename == null) {
                // No file selected so do nothing
                return;
            }

            newFilenameFieldController.setText(suggestedFilename);
            newFilenameFieldController.grabFocus();
            newFilenameFieldController.setCaretPositionBeforeExtention();

            // Delayed clear selection
            Timer timer = new Timer(500, _ -> listField.clearSelection());
            timer.setRepeats(false); // Ensure the timer only runs once
            timer.start();
        }
    }

    private final class MoreSuggestionsButtonAction extends AbstractAction {

        private static final long serialVersionUID = 5890446755560861964L;

        private AIController aiController;
        private SuggestionPublisher suggestionPublisher;

        public MoreSuggestionsButtonAction(AIController aiController, SuggestionPublisher suggestionPublisher) {
            this.aiController = aiController;
            this.suggestionPublisher = suggestionPublisher;

            putValue(NAME, "Get more suggestions");
            putValue(SHORT_DESCRIPTION, "Request the LLM to generate more filename suggestions.");
        }

        public void actionPerformed(ActionEvent e) {
            new AdditionalSuggestionWorker(aiController, suggestionPublisher).execute();
        }
    }

    private final class ClearSuggestionsButtonAction extends AbstractAction {

        private static final long serialVersionUID = 5890446755560861964L;

        private DefaultListModel<String> listModel;

        public ClearSuggestionsButtonAction(DefaultListModel<String> listModel) {

            this.listModel = listModel;
            putValue(NAME, "Clear suggestion list");
            putValue(SHORT_DESCRIPTION, "Clear the list of filename suggestions.");
        }

        public void actionPerformed(ActionEvent e) {
            listModel.clear();
        }
    }
}
