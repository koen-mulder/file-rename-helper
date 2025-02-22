package com.github.koen_mulder.file_rename_helper.processing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessedListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessedPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelPublisher;

/**
 * Model for the processing of files. This model keeps track of the items that have been processed,
 * the item that is currently being processed, the items that are re-queued for processing (high
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

        if (relativeIndex <= backlog.size() - 1) {
            return backlog.get(relativeIndex);
        }

        throw new IndexOutOfBoundsException(rowIndex);
    }

    /**
     * Adds a new row to the backlog of the model.
     * 
     * @param item item to be added
     * @return {@code true} if the item was added
     * @throws IllegalStateException if some property of this element prevents it from being added
     *                               to this list
     */
    public boolean add(FileProcessingItem item) {
        // Check for existing items
        if (current != null && current.equals(item)) {
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

        if (!backlog.add(item)) {
            throw new IllegalStateException(
                    "New item could not be added to backlog. Item: " + item);
        }
        fireRowInserted(getRowCount() - 1);
        return true;
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
     */
    public boolean requeue(int rowIndex) {
        if (rowIndex > processed.size() - 1) {
            throw new IllegalStateException(
                    "Cannot re-queue an item that has not been processed yet. Index: " + rowIndex);
        }
        FileProcessingItem item = processed.remove(rowIndex);
        fireRowDeleted(rowIndex);

        item.setState(EFileProcessingItemState.REQUEUED);
        int newRowIndex = processed.size() + requeued.size() + (current == null ? 0 : 1);
        if (!requeued.add(item)) {
            throw new IllegalStateException(
                    "Item removed from processed but could not be requeued. Item: " + item);
        }
        fireRowInserted(newRowIndex);
        return true;
    }

    /**
     * Remove the item from the processed list and add it to the end of the re-queue list.
     *
     * @param item the element to be re-queued
     * @return {@code true} if this model changed as a result of the call})
     * @throws NullPointerException  if the specified element is null
     * @throws IllegalStateException if the element does not exists, is not in the processed list or
     *                               if some property of this element prevents it from being
     */
    public boolean requeue(FileProcessingItem item) {
        int index = getIndexOf(item);
        if (index == -1) {
            throw new IllegalStateException(
                    "Trying to requeue an item that does not exist: " + item);
        }
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
     * Returns the index of the item currently being processed.
     * 
     * @return the index of the item currently being processed
     */
    public Integer getCurrentIndex() {
        if (current == null) {
            return null;
        }
        return processed.size();
    }

    /**
     * Removes the element at the specified rowIndex.
     * 
     * @param rowIndex the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IllegalStateException if the element is currently being processed
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public FileProcessingItem remove(int rowIndex) {
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
                throw new IllegalStateException(
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

        if (relativeIndex <= backlog.size() - 1) {
            FileProcessingItem removed = backlog.remove(relativeIndex);
            fireRowDeleted(rowIndex);
            return removed;
        }

        throw new IndexOutOfBoundsException(relativeIndex);
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

        // Search processed items
        Integer index = processed.indexOf(item);

        if (index != -1) {
            return index;
        }

        // Check current item
        if (current.equals(item)) {
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
