package com.github.koen_mulder.file_rename_helper.suggestions;

import java.util.List;

/**
 * Suggestions for renaming a file. Contains a list of possible filenames, file paths, relevant
 * words or relevant dates.
 */
public record Suggestions(List<String> suggestions) {
    
}
