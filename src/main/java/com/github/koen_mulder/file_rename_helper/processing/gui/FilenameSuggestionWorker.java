package com.github.koen_mulder.file_rename_helper.processing.gui;

import javax.swing.SwingWorker;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelController;
import com.github.koen_mulder.file_rename_helper.suggestions.AIController;
import com.github.koen_mulder.file_rename_helper.suggestions.IAISuggestionService;
import com.github.koen_mulder.file_rename_helper.suggestions.Suggestions;

/**
 * Worker class to suggest filenames for files in the processing queue.
 */
public class FilenameSuggestionWorker extends SwingWorker<Void, Void> {

    private AIController aiController;
    private FileProcessingModelController fileProcessingModelController;

    public FilenameSuggestionWorker(AIController aiController,
            FileProcessingModelController fileProcessingModelController) {

        this.aiController = aiController;
        this.fileProcessingModelController = fileProcessingModelController;
    }

    @Override
    protected Void doInBackground() throws Exception {
        while (true) {
            FileProcessingItem item = fileProcessingModelController.getNext();

            if (item == null) {
                // Empty queue wait a bit
                try {
                    synchronized (this) {
                        wait(1000);
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Background interrupted");
                }
            } else {
                // Get AI service
                IAISuggestionService aiService = aiController.getAiService();

                if (item.getFilenameSuggestions().isEmpty()) {

                    // Get filename suggestions
                    Suggestions filenameSuggestions = aiService.getFilenameSuggestions(item);
                    item.addFilenameSuggestions(filenameSuggestions);

                    // Get important keywords suggestions
                    Suggestions keywordSuggestions = aiService
                            .getImportantKeywordsSuggestions(item);
                    item.addKeywordSuggestions(keywordSuggestions);

                    // Get important date suggestions
                    Suggestions dateSuggestions = aiService.getImportantDatesSuggestions(item);
                    item.addDateSuggestions(dateSuggestions);

                    // Get file path suggestions
                    Suggestions filepathSuggestions = aiService.getFilepathSuggestions(item);
                    item.addFilepathSuggestions(filepathSuggestions);

                } else {
                    // Get additional filename suggestions
                    Suggestions filenameSuggestions = aiService
                            .getAdditionalFilenameSuggestions(item);
                    item.addFilenameSuggestions(filenameSuggestions);

                    // Get additional important keywords suggestions
                    Suggestions keywordSuggestions = aiService
                            .getAdditionalImportantKeywordsSuggestions(item);
                    item.addKeywordSuggestions(keywordSuggestions);

                    // Get additional important date suggestions
                    Suggestions dateSuggestions = aiService
                            .getAdditionalImportantDatesSuggestions(item);
                    item.addDateSuggestions(dateSuggestions);

                    // Get additional file path suggestions
                    Suggestions filepathSuggestions = aiService
                            .getAdditionalFilepathSuggestions(item);
                    item.addFilepathSuggestions(filepathSuggestions);
                }
            }
        }
    }

    @Override
    protected void done() {
        // Do nothing - This task runs forever
    }
}
