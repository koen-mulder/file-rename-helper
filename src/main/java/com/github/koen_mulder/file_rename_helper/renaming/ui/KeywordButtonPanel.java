package com.github.koen_mulder.file_rename_helper.renaming.ui;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.api.IFileProcessedListener;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.renaming.NewFilenameFieldController;
import com.github.koen_mulder.file_rename_helper.shared.WrapLayout;

/**
 * Panel containing buttons for inserting keywords and date suggestions into the new filename field. 
 */
@SuppressWarnings("serial") // Same-version serialization only
class KeywordButtonPanel extends JPanel implements IOpenFileActionListener, IFileProcessedListener {

    private NewFilenameFieldController newFilenameFieldController;

    private FileProcessingItem activeFileItem;

    /**
     * Panel for insert keyword buttons.
     * 
     * @param newFilenameFieldController the keyword should be inserted in once the keyword button
     *                                   is pressed.
     */
    public KeywordButtonPanel(NewFilenameFieldController newFilenameFieldController) {
        
        this.newFilenameFieldController = newFilenameFieldController;
        setLayout(new WrapLayout(WrapLayout.LEFT));
        setBorder(new TitledBorder("Insert important keywords"));
        
        addPlaceholderLabel();
    }

    private void addPlaceholderLabel() {
        JLabel noKeywordsLabel = new JLabel("Select a processed file to view suggested keywords.");
        noKeywordsLabel.setEnabled(false);
        add(noKeywordsLabel);
    }
    
    public void clearPanel() {
        // Remove all keyword buttons
        removeAll();
    }
    
    /**
     * Add keyword buttons to this panel.
     * 
     * @param keywords List of keywords to create buttons for
     */
    public void addButtons(FileProcessingItem fileItem) {
        List<String> aggregatedSuggestions = Lists.newArrayList();
        aggregatedSuggestions.addAll(fileItem.getKeywordSuggestions());
        aggregatedSuggestions.addAll(fileItem.getDateSuggestions());
        
        // Create a button for each keyword
        for (String keyword : aggregatedSuggestions) {
            add(new JButton(new KeywordButtonAction(keyword)));
        }
    }

    @Override
    public void onOpenFileAction(FileProcessingItem fileItem) {
        if (fileItem == null) {
            // Clear panel by "opening" a null file
            activeFileItem = fileItem;
            clearPanel();
            addPlaceholderLabel();
        } else if (activeFileItem == null || !activeFileItem.equals(fileItem)) {
            activeFileItem = fileItem;
            clearPanel();
            addButtons(fileItem);
        }

        revalidate(); // Tell the layout manager to recalculate
        repaint();    // Repaint the panel
    }

    @Override
    public void onFileProcessed(FileProcessingItem fileItem) {
        // Update buttons if the current active file has been reprocessed
        if (fileItem.equals(activeFileItem)) {
            clearPanel();
            addButtons(fileItem);
        }
    }

    /**
     * Action for buttons that insert a keyword in the new filename field.
     */
    private class KeywordButtonAction extends AbstractAction {
        
        private String keyword;
        
        /**
         * @param filenameField to insert keyword in on actionPerformed
         * @param keyword to insert into field
         */
        public KeywordButtonAction(String keyword) {
            this.keyword = keyword;
            
            putValue(NAME, keyword);
            putValue(SHORT_DESCRIPTION, String.format("Insert \"{}\" into the filename field.", keyword));
        }
        
        public void actionPerformed(ActionEvent e) {
            // Insert relevant word (and overwrite selection if applicable)
            newFilenameFieldController.replaceSelection(" " + keyword + " ");
            // Focus on the filename field
            newFilenameFieldController.grabFocus();
        }
    }
}
