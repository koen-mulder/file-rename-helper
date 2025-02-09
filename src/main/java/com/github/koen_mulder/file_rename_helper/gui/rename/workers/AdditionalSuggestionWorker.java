package com.github.koen_mulder.file_rename_helper.gui.rename.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.controller.AIController.FilenameSuggestions;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionPublisher;

public class AdditionalSuggestionWorker extends SwingWorker<FilenameSuggestions, Integer> {

    private AIController aiController;
    private SuggestionPublisher suggestionPublisher;

    public AdditionalSuggestionWorker(AIController aiController, SuggestionPublisher suggestionPublisher) {
        this.aiController = aiController;
        this.suggestionPublisher = suggestionPublisher;
    }

    @Override
    protected FilenameSuggestions doInBackground() throws Exception {
        return aiController.getAdditionalFilenameSuggestions();
    }

    @Override
    protected void done() {
        try {
            FilenameSuggestions result = get();
            suggestionPublisher.notifySuggestionListeners(result);
        } catch (InterruptedException e) {
            // TODO: Properly handle exceptions
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO: Properly handle exceptions
            e.printStackTrace();
        }
    }
}
