package com.github.koen_mulder.file_rename_helper.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * Service for managing opening and saving projects as JSON.
 */
public class ProjectService {
    
    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    
    public final static String PROJECT_FILE_NAME = "file_rename_helper_project.json";
    
    private ProjectService() {
        // Prevent instantiation
    }
    
    public static void saveNewProject(Project project) throws ProjectException {
        saveProject(project);
    }
    
    public static Project openProject(File projectFile) throws ProjectException {
        Gson gson = getGson();
        
        Project fromJson;
        try (FileReader reader = new FileReader(projectFile)) {
            fromJson = gson.fromJson(reader,Project.class);
        } catch (FileNotFoundException e) {
            throw new ProjectException(String.format("Project file '%s' not found.",
                    projectFile.getAbsolutePath()), e);
        } catch (IOException e) {
            throw new ProjectException(String.format("Could not read project file '%s'.",
                    projectFile.getAbsolutePath()), e);
        } catch (JsonSyntaxException e) {
            throw new ProjectException(String.format("Could not parse project file '%s'.",
                    projectFile.getAbsolutePath()), e);
        } catch (JsonIOException e) {
            throw new ProjectException(String.format("Could not parse project file '%s'.",
                    projectFile.getAbsolutePath()), e);
        }
        
        if (fromJson == null) {
            throw new ProjectException(String.format(
                    "Could not parse project data from file. The file might be empty or not contain valid project structure. File: %s",
                    projectFile.getAbsolutePath()));
        }

        // Set transient field workspaceLocation - this is not saved in the project file because
        // it can be moved on disk.
        fromJson.initWorkspaceLocation(projectFile.toPath().getParent());

        return fromJson;
    }
    
    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(Path.class, new PathTypeAdapter())
                .create();
    }

    public static void saveProject(Project project) throws ProjectException {
        Gson gson = getGson();
        
        File projectFile = project.getWorkspaceLocation().resolve(PROJECT_FILE_NAME).toFile();
        try (FileWriter writer = new FileWriter(projectFile)) {
            gson.toJson(project, writer);
        } catch (IOException | JsonIOException e) {
            logger.error("Could not save project file '{}'.", projectFile.getAbsolutePath(), e);
            throw new ProjectException(String.format("Could not save project file '%s'.",
                    projectFile.getAbsolutePath()), e);
        } 
    }
}