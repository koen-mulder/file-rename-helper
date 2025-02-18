package com.github.koen_mulder.file_rename_helper.gui.rename;

import java.awt.event.ActionEvent;
import java.util.List;

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

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.controller.AIController.FilenameSuggestions;
import com.github.koen_mulder.file_rename_helper.controller.NewFilenameFieldController;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IOpenFileActionPublisher;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModel;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelEvent;

/**
 * Panel containing a list of filename suggestions and controls to interact with the list.
 */
public class SuggestedFilenameListPanel extends JPanel implements IOpenFileActionListener, IFileProcessingModelListener {

    private static final long serialVersionUID = -194287030076951038L;

    private JButton moreSuggestionsButton;
    private JButton clearSuggestionsButton;
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
    public SuggestedFilenameListPanel(AIController aiController, IOpenFileActionPublisher openFileActionPublisher,
            IFileProcessingModelPublisher iFileProcessingModelPublisher,
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
        moreSuggestionsButton = new JButton(
                new MoreSuggestionsButtonAction(aiController, openFileActionPublisher, iFileProcessingModelPublisher));
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
    public void tableChanged(FileProcessingModelEvent e) {
        if (activeFileItem == null) {
            // No active item so no suggestions to add
            return;
        }
        
        if (e.getType() != FileProcessingModelEvent.UPDATE) {
            // Event is for an INSERT or DELETE so no suggestions to add
        }
        
        if (e.getFirstRow() != e.getLastRow()) {
            // Multi row event so not a processing state change
        }
        
        if (e.getSource() instanceof FileProcessingModel) {
            FileProcessingModel model = (FileProcessingModel)e.getSource();
            //FIXME: This is a race condition waiting to happen. If a model change happens before this event is processed then the wrong file will become active!!!! Should use another listener for this and not the model change listener.
            FileProcessingItem item = model.getValueAt(e.getFirstRow());
            listModel.clear();
            List<String> aggregatedSuggestions = Lists.newArrayList();
            for (FilenameSuggestions suggestions : item.getSuggestions()) {
                aggregatedSuggestions.addAll(suggestions.possibleFilenames());
            }
            listModel.addAll(aggregatedSuggestions);
        }
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
            clearList();
            setEnabled(true);
            
            List<String> aggregatedSuggestions = Lists.newArrayList();
            for (FilenameSuggestions suggestions : fileItem.getSuggestions()) {
                aggregatedSuggestions.addAll(suggestions.possibleFilenames());
            }
            listModel.addAll(aggregatedSuggestions);
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

    // TODO: FIX more suggestions!
    private final class MoreSuggestionsButtonAction extends AbstractAction {

        private static final long serialVersionUID = 5890446755560861964L;

        private AIController aiController;
        private IOpenFileActionPublisher openFileActionPublisher;

        private IFileProcessingModelPublisher iFileProcessingModelPublisher;

        public MoreSuggestionsButtonAction(AIController aiController, IOpenFileActionPublisher openFileActionPublisher,
                IFileProcessingModelPublisher iFileProcessingModelPublisher) {
            
            this.aiController = aiController;
            this.openFileActionPublisher = openFileActionPublisher;
            this.iFileProcessingModelPublisher = iFileProcessingModelPublisher;

            putValue(NAME, "Get more suggestions");
            putValue(SHORT_DESCRIPTION, "Request the LLM to generate more filename suggestions.");
        }

        public void actionPerformed(ActionEvent e) {
//            new AdditionalSuggestionWorker(aiController, suggestionPublisher, formEventPublisher).execute();
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
