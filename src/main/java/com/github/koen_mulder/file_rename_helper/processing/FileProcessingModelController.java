package com.github.koen_mulder.file_rename_helper.processing;

import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessedListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessedPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelPublisher;

/**
 * Controller class separating the {@link FileProcessingModel} from the rest of the application.
 * It controls access and passes on model events to listeners.
 */
public class FileProcessingModelController
        implements IFileProcessingModelPublisher, IFileProcessedPublisher {

    private final List<IFileProcessingModelListener> modelListeners = Lists.newArrayList();
    private final List<IFileProcessedListener> processedListeners = Lists.newArrayList();

    private FileProcessingModel model;

    public FileProcessingModelController() {
        model = new FileProcessingModel();

        // Listen to changes in the model to pass on to listeners
        model.addFileProcessingModelListener(new IFileProcessingModelListener() {

            @Override
            public void onTableChanged(FileProcessingModelEvent event) {
                fireModelChanged(event);
            }
        });

        // Listen to processed files to pass on to listeners
        model.addFileProcessedListener(new IFileProcessedListener() {

            @Override
            public void onFileProcessed(FileProcessingItem fileItem) {
                // Pass on
                notifyFileProcessedListeners(fileItem);
            }
        });
    }

    /**
     * Returns the value at <code>rowIndex</code>.
     *
     * @param rowIndex the row whose value is to be queried
     * @return the value Object at the specified row
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   ({@code index < 0 || index >= getRowCount()})
     * @see FileProcessingModel#getValueAt(int)
     */
    public FileProcessingItem getValueAt(int rowIndex) {
        return model.getValueAt(rowIndex);
    }

    /**
     * @return Returns all values in the model.
     * @see FileProcessingModel#getAllValues()
     */
    public List<FileProcessingItem> getAllValues() {
        return model.getAllValues();
    }

    /**
     * Bumps the item previously processed to the processed list, sets a new item to be processed
     * and returns that item.
     * 
     * @return item to be processed
     * @see FileProcessingModel#getNext()
     */
    public FileProcessingItem getNext() {
        return model.getNext();
    }

    /**
     * Adds a list of items to the backlog of the model.
     * 
     * @param items List of items to be added
     * @return {@code true} if the items were all added, false if one or more of the items is
     *         duplicate
     * @return {@code true} if the items were all added, {code false} if one or more of the items
     *         are duplicate
     * @throws IllegalArgumentException if any of the items {@code item} were {@code null}, had a
     *                                  {@code state} other than
     *                                  {@code EFileProcessingItemState.NEW}
     * @throws IllegalStateException    if any item could not be added
     * @see FileProcessingModel#add(List)
     */
    public boolean add(List<FileProcessingItem> items) {
        return model.add(items);
    }

    /**
     * Remove the item from the processed list and add it to the end of the re-queue list.
     *
     * @param item the element to be requeued
     * @return {@code true} if this model changed as a result of the call})
     * @throws IllegalArgumentException if item is {@code null}, the element does not exist in the
     *                                  processed list
     * @throws IllegalStateException    if the item could not be requeued
     * @see FileProcessingModel#requeue(FileProcessingItem)
     */
    public boolean requeue(FileProcessingItem activeFileItem) {
        return model.requeue(activeFileItem);
    }

    @Override
    public void addFileProcessingModelListener(IFileProcessingModelListener listener) {
        modelListeners.add(listener);
    }

    @Override
    public void removeFileProcessingModelListener(IFileProcessingModelListener listener) {
        modelListeners.remove(listener);
    }

    @Override
    public void fireModelChanged(FileProcessingModelEvent event) {
        // Create new event with controller as event source to separate model
        FileProcessingModelEvent newEvent = new FileProcessingModelEvent(this, event.getFirstRow(),
                event.getLastRow(), event.getType());
        for (IFileProcessingModelListener listener : modelListeners) {
            // Handle model update event in event thread
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    listener.onTableChanged(newEvent);
                }
            });
        }
    }

    @Override
    public void addFileProcessedListener(IFileProcessedListener listener) {
        processedListeners.add(listener);
    }

    @Override
    public void removeFileProcessedListener(IFileProcessedListener listener) {
        processedListeners.remove(listener);
    }

    @Override
    public void notifyFileProcessedListeners(FileProcessingItem fileItem) {
        for (IFileProcessedListener listener : processedListeners) {
            // Handle event in event thread
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    listener.onFileProcessed(fileItem);
                }
            });
        }
    }
}
