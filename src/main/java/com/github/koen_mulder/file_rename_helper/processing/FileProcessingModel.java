package com.github.koen_mulder.file_rename_helper.processing;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessedListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessedPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelPublisher;
import com.google.common.base.Preconditions;

/**
 * Model for the processing of files. This model keeps track of the items that have been processed,
 * the item that is currently being processed, the items that are requeued for processing (high
 * priority) and the items in the backlog (normal priority).
 */
//TODO: Make thread safe
public class FileProcessingModel implements IFileProcessingModelPublisher, IFileProcessedPublisher {

    private final List<IFileProcessingModelListener> modelListeners = Lists.newArrayList();
    private final List<IFileProcessedListener> processedListeners = Lists.newArrayList();

    // List of items that have been processed
    private List<FileProcessingItem> processed = Lists.newArrayList();

    // Item currently being processed
    private FileProcessingItem current;

    // High priority items that are in queue for being reprocessed.
    private List<FileProcessingItem> requeued = Lists.newArrayList();

    // Normal priority items that are in queue for being processed
    private List<FileProcessingItem> backlog = Lists.newArrayList();

    /**
     * Returns the number of rows in the model.
     */
    public int getRowCount() {
        return processed.size() + requeued.size() + backlog.size() + (current == null ? 0 : 1);
    }

    /**
     * Returns the value at <code>rowIndex</code>.
     *
     * @param rowIndex the row whose value is to be queried
     * @return the value Object at the specified row
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   ({@code index < 0 || index >= getRowCount()})
     */
    public FileProcessingItem getValueAt(int rowIndex) {
        // Check for out of bounds
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            throw new IndexOutOfBoundsException(rowIndex);
        }
        
        int relativeIndex = rowIndex;

        if (relativeIndex <= processed.size() - 1) {
            return processed.get(relativeIndex);
        }

        // Adjust relative index
        relativeIndex -= processed.size();

        if (current != null) {
            if (relativeIndex == 0) {
                return current;
            }

            // Adjust relative index
            relativeIndex--;
        }

        if (relativeIndex <= requeued.size() - 1) {
            return requeued.get(relativeIndex);
        }

        // Adjust relative index
        relativeIndex -= requeued.size();

        return Preconditions.checkNotNull(backlog.get(relativeIndex), String.format(
                "Could not find item (at index: %d). Model state must have changed after index out"
                        + " of bounds check.",
                rowIndex));
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
     */
    public boolean add(List<FileProcessingItem> items) {
        boolean result = true;
        for(FileProcessingItem item :items) {
            result &= add(item);
        }
        return result;
    }
    
    /**
     * Adds a new row to the backlog of the model.
     * 
     * @param item item to be added
     * @return {@code true} if the item was added, {code false} if the item is duplicate
     * @throws IllegalArgumentException if {@code item} is {@code null}, has a {@code state} other
     *                                  than {@code EFileProcessingItemState.NEW}
     * @throws IllegalStateException    if the item could not be added
     */
    public boolean add(FileProcessingItem item) {
        // Check for null input
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item to the model.");
        }
        
        if (item.getState() != EFileProcessingItemState.NEW) {
            throw new IllegalArgumentException(
                    "Cannot add an item that is not new to the model. Item: " + item);
        }

        // Check for existing items
        if (item.equals(current)) {
            // Do not allow duplicate objects to be added
            return false;
        }
        for (FileProcessingItem existingItem : processed) {
            if (existingItem.equals(item)) {
                // Do not allow duplicate objects to be added
                return false;
            }
        }
        for (FileProcessingItem existingItem : requeued) {
            if (existingItem.equals(item)) {
                // Do not allow duplicate objects to be added
                return false;
            }
        }
        for (FileProcessingItem existingItem : backlog) {
            if (existingItem.equals(item)) {
                // Do not allow duplicate objects to be added
                return false;
            }
        }

        // Add item to backlog
        Preconditions.checkState(backlog.add(item),
                "Failed to add item to the list. This is unexpected.");
        
        // Set state to backlog
        item.setState(EFileProcessingItemState.BACKLOG);
        
        // Fire update to model listeners
        fireRowInserted(getRowCount() - 1);
        
