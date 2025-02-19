package com.github.koen_mulder.file_rename_helper.processing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link FileProcessingModel}.
 */
class FileProcessingModelTest {

    @Test
    void testGetRowCount() {
        FileProcessingModel model = new FileProcessingModel();

        // Empty model
        // State [processed = (), current = (), re-queued = (), backlog = ()]
        assertEquals(0, model.getRowCount());

        // Add first item
         model.add(new FileProcessingItem("C:\\10", "10"));;
        // State [processed = (), current = (), re-queued = (), backlog = (10)]
        assertEquals(1, model.getRowCount());
        
        // Add three more items
         model.add(new FileProcessingItem("C:\\11", "11"));;
         model.add(new FileProcessingItem("C:\\12", "12"));;
         model.add(new FileProcessingItem("C:\\13", "13"));;
        // State [processed = (), current = (), re-queued = (), backlog = (10, 11, 12, 13)]
        assertEquals(4, model.getRowCount());
        
        // Process items - changes item distribution but keeps the total amount of items the same
        model.getNext();
        model.getNext();
        model.getNext();
        // State [processed = (10, 11), current = (12), re-queued = (), backlog = (13)]
        assertEquals(4, model.getRowCount());

        // Re-queue an item - changes item distribution keeps the total amount of items the same
        model.requeue(1);
        // State [processed = (10), current = (12), re-queued = (11), backlog = (13)]
        assertEquals(4, model.getRowCount());
        
        // Remove item from processed - affects the total amount of items
        model.remove(0);
        // State [processed = (0), current = (12), re-queued = (11), backlog = (13)]
        assertEquals(3, model.getRowCount());

        // Remove item from re-queued - affects the total amount of items
        model.remove(1);
        // State [processed = (0), current = (12), re-queued = (), backlog = (13)]
        assertEquals(2, model.getRowCount());

        // Remove item from backlog - affects the total amount of items
        model.remove(1);
        // State [processed = (0), current = (12), re-queued = (), backlog = ()]
        assertEquals(1, model.getRowCount());
    }

    @Test
    void testGetValueAt() {
        FileProcessingModel model = new FileProcessingModel();

        // Add first item
         model.add(new FileProcessingItem("C:\\10", "10"));;
        // State [processed = (), current = (), re-queued = (), backlog = (10)]
        assertFilePathEquals("C:\\10", model.getValueAt(0));

        // Add four more items
         model.add(new FileProcessingItem("C:\\11", "11"));;
         model.add(new FileProcessingItem("C:\\12", "12"));;
         model.add(new FileProcessingItem("C:\\13", "13"));;
        // State [processed = (), current = (), re-queued = (), backlog = (10, 11, 12,
        // 13)]
        assertFilePathEquals("C:\\10", model.getValueAt(0));
        assertFilePathEquals("C:\\11", model.getValueAt(1));
        assertFilePathEquals("C:\\12", model.getValueAt(2));
        assertFilePathEquals("C:\\13", model.getValueAt(3));

        // Process some items - keeps the order of items the same
        model.getNext();
        model.getNext();
        model.getNext();
        // State [processed = (10, 11), current = (12), re-queued = (), backlog = (13)]
        assertFilePathEquals("C:\\10", model.getValueAt(0));
        assertFilePathEquals("C:\\11", model.getValueAt(1));
        assertFilePathEquals("C:\\12", model.getValueAt(2));
        assertFilePathEquals("C:\\13", model.getValueAt(3));

        // Re-queue an item - changes the order of the items
        model.requeue(1);
        // State [processed = (10), current = (12), re-queued = (11), backlog = (13)]
        assertFilePathEquals("C:\\10", model.getValueAt(0));
        assertFilePathEquals("C:\\12", model.getValueAt(1));
        assertFilePathEquals("C:\\11", model.getValueAt(2));
        assertFilePathEquals("C:\\13", model.getValueAt(3));

        // Re-queue an item - changes the order of the items
        model.requeue(0);
        // State [processed = (), current = (12), re-queued = (11, 10), backlog = (13)]
        assertFilePathEquals("C:\\12", model.getValueAt(0));
        assertFilePathEquals("C:\\11", model.getValueAt(1));
        assertFilePathEquals("C:\\10", model.getValueAt(2));
        assertFilePathEquals("C:\\13", model.getValueAt(3));

        // Get an index that is out of range
        IndexOutOfBoundsException thrown = assertThrowsExactly(IndexOutOfBoundsException.class,
                () -> model.getValueAt(4), "Expected there would be no item at index 4 but there was.");
        assertEquals("Index out of range: 4", thrown.getMessage());
    }

    @Test
    void add() {
        FileProcessingModel model = new FileProcessingModel();

        // Empty model
        // State [processed = (), current = (), re-queued = (), backlog = ()]
        assertEquals(0, model.getRowCount());

        // Add first item
         model.add(new FileProcessingItem("C:\\10", "10"));;
        // State [processed = (), current = (), re-queued = (), backlog = (10)]
        assertEquals(1, model.getRowCount());

        // Add same item again - should not be added
        assertFalse(model.add(new FileProcessingItem("C:\\10", "10")));
        // State [processed = (), current = (), re-queued = (), backlog = (10)]
        assertEquals(1, model.getRowCount());
    }

