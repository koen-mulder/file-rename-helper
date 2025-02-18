package com.github.koen_mulder.file_rename_helper.gui.rename;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.controller.AIController.FilenameSuggestions;
import com.github.koen_mulder.file_rename_helper.controller.NewFilenameFieldController;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelEvent;

/**
 * Panel for insert keyword buttons.
 */
public class KeywordButtonPanel extends JPanel implements IOpenFileActionListener, IFileProcessingModelListener{

    private static final long serialVersionUID = 7824169572049309584L;

    private NewFilenameFieldController newFilenameFieldController;

    private FileProcessingItem activeFileItem;

    /**
     * Panel for insert keyword buttons.
     * 
     * @param newFilenameFieldController the keyword should be inserted in once the keyword button is pressed.
     */
    public KeywordButtonPanel(NewFilenameFieldController newFilenameFieldController) {
        
        this.newFilenameFieldController = newFilenameFieldController;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder("Insert important keywords"));
    }
    
    public void clearPanel() {
        // Remove all keyword buttons
        removeAll();
        
        revalidate(); // Tell the layout manager to recalculate
        repaint();    // Repaint the panel
    }
    
    /**
     * Add keyword buttons to this panel.
     * 
     * @param keywords List of keywords to create buttons for
     */
    public void addButtons(List<String> keywords) {
        for (String keyword : keywords) {
            add(new JButton(new KeywordButtonAction(keyword)));
        }
        
        revalidate(); // Tell the layout manager to recalculate
        repaint();    // Repaint the panel
    }

    @Override
    public void tableChanged(FileProcessingModelEvent e) {
        // TODO: This should listen to a suggestion listener and not this model listener 
    }

    @Override
    public void onOpenFileAction(FileProcessingItem fileItem) {
        if (fileItem == null) {
            // Clear panel by "opening" a null file
            activeFileItem = fileItem;
            clearPanel();
        } else if (activeFileItem == null || !activeFileItem.equals(fileItem)) {
            activeFileItem = fileItem;
            clearPanel();
            List<String> aggregatedSuggestions = Lists.newArrayList();
            for (FilenameSuggestions suggestions : fileItem.getSuggestions()) {
                aggregatedSuggestions.addAll(suggestions.relevantWords());
            }
            addButtons(aggregatedSuggestions);
        }
    }

    /**
     * Action for buttons that insert a keyword in the new filename field.
     */
    private class KeywordButtonAction extends AbstractAction {
        
        private static final long serialVersionUID = 8005245950554081086L;
        
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
