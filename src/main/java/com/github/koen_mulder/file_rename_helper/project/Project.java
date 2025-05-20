package com.github.koen_mulder.file_rename_helper.project;

import java.nio.file.Path;

public class Project {

    private final static String PROJECT_FILE_NAME = "file_rename_helper_project.json";
    
    private Path workspaceLocation;
    private Path archiveLocation;
    
    public Project() {
        // TODO: Dummy method while developing
        this.workspaceLocation = null;
        this.archiveLocation = null;
    }
    
    public Project(ProjectCreationData data) {
        this(data.workspaceLocation(),data.archiveLocation());
    }
    
    public Project(Path workspaceLocation, Path archiveLocation) {
        this.workspaceLocation = workspaceLocation;
        this.archiveLocation = archiveLocation;
    }
    
    public Path getArchiveLocation() {
        return archiveLocation;
    }
}
