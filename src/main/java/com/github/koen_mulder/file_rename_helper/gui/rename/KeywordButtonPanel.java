package com.github.koen_mulder.file_rename_helper.gui.rename;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.github.koen_mulder.file_rename_helper.controller.AIController.FilenameSuggestions;
import com.github.koen_mulder.file_rename_helper.interfaces.FormClearListener;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionListener;

/**
 * Panel for insert keyword buttons.
 */
public class KeywordButtonPanel extends JPanel implements SuggestionListener, FormClearListener{

    private static final long serialVersionUID = 7824169572049309584L;

    private JTextField filenameField;

    /**
     * Panel for insert keyword buttons.
     * 
     * @param filenameField JTextfield the keyword should be inserted in once the keyword button is pressed.
     */
    public KeywordButtonPanel(JTextField filenameField) {
        this.filenameField = filenameField;
        
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder("Insert important keywords"));
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
    public void addButtons(List<String> keywords) {
        for (String keyword : keywords) {
            add(new JButton(new KeywordButtonAction(keyword)));
        }
        validate();
    }

    @Override
    public void onSuggestionsGenerated(FilenameSuggestions suggestions) {
        addButtons(suggestions.relevantWords());
        addButtons(suggestions.relevantDates());
    }

    @Override
    public void onClearFormEvent() {
        // Clean panel for suggestions based on the new file
        clearPanel();
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
            filenameField.replaceSelection(" " + keyword + " ");
            // Focus on the filename field
            filenameField.grabFocus();
        }
    }
}
