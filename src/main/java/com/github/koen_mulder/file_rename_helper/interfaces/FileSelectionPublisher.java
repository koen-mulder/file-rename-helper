/**
 * 
 */
package main.java.interfaces;

/**
 * The FileSelectionPublisher interface is implemented by classes that manage 
 * the file selection process and notify registered {@link FileSelectionListener}s when a file is selected.
 */
public interface FileSelectionPublisher {
    /**
     * Adds a listener that will be notified when a file is selected.
     * 
     * @param listener The listener to be added.
     */
    void addFileSelectionListener(FileSelectionListener listener);

    /**
     * Removes a previously added listener.
     * 
     * @param listener The listener to be removed.
     */
    void removeFileSelectionListener(FileSelectionListener listener);

    /**
     * Notifies all registered listeners of the file selection event.
     * 
     * @param filePath The path of the selected file.
     */
    void notifyFileSelectionListeners(String filePath);
}
