package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete;

import java.util.List;

/**
 * Interface for a service that provides autocomplete functionality consisting of text
 * completions and complete text suggestions.
 */
public interface AutocompleteService {

    /**
     * Returns the completion for the given leadingText and trailingText.
     *
     * @param leadingText  The leadingText to complete.
     * @param trailingText The trailingText to possibly take into account.
     * @return The completion that fits between the leadingText and trailingText.
     */
    CompletionItem getCompletion(String leadingText, String trailingText);

    /**
     * Returns a list of completions for the given leadingText and trailingText.
     *
     * @param leadingText  The leadingText to complete.
     * @param trailingText The trailingText to possibly take into account.
     * @return A list of completions that fit between the leadingText and trailingText.
     */
    List<CompletionItem> getCompletions(String leadingText, String trailingText);

    /**
     * Returns a limited list of completions.
     *
     * @return A list of completions.
     */
    List<CompletionItem> getCompletions();

    
    /**
     * Returns a list of complete text suggestions.
     *
     * @return A list of complete text suggestions.
     */
    List<SuggestionItem> getSuggestions();
}