        // Return success
        return true;
    }

    /**
     * Appends the element at the specified position in the processed list to the end of the
     * re-queue list.
     *
     * @param index the index of the element to be requeued
     * @return {@code true} if this model changed as a result of the call
     * @throws IllegalArgumentException  if the element is not in the processed list
     * @throws IllegalStateException     if the item could not be requeued
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   ({@code index < 0 || index >= getRowCount()})
     */
    public boolean requeue(int rowIndex) {
        // Check for out of bounds
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            throw new IndexOutOfBoundsException(rowIndex);
        }
        
        // Check if index is in processed list
        if (rowIndex > processed.size() - 1) {
            throw new IllegalArgumentException(
                    "Cannot re-queue an item that has not been processed yet. Index: " + rowIndex);
        }
        
        // Remove item from processed list
        FileProcessingItem item = processed.remove(rowIndex);
        // Fire update to model listeners
        fireRowDeleted(rowIndex);

        // Set state to re-queued
        item.setState(EFileProcessingItemState.REQUEUED);
        // Calculate new index for event
        int newRowIndex = processed.size() + requeued.size() + (current == null ? 0 : 1);
        
        // Add item to re-queue list
        Preconditions.checkState(requeued.add(item),
                "Failed to add item to the list. This is unexpected.");

        // Fire update to model listeners
        fireRowInserted(newRowIndex);
        return true;
    }

    /**
     * Remove the item from the processed list and add it to the end of the re-queue list.
     *
     * @param item the element to be requeued
     * @return {@code true} if this model changed as a result of the call})
     * @throws IllegalArgumentException if item is {@code null}, the element does not exist in the
     *                                  processed list
     * @throws IllegalStateException    if the item could not be requeued
     */
    public boolean requeue(FileProcessingItem item) {
        // Check for null input
        if (item == null) {
            throw new IllegalArgumentException("Cannot requeue a null item.");
        }
        // Find index of item
        int index = getIndexOf(item);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "Trying to requeue an item that does not exist: " + item);
        }
        // Requeue item
        return requeue(index);
    }

    /**
     * Bumps the item previously processed to the processed list, sets a new item to be processed
     * and returns that item.
     * 
     * @return item to be processed
     */
    public FileProcessingItem getNext() {
        // Bump current processed
        if (current != null) {
            current.setState(EFileProcessingItemState.PROCESSED);
            // Move last current to processed
            processed.add(current);
            // Fire update to model listeners
            fireRowUpdated(processed.size() - 1);
            notifyFileProcessedListeners(current);
            // Clear current
            current = null;
        }

        if (requeued.size() > 0) {
            current = requeued.remove(0);
            current.setState(EFileProcessingItemState.PROCESSING);
            fireRowUpdated(processed.size());
            return current;
        }

        if (backlog.size() > 0) {
            current = backlog.remove(0);
            current.setState(EFileProcessingItemState.PROCESSING);
            fireRowUpdated(processed.size());
            return current;
        }

        return null;
    }

    /**
     * Removes the element at the specified rowIndex.
     * 
     * @param rowIndex the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IllegalArgumentException if the element is currently being processed
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   ({@code index < 0 || index >= getRowCount()})
     */
    public FileProcessingItem remove(int rowIndex) {
        // Check for out of bounds
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            throw new IndexOutOfBoundsException(rowIndex);
        }
        
        int relativeIndex = rowIndex;

        if (relativeIndex <= processed.size() - 1) {
            FileProcessingItem removed = processed.remove(relativeIndex);
            fireRowDeleted(rowIndex);
            return removed;
        }

        // Adjust relative index
        relativeIndex -= processed.size();

        if (current != null) {
            if (relativeIndex == 0) {
                throw new IllegalArgumentException(
                        "Cannot remove the item currently being processed. Index: " + rowIndex);
            }

            // Adjust relative index
            relativeIndex--;
        }

        if (relativeIndex <= requeued.size() - 1) {
            FileProcessingItem removed = requeued.remove(relativeIndex);
            fireRowDeleted(rowIndex);
            return removed;
        }

        // Adjust relative index
        relativeIndex -= requeued.size();

        // Remove item from backlog
        FileProcessingItem removed = backlog.remove(relativeIndex);
        fireRowDeleted(rowIndex);
        return removed;
    }

    /**
     * @return Returns all values in the model.
     */
    public List<FileProcessingItem> getAllValues() {
        List<FileProcessingItem> combinedList = new ArrayList<>();
        combinedList.addAll(processed);
        if (current != null) {
            combinedList.add(current);
        }
        combinedList.addAll(requeued);
        combinedList.addAll(backlog);

        return combinedList;
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list, or -1 if
     * this list does not contain the element.
     * 
     * @param item element to search for
     * @return the index of the first occurrence of the specified element in this list, or -1 if
     *         this list does not contain the element
     */
    public Integer getIndexOf(FileProcessingItem item) {
        // Check for null input
        if (item == null) {
            throw new IllegalArgumentException("Cannot get the index of a null item.");
        }
        
        // Search processed items
        Integer index = processed.indexOf(item);

        if (index != -1) {
            return index;
        }

        // Check current item
        if (item.equals(current)) {
            return processed.size();
        }

        // Search processed items
        index = requeued.indexOf(item);

        if (index != -1) {
            return processed.size() + (current == null ? 0 : 1) + index;
        }

        // Search processed items
        index = backlog.indexOf(item);

        if (index != -1) {
            return processed.size() + requeued.size() + (current == null ? 0 : 1) + index;
        }

        return -1;
    }

    /**
     * Note: Only the {@link FileProcessingModelController} should listen to this model. If you need
     * to listen to changes in the model, listen to the {@link FileProcessingModelController}
     * instead.
     */
    @Override
    public void addFileProcessingModelListener(IFileProcessingModelListener listener) {
        modelListeners.add(listener);
    }

    @Override
    public void removeFileProcessingModelListener(IFileProcessingModelListener listener) {
        modelListeners.remove(listener);
    }

    private void fireRowDeleted(int row) {
        fireRowsDeleted(row, row);
    }

    private void fireRowsDeleted(int firstRow, int lastRow) {
        fireModelChanged(new FileProcessingModelEvent(this, firstRow, lastRow,
                FileProcessingModelEvent.DELETE));
    }

    private void fireRowUpdated(int row) {
        fireRowsUpdated(row, row);
    }

    private void fireRowsUpdated(int firstRow, int lastRow) {
        fireModelChanged(new FileProcessingModelEvent(this, firstRow, lastRow,
                FileProcessingModelEvent.UPDATE));
    }

    private void fireRowInserted(int row) {
        fireRowsInserted(row, row);
    }

    private void fireRowsInserted(int firstRow, int lastRow) {
        fireModelChanged(new FileProcessingModelEvent(this, firstRow, lastRow,
                FileProcessingModelEvent.INSERT));
    }

    @Override
    public void fireModelChanged(FileProcessingModelEvent event) {
        for (IFileProcessingModelListener listener : modelListeners) {
            listener.onTableChanged(event);
        }
    }

    /**
     * Note: Only the {@link FileProcessingModelController} should listen to this model. If you need
     * to listen to changes in the model, listen to the {@link FileProcessingModelController}
     * instead.
     */
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
            listener.onFileProcessed(fileItem);
        }
    }
}
