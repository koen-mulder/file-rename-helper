package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete;

/**
 * Interface for completions and suggestions used in the autocomplete popup.
 */
public interface AutocompleteItem {

    /**
     * @return the complete text without markup so it can be passed to a text field
     */
    public String getPlainText();

}
