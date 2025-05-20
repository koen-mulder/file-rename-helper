package com.github.koen_mulder.file_rename_helper.processing;

import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionPublisher;

public class FileProcessingController {

    private final FileProcessingModelController fileProcessingModelController;
    private final IOpenFileActionPublisher openFileActionPublisher;

    public FileProcessingController() {
        fileProcessingModelController = new FileProcessingModelController();
        openFileActionPublisher = new OpenFileActionPublisher();
    }

    public FileProcessingModelController getFileProcessingModelController() {
        return fileProcessingModelController;
    }

    public IOpenFileActionPublisher getOpenFileActionPublisher() {
        return openFileActionPublisher;
    }
}
