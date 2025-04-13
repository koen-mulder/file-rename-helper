package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.filename;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.AutocompleteItem;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.CompletionItem;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.SuggestionItem;

@SuppressWarnings("serial") // Same-version serialization only
public class FilenameCompletionCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        // Let the default renderer handle selection colors, borders etc.
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);

        if (value instanceof AutocompleteItem) {
            if (value instanceof CompletionItem) {
                CompletionItem item = (CompletionItem) value;
                label.setText(String.format("<html>%s<b>%s</b>%s</html>", item.getLeadingText(),
                        item.getCompletion(), item.getTrailingText()));
            } else if (value instanceof SuggestionItem) {
                SuggestionItem item = (SuggestionItem) value;
                label.setText(String.format("<html><span style=\"background-color:#EEEEEE;"
                        + " color: #AAAAAA; font: monospaced;\">&nbsp;Alt+%d&nbsp;</span>"
                        + "&nbsp;<b>%s</b></html>", index, item.getPlainText()));
            } else {
                AutocompleteItem item = (AutocompleteItem) value;
                label.setText(item.getPlainText());
            }
        } else {
            // Fallback for unexpected data types
            label.setText(value != null ? value.toString() : "");
        }
        return label;
    }

}
