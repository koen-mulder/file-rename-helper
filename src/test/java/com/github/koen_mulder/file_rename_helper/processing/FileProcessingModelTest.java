package com.github.koen_mulder.file_rename_helper.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessedListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelListener;
import com.google.common.collect.Lists;

/**
 * Test class for {@link FileProcessingModel}.
 */
class FileProcessingModelTest {
    
    private FileProcessingModel model;

    @BeforeEach
    void setUp() {
        model = new FileProcessingModel();
    }
    
    // Region getRowCount() tests
    
    @Test
    void getRowCount_emptyModel_shouldReturnZero() {
        assertEquals(0, model.getRowCount());
    }
    
    @Test
    void getRowCount_oneItemAdded_shouldReturnOne() {
        model.add(new FileProcessingItem("item", "item"));
        
        // Item in backlog
        assertEquals(1, model.getRowCount());

        model.getNext(); // Move item to current
        assertEquals(1, model.getRowCount()); // Item in current
        
        model.getNext(); // Move item to processed
        assertEquals(1, model.getRowCount()); // Item in processed
        
        model.requeue(0); // Move item to requeued
        assertEquals(1, model.getRowCount()); // Item in requeued
    }
    
    @Test
    void getRowCount_withItemsInAllPositions_shouldReturnCorrectAmount() {
        model.add(new FileProcessingItem("item 1", "item 1"));
        model.add(new FileProcessingItem("item 2", "item 2"));
        model.add(new FileProcessingItem("item 3", "item 3"));
        model.add(new FileProcessingItem("item 4", "item 4"));
        
        // State [processed = (), current = (), requeued = (), backlog = (1, 2, 3, 4)]
        assertEquals(4, model.getRowCount()); // Items in model
        
        model.getNext(); // Move next item to current
        // State [processed = (), current = (1), requeued = (), backlog = (2, 3, 4)]
        assertEquals(4, model.getRowCount()); // Items in model
        
        model.getNext(); // Move next item to current, move previous item to processed
        // State [processed = (1), current = (2), requeued = (), backlog = (3, 4)]
        assertEquals(4, model.getRowCount()); // Items in model

        model.getNext(); // Move next item to current, move previous item to processed
        // State [processed = (1, 2), current = (3), requeued = (), backlog = (4)]
        assertEquals(4, model.getRowCount()); // Items in model
        
        model.requeue(0); // Move processed item to requeued
        // State [processed = (2), current = (3), requeued = (1), backlog = (4)]
        assertEquals(4, model.getRowCount()); // Items in backlog
    }
    
    // End region

    
    // Region getValueAt(int rowIndex) tests
    
