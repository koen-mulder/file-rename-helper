package com.github.koen_mulder.file_rename_helper.gui.rename.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.controller.AIController.FilenameSuggestions;
import com.github.koen_mulder.file_rename_helper.gui.EFormEvent;
import com.github.koen_mulder.file_rename_helper.interfaces.FormEventPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionPublisher;

public class FilenameSuggestionWorker extends SwingWorker<FilenameSuggestions, Integer> {

    private AIController aiController;
    private SuggestionPublisher suggestionPublisher;
    private FormEventPublisher formEventPublisher;
    private String filePath;

    public FilenameSuggestionWorker(AIController aiController, SuggestionPublisher suggestionPublisher,
            FormEventPublisher formEventPublisher, String filePath) {
        this.aiController = aiController;
        this.suggestionPublisher = suggestionPublisher;
        this.formEventPublisher = formEventPublisher;
        this.filePath = filePath;
    }

    @Override
    protected FilenameSuggestions doInBackground() throws Exception {
        return aiController.generatePossibleFileNames(filePath);
    }

    @Override
    protected void done() {
        try {
            FilenameSuggestions result = get();
            suggestionPublisher.notifySuggestionListeners(result);
            formEventPublisher.notifyFormEventListeners(EFormEvent.ENABLE);
            formEventPublisher.notifyFormEventListeners(EFormEvent.PROGRESS_STOP);
        } catch (InterruptedException e) {
            // TODO: Properly handle exceptions
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO: Properly handle exceptions
            e.printStackTrace();
        }
    }
}
