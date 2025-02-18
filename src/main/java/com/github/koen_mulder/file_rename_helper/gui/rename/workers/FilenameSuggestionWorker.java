package com.github.koen_mulder.file_rename_helper.gui.rename.workers;

import javax.swing.SwingWorker;

import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModel;

public class FilenameSuggestionWorker extends SwingWorker<Void, Void> {

    private AIController aiController;
    private FileProcessingModel processModel;

    public FilenameSuggestionWorker(AIController aiController, FileProcessingModel processModel) {
        this.aiController = aiController;
        this.processModel = processModel;
    }

    @Override
    protected Void doInBackground() throws Exception {
        while (true) {
            FileProcessingItem item = processModel.getNext();
            
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
