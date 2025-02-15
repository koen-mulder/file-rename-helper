package com.github.koen_mulder.file_rename_helper.processing;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

//TODO: Add missing JavaDoc
//TODO: Make thread safe
public class ProcessModel<T> {

    // List of items that have been processed
    private List<T> processed = Lists.newArrayList();
    
    // Item currently being processed
    private T current;
    
    // High priority items that are in queue for being reprocessed. 
    private List<T> requeued = Lists.newArrayList();
    
    // Normal priority items that are in queue for being processed
    private List<T> backlog = Lists.newArrayList();
    
    /**
     * Returns the number of rows in the model.
     */
    public int getRowCount() {
        return processed.size() + requeued.size() + backlog.size() + (current == null ? 0 : 1);
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code>.
     *
     * @param   rowIndex        the row whose value is to be queried
     * @return  the value Object at the specified cell
     */
    public T getValueAt(int rowIndex) {
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
    
    public boolean add(T object) {
        if (backlog.contains(object)) {
            // Do not allow duplicate objects to be added
            return false;
        }
        return backlog.add(object);
    }
    
    /**
     * Appends the element at the specified position in the processed list to the end of the re-queue list.
     *
     * @param index the index of the element to be re-queued
     * @return {@code true} if this model changed as a result of the
     *         call})
     * 
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being re-queued
     */
    public boolean requeue(int rowIndex) {
        if (rowIndex > processed.size() - 1 ) {
            throw new IllegalStateException(
                    "Cannot re-queue an item that has not been processed yet. Index: " + rowIndex);
        }
        T object = processed.remove(rowIndex);
        return requeued.add(object);
    }

    /**
     * Bumps the item previously processed to the processed list, sets a new item to be processed and returns that item.
     * 
     * @return item to be processed
     */
    public T getNext() {
        // Bump current processed
        if (current != null) {
            processed.add(current);
        }
        
        if (requeued.size() > 0) {
            current = requeued.remove(0);
            return current;
        }
        
        if (backlog.size() > 0) {
            current = backlog.remove(0);
            return current;
        }
        
        return null;
    }

    public T remove(int rowIndex) {
        int relativeIndex = rowIndex;
        
        if (relativeIndex <= processed.size() - 1) {
            return processed.remove(relativeIndex);
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
            return requeued.remove(relativeIndex);
        }
        
        // Adjust relative index
        relativeIndex -= requeued.size();
        
        if (relativeIndex <= backlog.size() - 1) {
            return backlog.remove(relativeIndex);
        }
        
        throw new IndexOutOfBoundsException(relativeIndex);
    }
}
