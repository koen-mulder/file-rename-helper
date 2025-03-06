package com.github.koen_mulder.file_rename_helper.processing.gui;

import javax.swing.SwingWorker;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelController;
import com.github.koen_mulder.file_rename_helper.suggestions.AIController;

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
                    synchronized(this){
                        wait(1000);
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Background interrupted");
                }
            } else {
                // Process item
                aiController.process(item);
            }
        }
    }

    @Override
    protected void done() {
        // Do nothing - This task runs forever
    }
}
