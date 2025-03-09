package com.github.koen_mulder.file_rename_helper.renaming.ui;

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
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelController;
import com.github.koen_mulder.file_rename_helper.processing.api.IFileProcessedListener;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.renaming.NewFilenameFieldController;

/**
 * Panel containing a list of filename suggestions and controls to interact with the list.
 */
@SuppressWarnings("serial") // Same-version serialization only
class SuggestedFilenameListPanel extends JPanel implements IOpenFileActionListener, IFileProcessedListener {

    private JButton moreSuggestionsButton;
    private JList<String> suggestedFilenameList;

    private DefaultListModel<String> listModel;

    private FileProcessingItem activeFileItem;

    /**
     * Panel containing a list of filename suggestions and controls to interact with the list.
     * 
     * @param aiController for requesting suggestions
     * @param suggestionPublisher for notifying components there are new suggestions
     * @param formEventPublisher 
     * @param newFilenameFieldController controller for interacting with the new filename field
     */
    public SuggestedFilenameListPanel(FileProcessingModelController fileProcessingModelController,
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
        suggestedFilenameList.setCellRenderer(new AlternatingListCellRenderer(suggestedFilenameList));
        suggestedFilenameList.setVisibleRowCount(10);

        // Create scroll pane so we can scroll through list of suggestions
        JScrollPane scrollPane = new JScrollPane(suggestedFilenameList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Create buttons
        moreSuggestionsButton = new JButton(new MoreSuggestionsButtonAction(fileProcessingModelController));

        // Disable fields because no suggestions have been loaded yet
        setEnabled(false);

        // Set layout
        GroupLayout gl_suggestedFilenamePanel = new GroupLayout(this);
        gl_suggestedFilenamePanel.setHorizontalGroup(
            gl_suggestedFilenamePanel.createParallelGroup(Alignment.LEADING)
                .addComponent(listLabel)
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addGroup(gl_suggestedFilenamePanel.createSequentialGroup()
                    .addComponent(moreSuggestionsButton))
        );
        gl_suggestedFilenamePanel.setVerticalGroup(
            gl_suggestedFilenamePanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_suggestedFilenamePanel.createSequentialGroup()
                    .addComponent(listLabel)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(moreSuggestionsButton)
                    .addGap(5))
        );
        setLayout(gl_suggestedFilenamePanel);
    }

    @Override
    public void setEnabled(boolean enabled) {
        suggestedFilenameList.setEnabled(enabled);
        moreSuggestionsButton.setEnabled(enabled);
    }

    /**
     * Empty the list of suggestions
     */
    private void clearList() {
        listModel.clear();
    }

    @Override
    public void onFileProcessed(FileProcessingItem fileItem) {
        if (activeFileItem == null) {
            // No active item so no suggestions to add
            return;
        }

        if (activeFileItem.equals(fileItem)) {
            addFilenameSuggestions(fileItem);
        }
    }
    
    private void addFilenameSuggestions(FileProcessingItem fileItem) {
        // Clear existing buttons
        clearList();

        // Add buttons
        listModel.addAll(fileItem.getFilenameSuggestions());

    }

    @Override
    public void onOpenFileAction(FileProcessingItem fileItem) {
        if (fileItem == null) {
            // Clear panel by "opening" a null file
            activeFileItem = fileItem;
            clearList();
            setEnabled(false);
        } else if (activeFileItem == null || !activeFileItem.equals(fileItem)) {
            activeFileItem = fileItem;
            setEnabled(true);
            
            addFilenameSuggestions(fileItem);
        }
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
            Timer timer = new Timer(500, event -> listField.clearSelection());
            timer.setRepeats(false); // Ensure the timer only runs once
            timer.start();
        }
    }

    private final class MoreSuggestionsButtonAction extends AbstractAction {

        private FileProcessingModelController fileProcessingModelController;

        public MoreSuggestionsButtonAction(FileProcessingModelController fileProcessingModelController) {
            
            this.fileProcessingModelController = fileProcessingModelController;

            putValue(NAME, "Get more suggestions");
            putValue(SHORT_DESCRIPTION, "Request the LLM to generate more filename suggestions.");
        }

        public void actionPerformed(ActionEvent e) {
            fileProcessingModelController.requeue(activeFileItem);
        }
    }
}
