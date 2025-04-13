package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete;

import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * DocumentFilter for handling autocomplete functionality in a JTextField.
 */
public class AutocompleteDocumentFilter extends DocumentFilter {
    
    // Flag to prevent infinite loops when updating text
    private boolean isUpdatingText = false;
    private boolean isActive = true;
    
    private AutocompleteTextField autocompleteTextField;

    public AutocompleteDocumentFilter(AutocompleteTextField autocompleteTextField) {
                this.autocompleteTextField = autocompleteTextField;
    }
    
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {

        if (isUpdatingText || !isActive) {
            // Accept the change without further processing and starting an infinite loop
            super.insertString(fb, offset, string, attr);
            return;
        }

        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        // Determine leading and trailing text after change
        String leadingText = currentText.substring(0, offset) + string;
        String trailingText = currentText.substring(offset);
        
        // Perform the actual insertion
        super.insertString(fb, offset, string, attr);
        
        handleInlineCompletion(leadingText, trailingText);
        handlePopupCompletion(leadingText, trailingText);
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        
        if (isUpdatingText || !isActive) {
            // Accept the change without further processing and starting an infinite loop
            super.replace(fb, offset, length, text, attrs);
            return;
        }
        
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        // Determine leading and trailing text after change
        String leadingText = currentText.substring(0, offset) + (text == null ? "" : text);
        String trailingText = currentText.substring(offset + length);
        
        // Perform the actual replacement
        super.replace(fb, offset, length, text, attrs);
        
        handleInlineCompletion(leadingText, trailingText);
        handlePopupCompletion(leadingText, trailingText);
    }
    
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

        if (isUpdatingText || !isActive) {
            // Accept the change without further processing and starting an infinite loop
            super.remove(fb, offset, length);
            return;
        }
        
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        // Determine leading and trailing text after change
        String leadingText = currentText.substring(0, offset);
        String trailingText = currentText.substring(offset + length);
        
        // Perform the actual removal
        super.remove(fb, offset, length);
        
        // Only do popup completion on removal
        handlePopupCompletion(leadingText, trailingText);
    }


    private void handleInlineCompletion(String leadingText, String trailingText) {
        // Use invokeLater to ensure UI updates happen correctly after document changes
        SwingUtilities.invokeLater(() -> {
            isUpdatingText = true; // Prevent feedback loop
            try {
                autocompleteTextField.handleInlineCompletion(leadingText, trailingText);
            } finally {
                // Crucial: Reset the flag after processing is done
                SwingUtilities.invokeLater(() -> isUpdatingText = false);
            }
        });
    }

    private void handlePopupCompletion(String leadingText, String trailingText) {
        autocompleteTextField.handlePopupCompletion(leadingText, trailingText);
    }
    
    /**
     * Enable or disable filter during external programmatic changes.
     * 
     * @param active true to enable the filter, false to disable it
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
}