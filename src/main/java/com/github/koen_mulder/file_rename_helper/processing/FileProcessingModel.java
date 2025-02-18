package com.github.koen_mulder.file_rename_helper.processing;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelPublisher;

//TODO: Add missing JavaDoc
//TODO: Make thread safe
public class FileProcessingModel implements IFileProcessingModelPublisher {

    private final List<IFileProcessingModelListener> listeners = Lists.newArrayList();

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
     * Returns the value for the cell at <code>columnIndex</code>.
     *
     * @param rowIndex the row whose value is to be queried
     * @return the value Object at the specified cell
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

    public boolean add(FileProcessingItem item) {
        // Check for existing items
        for (FileProcessingItem existingItem : backlog) {
            if (existingItem.getOriginalAbsoluteFilePath().equals(item.getOriginalAbsoluteFilePath())) {
                // Do not allow duplicate objects to be added
                return false;
            }
        }
        
        if (!backlog.add(item)) {
            throw new IllegalStateException("New item could not be added to backlog. Item: " + item);
        }
        fireRowInserted(getRowCount()-1);
        return true;
    }

    /**
     * Appends the element at the specified position in the processed list to the
     * end of the re-queue list.
     *
     * @param index the index of the element to be re-queued
     * @return {@code true} if this model changed as a result of the call})
     * 
     * @throws NullPointerException     if the specified element is null
     * @throws IllegalArgumentException if some property of this element prevents it
     *                                  from being re-queued
     */
    public boolean requeue(int rowIndex) {
        if (rowIndex > processed.size() - 1) {
            throw new IllegalStateException(
                    "Cannot re-queue an item that has not been processed yet. Index: " + rowIndex);
        }
        FileProcessingItem item = processed.remove(rowIndex);
        fireRowDeleted(rowIndex);

        if (!requeued.add(item)) {
            throw new IllegalStateException("Item removed from processed but could not be requeued. Item: " + item);
        }
        item.setState(EFileProcessingItemState.REQUEUED);
        int newRowIndex = processed.size() + requeued.size() + (current == null ? 0 : 1);
        fireRowInserted(newRowIndex);
        return true;
    }

    /**
     * Bumps the item previously processed to the processed list, sets a new item to
     * be processed and returns that item.
     * 
     * @return item to be processed
     */
    public FileProcessingItem getNext() {
        // Bump current processed
        if (current != null) {
            current.setState(EFileProcessingItemState.PROCESSED);
            // Move last current to processed
            processed.add(current);
            current = null;
            // Fire update to model listeners
            fireRowUpdated(processed.size() - 1);
            
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
    
    public Integer getCurrentIndex() {
        if (current == null) {
            return null;
        }
        return processed.size();
    }

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
                throw new IllegalStateException("Cannot remove the item currently being processed. Index: " + rowIndex);
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

    @Override
    public void addFileProcessingModelListener(IFileProcessingModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeFileProcessingModelListener(IFileProcessingModelListener listener) {
        listeners.remove(listener);
    }

    private void fireRowDeleted(int row) {
        fireRowsDeleted(row, row);
    }

    private void fireRowsDeleted(int firstRow, int lastRow) {
        fireModelChanged(new FileProcessingModelEvent(this, firstRow, lastRow, FileProcessingModelEvent.DELETE));
    }

    private void fireRowUpdated(int row) {
        fireRowsUpdated(row, row);
    }

    private void fireRowsUpdated(int firstRow, int lastRow) {
        fireModelChanged(new FileProcessingModelEvent(this, firstRow, lastRow, FileProcessingModelEvent.UPDATE));
    }

    private void fireRowInserted(int row) {
        fireRowsInserted(row, row);
    }

    private void fireRowsInserted(int firstRow, int lastRow) {
        fireModelChanged(new FileProcessingModelEvent(this, firstRow, lastRow, FileProcessingModelEvent.INSERT));
    }

    private void fireModelChanged(FileProcessingModelEvent event) {
        for (IFileProcessingModelListener listener : listeners) {
            listener.tableChanged(event);
        }
    }
}