    @Test
    void getValueAt_validIndexInBacklog_shouldReturnCorrectItem() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        assertEquals(item, model.getValueAt(0));
    }
    
    @Test
    void getValueAt_validIndexInCurrent_shouldReturnCorrectItem() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        model.getNext(); // Move item to current
        
        assertEquals(item, model.getValueAt(0));
    }
    
    @Test
    void getValueAt_validIndexInProcessed_shouldReturnCorrectItem() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        model.getNext(); // Move item to current
        model.getNext(); // Move item to processed
        
        assertEquals(item, model.getValueAt(0));
    }
    
    @Test
    void getValueAt_validIndexInRequeued_shouldReturnCorrectItem() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        model.getNext(); // Move item to current
        model.getNext(); // Move item to processed
        model.requeue(0); // Move item to requeued
        
        assertEquals(item, model.getValueAt(0));
    }
    
    @Test
    void getValueAt_validIndex_withItemsInAllPositions_shouldReturnCorrectItem() {
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        model.add(item1);
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        model.add(item2);
        FileProcessingItem item3 = new FileProcessingItem("item 3", "item 3");
        model.add(item3);
        FileProcessingItem item4 = new FileProcessingItem("item 4", "item 4");
        model.add(item4);

        model.getNext(); // Move next item to current
        model.getNext(); // Move next item to current, move previous item to processed
        model.getNext(); // Move next item to current, move previous item to processed
        model.requeue(0); // Move processed item to requeued
        
        // State [processed = (2), current = (3), requeued = (1), backlog = (4)]
        assertEquals(item1, model.getValueAt(2));
        assertEquals(item2, model.getValueAt(0));
        assertEquals(item3, model.getValueAt(1));
        assertEquals(item4, model.getValueAt(3));
    }
    
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 100 }) // Test out-of-bounds indices
    void getValueAt_emptyModel_shouldThrowIndexOutOfBoundsException(int index) {
        IndexOutOfBoundsException thrown = assertThrowsExactly(IndexOutOfBoundsException.class,
                () -> model.getValueAt(index),
                "Expected there would be no item at index " + index + " but there was.");
        assertEquals("Index out of range: " + index, thrown.getMessage());
    }
    
    // End region

    
    // Region add(FileProcessingItem item) tests
    
    @Test
    void add_singleItem() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        assertEquals(item, model.getValueAt(0)); // Item is added
        assertEquals(1, model.getRowCount()); // Only one item in model
    }
    
    @Test
    void add_duplicateItem_whenItemIsInBacklog_shouldReturnFalse() {
        // Create non-duplicate item to test all branches
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        
        // Create duplicate item
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        FileProcessingItem item2duplicate = new FileProcessingItem("item 2", "item 2");
        
        // Add items
        model.add(item1); // Add item 1 to backlog
        model.add(item2); // Add item 2 to backlog
        
        assertFalse(model.add(item2duplicate)); // Add duplicate item
        assertEquals(2, model.getRowCount()); // Only two items in model
    }
    
    @Test
    void add_duplicateItem_whenItemIsInCurrent_shouldReturnFalse() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        FileProcessingItem itemDuplicate = new FileProcessingItem("item", "item");
        
        model.add(item); // Add item to backlog
        model.getNext(); // Move item to current
        
        assertFalse(model.add(itemDuplicate)); // Add duplicate item
        assertEquals(1, model.getRowCount()); // Only one item in model
    }
    
    @Test
    void add_duplicateItem_whenItemIsInProcessed_shouldReturnFalse() {
        // Create non-duplicate item to test all branches
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        
        // Create duplicate item
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        FileProcessingItem item2duplicate = new FileProcessingItem("item 2", "item 2");
        
        // Add items
        model.add(item1); // Add item 1 to backlog
        model.add(item2); // Add item 2 to backlog
        
        // Move items to different states
        model.getNext(); // Move item 1 to current
        model.getNext(); // Move item 2 to current
        model.getNext(); // Move item 2 to processed
        
        assertFalse(model.add(item2duplicate)); // Add duplicate item
        assertEquals(2, model.getRowCount()); // Only two items in model
    }
    
    @Test
    void add_duplicateItem_whenItemIsInRequeued_shouldReturnFalse() {
        // Create non-duplicate item to test all branches
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        
        // Create duplicate item
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        FileProcessingItem item2duplicate = new FileProcessingItem("item 2", "item 2");
        
        // Add items
        model.add(item1); // Add item 1 to backlog
        model.add(item2); // Add item 2 to backlog
        
        // Move items to different states
        model.getNext(); // Move item 1 to current
        model.getNext(); // Move item 2 to current
        model.getNext(); // Move item 2 to processed
        
        // Requeue items - Order is important to test all branches
        model.requeue(0); // Requeue item 1
        model.requeue(0); // Requeue item 2
        
        assertFalse(model.add(item2duplicate)); // Add duplicate item
        assertEquals(2, model.getRowCount()); // Only two items in model
    }
    
    @Test
    void add_nullItem_shouldThrowIllegalArgumentException() {
        // Initialise null item otherwise method is ambiguous
        FileProcessingItem value = null;
        
        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.add(value),
                "Expected that adding a null item would throw a NullPointerException.");
        assertEquals("Cannot add a null item to the model.", thrown.getMessage());
    }
    
    @Test
    void add_invalidItem_withStateProcessed_shouldThrowIllegalArgumentException() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        item.setState(EFileProcessingItemState.PROCESSED);

        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.add(item),
                "Expected that adding an item that is already processed would throw an IllegalArgumentException.");
        assertEquals("Cannot add an item that is not new to the model. Item: " + item,
                thrown.getMessage());
    }
    
    @Test
    void add_invalidItem_withStateCurrent_shouldThrowIllegalArgumentException() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        item.setState(EFileProcessingItemState.PROCESSING);

        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.add(item),
                "Expected that adding an item that is currently being processed would throw an IllegalArgumentException.");
        assertEquals("Cannot add an item that is not new to the model. Item: " + item,
                thrown.getMessage());
    }
    
    @Test
    void add_invalidItem_withStateRequeued_shouldThrowIllegalArgumentException() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        item.setState(EFileProcessingItemState.REQUEUED);

        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.add(item),
                "Expected that adding an item that is requeued would throw an IllegalArgumentException.");
        assertEquals("Cannot add an item that is not new to the model. Item: " + item,
                thrown.getMessage());
    }
    
    @Test
    void add_invalidItem_withStateBacklog_shouldThrowIllegalArgumentException() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        item.setState(EFileProcessingItemState.BACKLOG);

        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.add(item),
                "Expected that adding an item that is in the backlog would throw an IllegalArgumentException.");
        assertEquals("Cannot add an item that is not new to the model. Item: " + item,
                thrown.getMessage());
    }
    
    @Test
    void add_item_shouldFireRowInsertedEvent() {
        // Create mock listener
        IFileProcessingModelListener listener = Mockito.mock(IFileProcessingModelListener.class);
        
        // Add the mock listeners to the model
        model.addFileProcessingModelListener(listener);

        // Capture the event that was fired
        ArgumentCaptor<FileProcessingModelEvent> captor = ArgumentCaptor
                .forClass(FileProcessingModelEvent.class);

        // Add an item to the model
        model.add(new FileProcessingItem("item 1", "item 1"));

        // Verify that the listener was called with the correct event
        Mockito.verify(listener).onTableChanged(captor.capture());
        FileProcessingModelEvent firstEvent = captor.getValue();
        assertEquals(FileProcessingModelEvent.INSERT, firstEvent.getType());
        assertEquals(0, firstEvent.getFirstRow());
        assertEquals(0, firstEvent.getLastRow());
        
        // Add a second item to the model
        model.add(new FileProcessingItem("item 2", "item 2"));
        
        // Verify that the listener was called with the correct event and not with index 0 again
        Mockito.verify(listener, Mockito.times(2)).onTableChanged(captor.capture());
        FileProcessingModelEvent secondEvent = captor.getValue();
        assertEquals(FileProcessingModelEvent.INSERT, secondEvent.getType());
        assertEquals(1, secondEvent.getFirstRow());
        assertEquals(1, secondEvent.getLastRow());
    }

    // End region
    
    // Region add(List<FileProcessingItem> items) tests
    
    @Test
    void addMultiple_validItems_shouldAddToBacklog() {
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        List<FileProcessingItem> items = Lists.newArrayList(item1, item2);

        assertTrue(model.add(items));
        assertEquals(2, model.getRowCount());
    }
    
    @Test
    void addMultiple_withDuplicate_shouldReturnFalse() {
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        FileProcessingItem item2 = new FileProcessingItem("item 1", "item 1"); // Duplicate
        List<FileProcessingItem> items = Lists.newArrayList(item1, item2);

        assertFalse(model.add(items)); // Should return false because of the duplicate
    }
    
    @Test
    void addMultiple_emptyList_shouldNotChangeModel() {
        assertTrue(model.add(Collections.emptyList()));
        assertEquals(0, model.getRowCount());
    }
    
    @Test
    void addMultiple_item_shouldFireRowInsertedEvent() {
        // Create mock listener
        IFileProcessingModelListener listener = Mockito.mock(IFileProcessingModelListener.class);
        
        // Add the mock listeners to the model
        model.addFileProcessingModelListener(listener);

        // Capture the event that was fired
        ArgumentCaptor<FileProcessingModelEvent> captor = ArgumentCaptor
                .forClass(FileProcessingModelEvent.class);

        // Add an item to the model
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        
        List<FileProcessingItem> items = Lists.newArrayList(item1, item2);
        model.add(items);

        // Verify that the listener was called with the correct event
        Mockito.verify(listener, Mockito.times(2)).onTableChanged(captor.capture());

        List<FileProcessingModelEvent> allValues = captor.getAllValues();
        
        FileProcessingModelEvent firstEvent = allValues.get(0);
        assertEquals(FileProcessingModelEvent.INSERT, firstEvent.getType());
        assertEquals(0, firstEvent.getFirstRow());
        assertEquals(0, firstEvent.getLastRow());
        
        FileProcessingModelEvent secondEvent = allValues.get(1);
        assertEquals(FileProcessingModelEvent.INSERT, secondEvent.getType());
        assertEquals(1, secondEvent.getFirstRow());
        assertEquals(1, secondEvent.getLastRow());
    }

    // End region
    
    // Region requeue(int rowIndex) tests
    
    @Test
    void requeue_validIndexInProcessed_shouldMoveItemToRequeued() {
        // Add item
        model.add(new FileProcessingItem("item", "item"));
        
        // Process item
        model.getNext(); // Move item to current
        model.getNext(); // Move item to processed
        
        assertTrue(model.requeue(0));
        assertEquals(1, model.getRowCount());
    }
    
    @Test
    void requeue_itemNotProcessedFromBacklog_shouldThrowIllegalArgumentException() {
        // Add item
        model.add(new FileProcessingItem("item", "item"));

        // Requeue item that has not been processed
        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.requeue(0),
                "Expected that requeuing an item that has not been processed would throw an"
                        + " IllegalStateException.");
        assertEquals("Cannot re-queue an item that has not been processed yet. Index: 0",
                thrown.getMessage());
    }
    
    @Test
    void requeue_itemNotProcessedFromCurrent_shouldThrowIllegalArgumentException() {
        // Add item
        model.add(new FileProcessingItem("item", "item"));
        
        // Process item
        model.getNext(); // Move item to current
        
        // Requeue item that has not been processed
        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.requeue(0),
                "Expected that requeuing an item that has not been processed would throw an"
                        + " IllegalStateException.");
        assertEquals("Cannot re-queue an item that has not been processed yet. Index: 0",
                thrown.getMessage());
    }
    
    @Test
    void requeue_itemAlreadyRequeued_shouldThrowIllegalArgumentException() {
        // Add item
        model.add(new FileProcessingItem("item", "item"));
        
        // Process item
        model.getNext(); // Move item to current
        model.getNext(); // Move item to processed
        model.requeue(0); // Move item to requeued
        
        // Requeue item that has not been processed
        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.requeue(0),
                "Expected that requeuing an item that has not been processed would throw an"
                        + " IllegalStateException.");
        assertEquals("Cannot re-queue an item that has not been processed yet. Index: 0",
                thrown.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 100 }) // Test out-of-bounds indices
    void requeue_invalidIndex_shouldThrowIndexOutOfBoundsException(int index) {
        // Requeue item with invalid index
        IndexOutOfBoundsException thrown = assertThrowsExactly(IndexOutOfBoundsException.class,
                () -> model.requeue(index),
                "Expected that requeuing an item with an invalid index would throw an"
                        + " IndexOutOfBoundsException.");
        assertEquals("Index out of range: " + index, thrown.getMessage());
    }
    
    @Test
    void requeue_validIndex_shouldFireCorrectModelEvents() {
        // Add an item to the model
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        // Process item
        model.getNext(); // Move item to current
        model.getNext(); // Move item to processed
        
        // Create mock listener
        IFileProcessingModelListener listener = Mockito.mock(IFileProcessingModelListener.class);
        
        // Add the mock listeners to the model
        model.addFileProcessingModelListener(listener);

        // Capture the event that was fired
        ArgumentCaptor<FileProcessingModelEvent> captor = ArgumentCaptor
                .forClass(FileProcessingModelEvent.class);
        
        // Requeue item
        model.requeue(0);

        // Verify that the listener was called with the correct event
        Mockito.verify(listener, Mockito.times(2)).onTableChanged(captor.capture());

        List<FileProcessingModelEvent> allValues = captor.getAllValues();
        
        FileProcessingModelEvent firstEvent = allValues.get(0);
        assertEquals(FileProcessingModelEvent.DELETE, firstEvent.getType());
        assertEquals(0, firstEvent.getFirstRow());
        assertEquals(0, firstEvent.getLastRow());
        
        FileProcessingModelEvent secondEvent = allValues.get(1);
        assertEquals(FileProcessingModelEvent.INSERT, secondEvent.getType());
        assertEquals(0, secondEvent.getFirstRow());
        assertEquals(0, secondEvent.getLastRow());
    }

    // End region
    
    // Region requeue(FileProcessingItem item) tests
    
    @Test
    void requeue_validItemInProcessed_shouldMoveItemToRequeued() {
        // Add item
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        // Process item
        model.getNext(); // Move item to current
        model.getNext(); // Move item to processed
        
        assertTrue(model.requeue(item));
        assertEquals(1, model.getRowCount());
    }
    
    @Test
    void requeue_invalidItemNotInModel_shouldThrowIllegalArgumentException() {
        // Create items
        FileProcessingItem item = new FileProcessingItem("item", "item");
        FileProcessingItem itemNotInModel = new FileProcessingItem("not in model", "not in model");

        // Add item
        model.add(item);
        
        // Process item
        model.getNext(); // Move item to current
        model.getNext(); // Move item to processed
        
        // Requeue item that is not in the model
        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.requeue(itemNotInModel),
                "Expected that requeuing an item that is not in the model would throw an"
                        + " IllegalStateException.");
        assertEquals("Trying to requeue an item that does not exist: " + itemNotInModel,
                thrown.getMessage());
    }
    
    // Add test for requeuing null item
    @Test
    void requeue_nullItem_shouldThrowIllegalArgumentException() {
        // Initialise null item otherwise method is ambiguous
        FileProcessingItem value = null;
        
        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.requeue(value),
                "Expected that requeuing a null item would throw a NullPointerException.");
        assertEquals("Cannot requeue a null item.", thrown.getMessage());
    }
    
    // End region
    
    // Region getNext() tests
    
    @Test
    void getNext_emptyModel_shouldReturnNull() {
        assertEquals(null, model.getNext());
    }
    
    @Test
    void getNext_oneItemAdded_shouldReturnItemWithCorrectState() {
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        FileProcessingItem nextItem = model.getNext();
        assertEquals(item, nextItem);
        assertEquals(EFileProcessingItemState.PROCESSING, nextItem.getState());
    }
    
    @Test
    void getNext_withRequeuedAndBacklogItems_shouldReturnCorrectItem() {
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        model.add(item1);
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        model.add(item2);
        FileProcessingItem item3 = new FileProcessingItem("item 3", "item 3");
        model.add(item3);

        // State [processed = (), current = (), requeued = (), backlog = (1, 2, 3)]
        assertEquals(item1, model.getNext());
        assertEquals(item2, model.getNext());

        
        // State [processed = (1), current = (2), requeued = (), backlog = (3)]
        model.requeue(0); // Move processed item to requeued
        
        // State [processed = (), current = (2), requeued = (1), backlog = (3)] 
        assertEquals(item1, model.getNext());
        
        // State [processed = (2), current = (1), requeued = (), backlog = (3)]
        assertEquals(item3, model.getNext());
    }
    
    @Test
    void getNext_withOneItemAdded_shouldFireCorrectModelEvent() {
        // Add an item to the model
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        // Create mock listener
        IFileProcessingModelListener listener = Mockito.mock(IFileProcessingModelListener.class);
        
        // Add the mock listeners to the model
        model.addFileProcessingModelListener(listener);

        // Capture the event that was fired
        ArgumentCaptor<FileProcessingModelEvent> captor = ArgumentCaptor
                .forClass(FileProcessingModelEvent.class);
        
        // Get the next item
        model.getNext();

        // Verify that the listener was called with the correct event
        Mockito.verify(listener).onTableChanged(captor.capture());
        FileProcessingModelEvent event = captor.getValue();
        assertEquals(FileProcessingModelEvent.UPDATE, event.getType());
        assertEquals(0, event.getFirstRow());
        assertEquals(0, event.getLastRow());
    }
    
    @Test
    void getNext_calledTwice_shouldFireCorrectModelEvents() {
        // Add an item to the model
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        model.add(item1);
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        model.add(item2);
        
        // Create mock listener
        IFileProcessingModelListener listener = Mockito.mock(IFileProcessingModelListener.class);
        
        // Add the mock listeners to the model
        model.addFileProcessingModelListener(listener);

        // Capture the event that was fired
        ArgumentCaptor<FileProcessingModelEvent> captor = ArgumentCaptor
                .forClass(FileProcessingModelEvent.class);
        
        // Get the next item
        model.getNext(); // Move item to current
        model.getNext(); // Move item to processed

        // Verify that the listener was called with the correct event
        Mockito.verify(listener, Mockito.times(3)).onTableChanged(captor.capture());
        
        List<FileProcessingModelEvent> allValues = captor.getAllValues();
        
        // Item 1 moved to current
        FileProcessingModelEvent firstEvent = allValues.get(0);
        assertEquals(FileProcessingModelEvent.UPDATE, firstEvent.getType());
        assertEquals(0, firstEvent.getFirstRow());
        assertEquals(0, firstEvent.getLastRow());
        
        // Item 1 moved to processed
        FileProcessingModelEvent secondEvent = allValues.get(1);
        assertEquals(FileProcessingModelEvent.UPDATE, secondEvent.getType());
        assertEquals(0, secondEvent.getFirstRow());
        assertEquals(0, secondEvent.getLastRow());
        
        // Item 2 moved to current
        FileProcessingModelEvent thirdEvent = allValues.get(2);
        assertEquals(FileProcessingModelEvent.UPDATE, thirdEvent.getType());
        assertEquals(1, thirdEvent.getFirstRow());
        assertEquals(1, thirdEvent.getLastRow());
    }
    
    @Test
    void getNext_withItemInCurrentAndRequeue_shouldFireCorrectModelEvents() {
        // Add items to the model
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        model.add(item1);
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        model.add(item2);
        
        
        // Process items
        model.getNext(); // Move item 1 to current
        model.getNext(); // Move item 1 to processed
        model.requeue(0); // Move item 1 to requeued
        
        // Create mock listener
        IFileProcessingModelListener listener = Mockito.mock(IFileProcessingModelListener.class);
        
        // Add the mock listeners to the model
        model.addFileProcessingModelListener(listener);

        // Capture the event that was fired
        ArgumentCaptor<FileProcessingModelEvent> captor = ArgumentCaptor
                .forClass(FileProcessingModelEvent.class);
        
        // Get the next item
        model.getNext();

        // Verify that the listener was called with the correct event
        Mockito.verify(listener, Mockito.times(2)).onTableChanged(captor.capture());
        
        List<FileProcessingModelEvent> allValues = captor.getAllValues();
        
        // Item 2 moved from current to processed
        FileProcessingModelEvent firstEvent = allValues.get(0);
        assertEquals(FileProcessingModelEvent.UPDATE, firstEvent.getType());
        assertEquals(0, firstEvent.getFirstRow());
        assertEquals(0, firstEvent.getLastRow());
        
        // Item 1 moved from requeued to current
        FileProcessingModelEvent secondEvent = allValues.get(1);
        assertEquals(FileProcessingModelEvent.UPDATE, secondEvent.getType());
        assertEquals(1, secondEvent.getFirstRow());
        assertEquals(1, secondEvent.getLastRow());
    }
    
    @Test
    void getNext_withItemInCurrent_shouldFireFileProcessedEvent() {
        // Add an item to the model
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        // Create mock listener
        IFileProcessedListener listener = Mockito.mock(IFileProcessedListener.class);
        
        // Add the mock listeners to the model
        model.addFileProcessedListener(listener);
        
        // Get the next item
        model.getNext();
        model.getNext();
        
        // Verify that the listener was called with the correct event
        Mockito.verify(listener).onFileProcessed(item);
    }
    
    // End region
    
    // Region remove(int rowIndex) tests
    
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 100 }) // Test out-of-bounds indices
    void remove_emptyModel_shouldThrowIndexOutOfBoundsException(int index) {
        IndexOutOfBoundsException thrown = assertThrowsExactly(IndexOutOfBoundsException.class,
                () -> model.remove(index),
                "Expected that removing an item from an empty model would throw an IndexOutOfBoundsException.");
        assertEquals("Index out of range: " + index, thrown.getMessage());
    }
    
    @Test
    void remove_validIndexInBacklog_shouldRemoveItem() {
        // Add item to be processed while removing anther item from backlog to test all branches
        model.add(new FileProcessingItem("current item", "current item"));
        model.getNext();
        
        // Add item to backlog to be removed
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        assertEquals(item, model.remove(1));
        assertEquals(1, model.getRowCount());
    }
    
    @Test
    void remove_currentItem_shouldThrowIllegalArgumentException() {
        // Add item to the model
        model.add(new FileProcessingItem("item", "item"));
        
        // Move item to current
        model.getNext();
        
        // Try to remove the item currently being processed
        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.remove(0),
                "Expected that removing the item currently being processed would throw an IllegalArgumentException.");
        assertEquals("Cannot remove the item currently being processed. Index: 0", thrown.getMessage());
    }
    
    @Test
    void remove_processedItem_shouldRemoveItem() {
        // Add item to the model
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        model.getNext(); // Move item to current
        model.getNext(); // Move item to processed
        
        
        assertEquals(1, model.getRowCount());
        assertEquals(item, model.remove(0));
        assertEquals(0, model.getRowCount());
    }
    
    @Test
    void remove_requeuedItem_shouldRemoveItem() {
        // Add item to the model
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        model.getNext(); // Move item to current
        model.getNext(); // Move item to processed
        model.requeue(0); // Move item to requeued
        
        assertEquals(1, model.getRowCount());
        assertEquals(item, model.remove(0));
        assertEquals(0, model.getRowCount());
    }
    
    @Test
    void remove_validIndex_shouldFireCorrectModelEvent() {
        // Add an item to the model
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        // Create mock listener
        IFileProcessingModelListener listener = Mockito.mock(IFileProcessingModelListener.class);
        
        // Add the mock listeners to the model
        model.addFileProcessingModelListener(listener);

        // Capture the event that was fired
        ArgumentCaptor<FileProcessingModelEvent> captor = ArgumentCaptor
                .forClass(FileProcessingModelEvent.class);
        
        // Remove item
        model.remove(0);

        // Verify that the listener was called with the correct event
        Mockito.verify(listener).onTableChanged(captor.capture());
        FileProcessingModelEvent event = captor.getValue();
        assertEquals(FileProcessingModelEvent.DELETE, event.getType());
        assertEquals(0, event.getFirstRow());
        assertEquals(0, event.getLastRow());
    }
    
    // End region
    
    // Region getAllValues() tests
    
    @Test
    void getAllValues_emptyModel_shouldReturnEmptyList() {
        assertEquals(Collections.emptyList(), model.getAllValues());
    }
    
    @Test
    void getAllValues_withItemsInAllPositions_shouldReturnCorrectItems() {
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        model.add(item1);
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        model.add(item2);
        FileProcessingItem item3 = new FileProcessingItem("item 3", "item 3");
        model.add(item3);
        FileProcessingItem item4 = new FileProcessingItem("item 4", "item 4");
        model.add(item4);

        // State [processed = (), current = (), requeued = (), backlog = (1, 2, 3, 4)]
        assertEquals(Lists.newArrayList(item1, item2, item3, item4), model.getAllValues());
        
        model.getNext(); // Move next item to current
        // State [processed = (), current = (1), requeued = (), backlog = (2, 3, 4)]
        assertEquals(Lists.newArrayList(item1, item2, item3, item4), model.getAllValues());
        
        model.getNext(); // Move next item to current, move previous item to processed
        // State [processed = (1), current = (2), requeued = (), backlog = (3, 4)]
        assertEquals(Lists.newArrayList(item1, item2, item3, item4), model.getAllValues());

        model.getNext(); // Move next item to current, move previous item to processed
        // State [processed = (1, 2), current = (3), requeued = (), backlog = (4)]
        assertEquals(Lists.newArrayList(item1, item2, item3, item4), model.getAllValues());
        
        model.requeue(0); // Move processed item to requeued
        // State [processed = (2), current = (3), requeued = (1), backlog = (4)]
        assertEquals(Lists.newArrayList(item2, item3, item1, item4), model.getAllValues());
    }
    
    // End region
    
    // Region getIndexOf(FileProcessingItem item) tests
    
    @Test
    void getIndexOf_emptyModel_shouldReturnMinusOne() {
        assertEquals(-1, model.getIndexOf(new FileProcessingItem("item", "item")));
    }
    
    @Test
    void getIndexOf_nullItem_shouldThrowIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrowsExactly(IllegalArgumentException.class,
                () -> model.getIndexOf(null),
                "Expected that getting the index of a null item would throw a IllegalArgumentException.");
        assertEquals("Cannot get the index of a null item.", thrown.getMessage());
    }
    
    @Test
    void getIndexOf_itemNotInModel_shouldReturnMinusOne() {
        // Add item to the model
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        assertEquals(-1, model.getIndexOf(new FileProcessingItem("not in model", "not in model")));
    }
    
    @Test
    void getIndexOf_withItemInEachState_shouldReturnCorrectIndex() {
        // Add items to the model
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        model.add(item1);
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        model.add(item2);
        FileProcessingItem item3 = new FileProcessingItem("item 3", "item 3");
        model.add(item3);
        FileProcessingItem item4 = new FileProcessingItem("item 4", "item 4");
        model.add(item4);
        FileProcessingItem item5 = new FileProcessingItem("item 5", "item 5");
        model.add(item5);
        
        // State [processed = (), current = (), requeued = (), backlog = (1, 2, 3, 4, 5)]
        assertEquals(0, model.getIndexOf(item1));
        assertEquals(1, model.getIndexOf(item2));
        assertEquals(2, model.getIndexOf(item3));
        assertEquals(3, model.getIndexOf(item4));
        assertEquals(4, model.getIndexOf(item5));
        
        // Move items to different states
        model.getNext(); // Move item 1 to current
        model.getNext(); // Move item 1 to processed
        model.getNext(); // Move item 2 to processed
        model.getNext(); // Move item 3 to processed
        
        // Requeue items
        model.requeue(2); // Move item 3 to requeued
        model.requeue(0); // Move item 1 to requeued
        
        // State [processed = (2), current = (4), requeued = (3, 1), backlog = (5)]
        assertEquals(0, model.getIndexOf(item2));
        assertEquals(1, model.getIndexOf(item4));
        assertEquals(2, model.getIndexOf(item3));
        assertEquals(3, model.getIndexOf(item1));
        assertEquals(4, model.getIndexOf(item5));
    }
    
    @Test
    void getIndexOf_requeuedItem_withNoCurrent_shouldReturnCorrectIndex() {
        // Add items to the model
        FileProcessingItem item1 = new FileProcessingItem("item 1", "item 1");
        model.add(item1);
        FileProcessingItem item2 = new FileProcessingItem("item 2", "item 2");
        model.add(item2);
        FileProcessingItem item3 = new FileProcessingItem("item 3", "item 3");
        model.add(item3);
        
        // State [processed = (), current = (), requeued = (), backlog = (1, 2, 3)]
        assertEquals(0, model.getIndexOf(item1));
        assertEquals(1, model.getIndexOf(item2));
        assertEquals(2, model.getIndexOf(item3));
        
        // Move items to different states
        model.getNext(); // Move item 1 to current
        model.getNext(); // Move item 1 to processed
        model.getNext(); // Move item 2 to processed
        model.getNext(); // Move item 3 to processed
        
        // Requeue items
        model.requeue(1); // Move item 2 to requeued
        
        // State [processed = (1, 3), current = (), requeued = (2), backlog = ()]
        assertEquals(0, model.getIndexOf(item1));
        assertEquals(1, model.getIndexOf(item3));
        assertEquals(2, model.getIndexOf(item2));
    }
    
    // End region
    
    // Region Listener Tests (Minimal - only check that they are called)

    // We already test that listeners get called with the proper events throughout the tests.
    
    @Test
    void removeFileProcessingModelListener_shouldRemoveListener() {
        // Create mock listener
        IFileProcessingModelListener listener = Mockito.mock(IFileProcessingModelListener.class);
        
        // Add the mock listener to the model
        model.addFileProcessingModelListener(listener);
        
        // Remove the listener
        model.removeFileProcessingModelListener(listener);
        
        // Add an item to the model
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        // Verify that the listener was not called
        Mockito.verify(listener, Mockito.never()).onTableChanged(Mockito.any());
    }
    
    @Test
    void removeFileProcessedListener_shouldRemoveListener() {
        // Create mock listener
        IFileProcessedListener listener = Mockito.mock(IFileProcessedListener.class);
        
        // Add the mock listener to the model
        model.addFileProcessedListener(listener);
        
        // Remove the listener
        model.removeFileProcessedListener(listener);
        
        // Add an item to the model
        FileProcessingItem item = new FileProcessingItem("item", "item");
        model.add(item);
        
        // Verify that the listener was not called
        Mockito.verify(listener, Mockito.never()).onFileProcessed(Mockito.any());
    }
}
