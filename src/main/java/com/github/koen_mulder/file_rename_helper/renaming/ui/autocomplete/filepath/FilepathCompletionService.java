package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.filepath;

import java.util.List;

import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.AutocompleteDictionary;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.AutocompleteService;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.CompletionItem;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.SuggestionItem;
import com.google.common.collect.Lists;

public class FilepathCompletionService implements AutocompleteService {

    private static final int MAX_SUGGESTIONS = 10;

    private AutocompleteDictionary dictionary = null;

    public FilepathCompletionService() {
    }

    @Override
    public CompletionItem getCompletion(String leadingText, String trailingText) {
        if (dictionary == null) {
            throw new IllegalStateException("Dictionary is not initialized");
        }

        List<String> suggestions = Lists.newArrayList();
        suggestions.addAll(getMatchingSuggestions(leadingText.toLowerCase()));

        String suggestion = suggestions.isEmpty() ? "" : suggestions.getFirst();

        System.out.println(String.format("leadin: %s, trailing: %s, suggestion: %s", leadingText,
                trailingText, suggestion));

        return new CompletionItem(leadingText, suggestion, trailingText);
    }

    @Override
    public List<CompletionItem> getCompletions(String leadingText, String trailingText) {
        if (dictionary == null) {
            throw new IllegalStateException("Dictionary is not initialized");
        }

        List<CompletionItem> suggestions = Lists.newArrayList();

        suggestions.addAll(getMatchingSuggestions(leadingText).stream()
                    .map(completion -> new CompletionItem(leadingText, completion, trailingText))
                    .toList());

        // Remove duplicates and limit the number of suggestions
        suggestions = suggestions.stream().distinct().limit(MAX_SUGGESTIONS).toList();

        // Add static completions to have at least some suggestions
        if (suggestions.isEmpty()) {
            suggestions = dictionary.getCompletions().stream().limit(MAX_SUGGESTIONS)
                    .map(completion -> new CompletionItem(leadingText.trim() + " ", completion,
                            trailingText))
                    .toList();
        }

        return suggestions;
    }

    @Override
    public List<SuggestionItem> getSuggestions() {
        if (dictionary == null) {
            throw new IllegalStateException("Dictionary is not initialized");
        }

        List<SuggestionItem> limitedSuggestionList = dictionary.getSuggestions().stream()
                .map(entry -> new SuggestionItem(entry)).limit(MAX_SUGGESTIONS).toList();
        
        return limitedSuggestionList;
    }
    
    @Override
    public void setDictionary(AutocompleteDictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void clearDictionary() {
        setDictionary(null);        
    }

    private List<String> getMatchingSuggestions(String searchText) {
        return dictionary.getCompletions().stream()
                .filter(entry -> entry.toLowerCase().startsWith(searchText)
                        && !entry.equalsIgnoreCase(searchText))
                .map(entry -> entry.substring(searchText.length())).limit(MAX_SUGGESTIONS).toList();
    }
}
