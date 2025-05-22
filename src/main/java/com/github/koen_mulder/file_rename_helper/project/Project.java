package com.github.koen_mulder.file_rename_helper.project;

import java.nio.file.Path;

import com.google.common.base.Objects;

/**
 * Represents a project in the file rename helper application.
 */
public class Project {

    /**
     * The workspace location of the project. This is the location where the project files are
     * stored.
     * 
     * This field is transient because it is not serialized when the project is saved to a file. We
     * set the workspace location to the actual location of the project file.
     */
    private transient Path workspaceLocation;
    
    private transient boolean isChanged = false;
    
    /**
     * The archive location of the project. This is the location used for recommending archive
     * locations of files.
     */
    private Path archiveLocation;
    
    public Project(ProjectCreationData data) {
        this(data.workspaceLocation(),data.archiveLocation());
    }
    
    public Project(Path workspaceLocation, Path archiveLocation) {
        this.workspaceLocation = workspaceLocation;
        this.archiveLocation = archiveLocation;
    }
    
    /**
     * Initializes the workspace location of the project after opening a project.
     * 
     * @param workspaceLocation The path to the workspace location.
     */
    public void initWorkspaceLocation(Path workspaceLocation) {
        this.workspaceLocation = workspaceLocation;
    }
    
    public Path getWorkspaceLocation() {
        return workspaceLocation;
    }

    public Path getArchiveLocation() {
        return archiveLocation;
    }
    
    public void setArchiveLocation(Path archiveLocation) {
        if (Objects.equal(this.archiveLocation, archiveLocation)) {
            isChanged = true;
        }
        this.archiveLocation = archiveLocation;
    }
    
    public boolean isChanged() {
        return isChanged;
    }
    
}
