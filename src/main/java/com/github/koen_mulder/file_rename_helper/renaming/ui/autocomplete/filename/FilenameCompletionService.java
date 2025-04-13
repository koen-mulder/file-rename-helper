package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.filename;

import java.util.List;

import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.AutocompleteDictionary;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.AutocompleteService;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.CompletionItem;
import com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete.SuggestionItem;
import com.google.common.collect.Lists;

public class FilenameCompletionService implements AutocompleteService {

    private static final int MAX_SUGGESTIONS = 5;

    private final AutocompleteDictionary dictionary;

    public FilenameCompletionService(AutocompleteDictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public CompletionItem getCompletion(String leadingText, String trailingText) {
        List<String> suggestions = Lists.newArrayList();
        List<String> words = List.of(leadingText.toLowerCase().split(" "));

        // Check if it has a partial word
        boolean hasPartialWord = leadingText.lastIndexOf(' ') != leadingText.length() - 1;
        // Check if it has a previous word
        boolean hasPreviousWord = (words.size() == 1 && !hasPartialWord) || (words.size() > 1);

        // Get completions for the last partial word
        if (hasPartialWord) {
            suggestions.addAll(getMatchingSuggestions(words.getLast()));
        }

        // Get completions for the previous word (and the partial word if it exists)
        if (hasPreviousWord) {
            String searchText = words.get(words.size() - (hasPartialWord ? 2 : 1)) + " ";
            if (hasPartialWord) {
                searchText += words.getLast();
            }

            suggestions.addAll(getMatchingSuggestions(searchText));
        }

        String suggestion = suggestions.isEmpty() ? "" : suggestions.getFirst();

        System.out.println(String.format("leadin: %s, trailing: %s, suggestion: %s", leadingText,
                trailingText, suggestion));

        return new CompletionItem(leadingText, suggestion, trailingText);
    }

    @Override
    public List<CompletionItem> getCompletions(String leadingText, String trailingText) {
        List<CompletionItem> suggestions = Lists.newArrayList();
        List<String> words = List.of(leadingText.toLowerCase().split(" "));

        // Check if it has a partial word
        boolean hasPartialWord = leadingText.lastIndexOf(' ') != leadingText.length() - 1;
        // Check if it has a previous word
        boolean hasPreviousWord = (words.size() == 1 && !hasPartialWord) || (words.size() > 1);

        // Get completions for the last partial word
        if (hasPartialWord) {
            suggestions.addAll(getMatchingSuggestions(words.getLast()).stream()
                    .map(completion -> new CompletionItem(leadingText, completion, trailingText))
                    .toList());
        }

        // Get completions for the previous word (and the partial word if it exists)
        if (hasPreviousWord) {
            String searchText = words.get(words.size() - (hasPartialWord ? 2 : 1)) + " ";
            if (hasPartialWord) {
                searchText += words.getLast();
            }

            suggestions.addAll(getMatchingSuggestions(searchText).stream()
                    .map(completion -> new CompletionItem(leadingText, completion, trailingText))
                    .toList());
        }

        // Remove duplicates and limit the number of suggestions
        suggestions = suggestions.stream().distinct().limit(MAX_SUGGESTIONS).toList();
        
        // Add static completions to have at least some suggestions
        if (suggestions.isEmpty()) {
            suggestions = dictionary.getCompletions().stream().limit(MAX_SUGGESTIONS)
                    .map(completion -> new CompletionItem(leadingText.trim() + " ", completion, trailingText))
                    .toList();
        }

        return suggestions;
    }

    private List<String> getMatchingSuggestions(String searchText) {
        return dictionary.getCompletions().stream()
                .filter(entry -> entry.toLowerCase().startsWith(searchText)
                        && !entry.equalsIgnoreCase(searchText))
                .map(entry -> entry.substring(searchText.length())).limit(MAX_SUGGESTIONS).toList();
    }

    @Override
    public List<CompletionItem> getCompletions() {
        List<CompletionItem> limitedList = dictionary.getCompletions().stream()
                .map(entry -> new CompletionItem("", entry, "")).limit(MAX_SUGGESTIONS).toList();
        return limitedList;
    }
    
    @Override
    public List<SuggestionItem> getSuggestions() {
        List<SuggestionItem> limitedSuggestionList = dictionary.getSuggestions().stream()
                .map(entry -> new SuggestionItem(entry)).limit(MAX_SUGGESTIONS).toList();
        return limitedSuggestionList;
    }
}
