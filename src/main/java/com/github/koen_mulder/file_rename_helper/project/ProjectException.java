package com.github.koen_mulder.file_rename_helper.project;

/**
 * Exception thrown when there is an error related to (opening or saving) a project.
 */
@SuppressWarnings("serial") // Same-version serialization only
public class ProjectException extends Exception {

    public ProjectException(String message) {
        super(message);
    }

    public ProjectException(String message, Throwable exception) {
        super(message, exception);
    }

}
