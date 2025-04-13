package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete;

import java.util.List;

/**
 * Dictionary of autocomplete entries. Contains both completions and suggestions.
 */
public class AutocompleteDictionary {
    
    private final List<String> completions;

    private final List<String> suggestions;

    public AutocompleteDictionary(List<String> completions, List<String> suggestions) {
        this.completions = completions;
        this.suggestions = suggestions;
    }
    
    public List<String> getCompletions() {
        return completions;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }
}
