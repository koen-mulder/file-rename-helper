package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete;

public class SuggestionItem implements AutocompleteItem {

    private final String suggestion;
    
    public SuggestionItem(String suggestion) {
        this.suggestion = suggestion;
    }
    
    @Override
    public String getPlainText() {
        return suggestion;
    }
}