    @Test
    void requeue() {
        FileProcessingModel model = new FileProcessingModel();
        
        // Add items
         model.add(new FileProcessingItem("C:\\10", "10"));;
         model.add(new FileProcessingItem("C:\\11", "11"));;
         model.add(new FileProcessingItem("C:\\12", "12"));;
         model.add(new FileProcessingItem("C:\\13", "13"));;
         model.add(new FileProcessingItem("C:\\14", "14"));;
         model.add(new FileProcessingItem("C:\\15", "15"));;
        
        // Process some items
        model.getNext();
        model.getNext();
        model.getNext();
        model.getNext();
        
        // State [processed = (10, 11, 12), current = (13), re-queued = (), backlog = (14, 15)]
        // Assert state
        assertFilePathEquals("C:\\10", model.getValueAt(0));
        assertFilePathEquals("C:\\11", model.getValueAt(1));
        assertFilePathEquals("C:\\12", model.getValueAt(2));
        assertFilePathEquals("C:\\13", model.getValueAt(3));
        assertFilePathEquals("C:\\14", model.getValueAt(4));
        assertFilePathEquals("C:\\15", model.getValueAt(5));
        
        model.requeue(2);
        model.requeue(0);
        model.requeue(0);
        // State [processed = (), current = (13), re-queued = (12, 10, 11), backlog = (14, 15)]
        // Assert state
        assertFilePathEquals("C:\\13", model.getValueAt(0));
        assertFilePathEquals("C:\\12", model.getValueAt(1));
        assertFilePathEquals("C:\\10", model.getValueAt(2));
        assertFilePathEquals("C:\\11", model.getValueAt(3));
        assertFilePathEquals("C:\\14", model.getValueAt(4));
        assertFilePathEquals("C:\\15", model.getValueAt(5));
        
        // Try to re-queue the item currently being processed
        // State [processed = (), current = (13), re-queued = (12, 10, 11), backlog = (14, 15)]
        IllegalStateException thrownCurrentlyProcessed = assertThrowsExactly(IllegalStateException.class,
                () -> model.requeue(0), "Did not expect that the item currently being processed could be re-queued.");
        assertEquals("Cannot re-queue an item that has not been processed yet. Index: 0",
                thrownCurrentlyProcessed.getMessage());
        
        // Try to re-queue the item currently being re-queued
        // State [processed = (), current = (13), re-queued = (12, 10, 11), backlog = (14, 15)]
        IllegalStateException thrownAlreadyRequeued = assertThrowsExactly(IllegalStateException.class,
                () -> model.requeue(2), "Did not expect that the item currently re-queued could be re-queued.");
        assertEquals("Cannot re-queue an item that has not been processed yet. Index: 2",
                thrownAlreadyRequeued.getMessage());
        
        // Try to re-queue the item currently being re-queued
        // State [processed = (), current = (13), re-queued = (12, 10, 11), backlog = (14, 15)]
        IllegalStateException thrownStillOnBacklog = assertThrowsExactly(IllegalStateException.class,
                () -> model.requeue(4), "Did not expect that the item currently on the backlog could be re-queued.");
        assertEquals("Cannot re-queue an item that has not been processed yet. Index: 4",
                thrownStillOnBacklog.getMessage());
        
        // Assert state unchanged
        assertFilePathEquals("C:\\13", model.getValueAt(0));
        assertFilePathEquals("C:\\12", model.getValueAt(1));
        assertFilePathEquals("C:\\10", model.getValueAt(2));
        assertFilePathEquals("C:\\11", model.getValueAt(3));
        assertFilePathEquals("C:\\14", model.getValueAt(4));
        assertFilePathEquals("C:\\15", model.getValueAt(5));
    }

