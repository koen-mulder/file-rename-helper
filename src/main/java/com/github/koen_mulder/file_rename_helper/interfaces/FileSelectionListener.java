/**
 * 
 */
package main.java.interfaces;

/**
 * The FileSelectionListener interface is implemented by components that need to
 * react to file selection events of a {@link FileSelectionPublisher}.
 */
public interface FileSelectionListener {
    /**
     * This method will be called when a file is selected in the {@link FileSelectionPublisher}.
     * 
     * @param filePath The path of the selected file.
     */
    void onFileSelected(String filePath);
}
