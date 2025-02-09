package com.github.koen_mulder.file_rename_helper.controller;

import java.util.ArrayList;

import com.github.koen_mulder.file_rename_helper.controller.AIController.FilenameSuggestions;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionListener;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionPublisher;
import com.google.common.collect.Lists;

public class SuggestionController implements SuggestionPublisher {

    private ArrayList<SuggestionListener> suggestionListeners = Lists.newArrayList();

    @Override
    public void addSuggestionListener(SuggestionListener listener) {
        this.suggestionListeners.add(listener);
    }

    @Override
    public void removeSuggestionListener(SuggestionListener listener) {
        this.suggestionListeners.remove(listener);

    }

    @Override
    public void notifySuggestionListeners(FilenameSuggestions suggestions) {
        for (SuggestionListener listener : suggestionListeners) {
            listener.onSuggestionsGenerated(suggestions);
        }
    }

}