    @Test
    void getNext() {
        FileProcessingModel model = new FileProcessingModel();
        
        // Add items
         model.add(new FileProcessingItem("C:\\10", "10"));;
         model.add(new FileProcessingItem("C:\\11", "11"));;
         model.add(new FileProcessingItem("C:\\12", "12"));;
         model.add(new FileProcessingItem("C:\\13", "13"));;
         model.add(new FileProcessingItem("C:\\14", "14"));;
         model.add(new FileProcessingItem("C:\\15", "15"));;
        
        // Process some items and assert the correct item is returned
         assertFilePathEquals("C:\\10", model.getNext());
         assertFilePathEquals("C:\\10", model.getValueAt(0));
         assertFilePathEquals("C:\\11", model.getNext());
         assertFilePathEquals("C:\\11", model.getValueAt(1));
         assertFilePathEquals("C:\\12", model.getNext());
         assertFilePathEquals("C:\\12", model.getValueAt(2));
         assertFilePathEquals("C:\\13", model.getNext());
         assertFilePathEquals("C:\\13", model.getValueAt(3));
         assertFilePathEquals("C:\\14", model.getNext());
         assertFilePathEquals("C:\\14", model.getValueAt(4));
        
        // State [processed = (10, 11, 12, 13), current = (14), re-queued = (), backlog = (15)]
        // Assert state
         assertFilePathEquals("C:\\10", model.getValueAt(0));
         assertFilePathEquals("C:\\11", model.getValueAt(1));
        assertFilePathEquals("C:\\12", model.getValueAt(2));
        assertFilePathEquals("C:\\13", model.getValueAt(3));
        assertFilePathEquals("C:\\14", model.getValueAt(4));
        assertFilePathEquals("C:\\15", model.getValueAt(5));
        
        // Re-queue items
        assertTrue(model.requeue(3));
        assertTrue(model.requeue(0));
        assertTrue(model.requeue(1));
        
        // State [processed = (11), current = (14), re-queued = (13, 10, 12), backlog = (15)]
        // Assert state
        assertFilePathEquals("C:\\11", model.getValueAt(0));
        assertFilePathEquals("C:\\14", model.getValueAt(1));
        assertFilePathEquals("C:\\13", model.getValueAt(2));
        assertFilePathEquals("C:\\10", model.getValueAt(3));
        assertFilePathEquals("C:\\12", model.getValueAt(4));
        assertFilePathEquals("C:\\15", model.getValueAt(5));
        
        // Process some items and assert the correct item is returned
        assertFilePathEquals("C:\\13", model.getNext());
        // State [processed = (11, 14), current = (13), re-queued = (10, 12), backlog = (15)]
        // Remove item and see if correct one is removed
        assertFilePathEquals("C:\\10", model.remove(3));
       
        // State [processed = (11, 14), current = (13), re-queued = (12), backlog = (15)]
        // Assert state
        assertFilePathEquals("C:\\11", model.getValueAt(0));
        assertFilePathEquals("C:\\14", model.getValueAt(1));
        assertFilePathEquals("C:\\13", model.getValueAt(2));
        assertFilePathEquals("C:\\12", model.getValueAt(3));
        assertFilePathEquals("C:\\15", model.getValueAt(4));

        // State [processed = (11, 14), current = (13), re-queued = (12), backlog = (15)]
        // Process last items
        assertFilePathEquals("C:\\12", model.getNext());
        assertFilePathEquals("C:\\15", model.getNext());
        model.getNext();
        
        // State [processed = (11, 14, 13, 12, 15), current = (), re-queued = (), backlog = ()]
        // Assert state
        assertFilePathEquals("C:\\11", model.getValueAt(0));
        assertFilePathEquals("C:\\14", model.getValueAt(1));
        assertFilePathEquals("C:\\13", model.getValueAt(2));
        assertFilePathEquals("C:\\12", model.getValueAt(3));
        assertFilePathEquals("C:\\15", model.getValueAt(4));
    }
    
    @Test
    void remove() {
        FileProcessingModel model = new FileProcessingModel();
        
        // Get an index that is out of range
        IndexOutOfBoundsException thrownOutOfBounds = assertThrowsExactly(IndexOutOfBoundsException.class,
                () -> model.remove(0), "Expected there would be no item to remove from an empty model.");
        assertEquals("Index out of range: 0", thrownOutOfBounds.getMessage());
        
        // Add first item
         model.add(new FileProcessingItem("C:\\10", "10"));;
        // Assert item removed
        assertFilePathEquals("C:\\10", model.remove(0));
        assertEquals(0, model.getRowCount());

        // Add items
         model.add(new FileProcessingItem("C:\\11", "11"));;
         model.add(new FileProcessingItem("C:\\12", "12"));;
         model.add(new FileProcessingItem("C:\\13", "13"));;
         model.add(new FileProcessingItem("C:\\14", "14"));;
         model.add(new FileProcessingItem("C:\\15", "15"));;

        // State [processed = (), current = (), re-queued = (), backlog = (11, 12, 13, 14, 15)]
        // Assert correct item removed from backlog
         assertFilePathEquals("C:\\12", model.remove(1));
        assertEquals(4, model.getRowCount());
        
        // Process item "11" and "13". Move item "14" to currently processing
        model.getNext();
        model.getNext();
        model.getNext();
        // State [processed = (11, 13), current = (14), re-queued = (), backlog = (15)]
        // Assert correct item removed from processed
        assertFilePathEquals("C:\\13", model.remove(1));
        assertEquals(3, model.getRowCount());
        
        // Re-queue item 11
        model.requeue(0);
        // State [processed = (), current = (14), re-queued = (11), backlog = (15)]
        // Assert correct item removed from re-queued
        assertFilePathEquals("C:\\11", model.remove(1));
        assertEquals(2, model.getRowCount());
        
        // Try to remove the item currently being processed
        // State [processed = (), current = (14), re-queued = (), backlog = (15)]
        IllegalStateException thrownIllegalState = assertThrowsExactly(IllegalStateException.class,
                () ->  model.remove(0), "Did not expect that the item currently being processed could be deleted.");
        assertEquals("Cannot remove the item currently being processed. Index: 0", thrownIllegalState.getMessage());
    }

    private void assertFilePathEquals(String expectedFilePath, FileProcessingItem item) {
        assertEquals(expectedFilePath, item.getOriginalAbsoluteFilePath());
    }
}
