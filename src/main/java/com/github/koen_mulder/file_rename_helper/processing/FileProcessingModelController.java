package com.github.koen_mulder.file_rename_helper.processing;

import java.awt.EventQueue;
import java.util.List;

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
     */
    public FileProcessingItem getValueAt(int rowIndex) {
        return model.getValueAt(rowIndex);
    }

    /**
     * @return all values in the model
     */
    public List<FileProcessingItem> getAllValues() {
        return model.getAllValues();
    }

    /**
     * Bumps the item previously processed to the processed list, sets a new item to be processed
     * and returns that item.
     * 
     * @return item to be processed
     */
    public FileProcessingItem getNext() {
        return model.getNext();
    }

    /**
     * Adds a new row to the backlog of the model.
     * 
     * @param item item to be added
     * @return {@code true} if the item was added
     * @throws IllegalStateException if some property of this element prevents it from being added
     *                               to this list
     * @see FileProcessingModel#add(FileProcessingItem)
     */
    public boolean add(FileProcessingItem fileProcessingItem) {
        return model.add(fileProcessingItem);
    }

    /**
     * Appends the element at the specified position in the processed list to the end of the
     * re-queue list.
     *
     * @param index the index of the element to be re-queued
     * @return {@code true} if this model changed as a result of the call})
     * @throws NullPointerException  if the specified element is null
     * @throws IllegalStateException if the element is not in the processed list or if some property
     *                               of this element prevents it from being
     * @see FileProcessingModel#requeue(int)
     */
    public boolean requeue(int rowIndex) {
        return model.requeue(0);
    }

    /**
     * Remove the item from the processed list and add it to the end of the re-queue list.
     *
     * @param item the element to be re-queued
     * @return {@code true} if this model changed as a result of the call})
     * @throws NullPointerException  if the specified element is null
     * @throws IllegalStateException if the element does not exists, is not in the processed list or
     *                               if some property of this element prevents it from being
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
            EventQueue.invokeLater(new Runnable() {
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
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    listener.onFileProcessed(fileItem);
                }
            });
        }
    }
}
