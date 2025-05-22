package com.github.koen_mulder.file_rename_helper.project;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingController;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionPublisher;
import com.github.koen_mulder.file_rename_helper.suggestions.AIController;

public class ActiveProjectController {
    
    private final Project currentProject;

    private FileProcessingController processingController;
    private AIController aiController;
    
    public ActiveProjectController(Project currentProject) {
        if (currentProject == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }
        this.currentProject = currentProject;
        
        processingController = new FileProcessingController();
        aiController = new AIController();
    }

    public FileProcessingController getProcessingController() {
        return processingController;
    }

    public AIController getAIController() {
        return aiController;
    }
    
    public IOpenFileActionPublisher getOpenFileActionPublisher() {
        return processingController.getOpenFileActionPublisher();
    }

    public Project getCurrentProject() {
        return currentProject;
    }
    
}
