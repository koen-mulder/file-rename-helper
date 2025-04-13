package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Dictionary of autocomplete entries. Contains both completions and suggestions.
 */
public class AutocompleteDictionary {
    
    private final List<String> completions = Lists.newArrayList();
    private final List<String> suggestions = Lists.newArrayList();

    public List<String> getCompletions() {
        return completions;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void addCompletions(List<String> keywordSuggestions) {
        completions.addAll(keywordSuggestions);
    }

    public void addSuggestions(List<String> filenameSuggestions) {
        suggestions.addAll(filenameSuggestions);
    }
}
