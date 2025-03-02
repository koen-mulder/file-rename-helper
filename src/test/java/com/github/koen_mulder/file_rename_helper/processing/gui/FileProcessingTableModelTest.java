package com.github.koen_mulder.file_rename_helper.processing.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelListener;
import com.github.koen_mulder.file_rename_helper.processing.EFileProcessingItemState;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelController;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelEvent;

public class FileProcessingTableModelTest {

    @Mock
    private FileProcessingModelController mockController;

    @BeforeEach
    public void setUp() throws InterruptedException, InvocationTargetException {
        MockitoAnnotations.openMocks(this);
    }

    // Region constructor tests

    @Test
    void constructor_EmptyInitialDataLoad() {
        // Set controller to return an empty list
        when(mockController.getAllValues()).thenReturn(List.of());

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Assert
        assertEquals(0, tableModel.getRowCount());

        // Verify listener is added
        verify(mockController).addFileProcessingModelListener(any());
    }

    @Test
    void constructor_InitialDataLoad() throws Exception {
        // Initialise a list of files
        List<FileProcessingItem> initialItems = Arrays.asList(
                new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf"),
                new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf"));
        // Set controller to return the initial items
        when(mockController.getAllValues()).thenReturn(initialItems);

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Assert
        assertEquals(2, tableModel.getRowCount());
        assertEquals(initialItems.get(0), tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));

        // Verify listener is added
        verify(mockController).addFileProcessingModelListener(any());
    }

    // End region constructor tests

    // Region onTableChanged - insert - tests

    @Test
    void onTableChanged_insert() throws InvocationTargetException, InterruptedException {
        // Set controller#getAllValues() to return an empty list
        when(mockController.getAllValues()).thenReturn(List.of());

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Insert a new item
        FileProcessingItem newItem = new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf");
        when(mockController.getValueAt(0)).thenReturn(newItem);

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 0, 0, FileProcessingModelEvent.INSERT));
        });

        // Assert
        assertEquals(1, tableModel.getRowCount());
        assertEquals(newItem, tableModel.getValueAt(0, 0));
    }

    @Test
    void onTableChanged_insertMultipleOneByOne()
            throws InvocationTargetException, InterruptedException {
        // Set controller#getAllValues() to return an empty list
        when(mockController.getAllValues()).thenReturn(List.of());

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Insert a new item
        FileProcessingItem newItem1 = new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf");
        when(mockController.getValueAt(0)).thenReturn(newItem1);

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 0, 0, FileProcessingModelEvent.INSERT));
        });

        // Assert
        assertEquals(1, tableModel.getRowCount());
        assertEquals(newItem1, tableModel.getValueAt(0, 0));

        // Insert next item
        FileProcessingItem newItem2 = new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf");
        when(mockController.getValueAt(1)).thenReturn(newItem2);

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 1, 1, FileProcessingModelEvent.INSERT));
        });

        // Assert
        assertEquals(2, tableModel.getRowCount());
        assertEquals(newItem1, tableModel.getValueAt(0, 0));
        assertEquals(newItem2, tableModel.getValueAt(1, 0));
    }

    @Test
    void onTableChanged_insertMultipleAtOnce()
            throws InvocationTargetException, InterruptedException {
        // Set controller#getAllValues() to return an empty list
        when(mockController.getAllValues()).thenReturn(List.of());

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Insert a new item
        FileProcessingItem newItem1 = new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf");
        FileProcessingItem newItem2 = new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf");
        when(mockController.getValueAt(0)).thenReturn(newItem1);
        when(mockController.getValueAt(1)).thenReturn(newItem2);

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 0, 1, FileProcessingModelEvent.INSERT));
        });

        // Assert
        assertEquals(2, tableModel.getRowCount());
        assertEquals(newItem1, tableModel.getValueAt(0, 0));
        assertEquals(newItem2, tableModel.getValueAt(1, 0));
    }

    // End region onTableChanged - insert - tests

    // Region onTableChanged - delete - tests

    @Test
    void onTableChanged_delete() throws InvocationTargetException, InterruptedException {
        // Initialise a list of files
        List<FileProcessingItem> initialItems = Arrays.asList(
                new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf"),
                new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf"));

        // Set controller to return the initial items
        when(mockController.getAllValues()).thenReturn(initialItems);

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Assert initial state
        assertEquals(2, tableModel.getRowCount());
        assertEquals(initialItems.get(0), tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 0, 0, FileProcessingModelEvent.DELETE));
        });

        // Assert after delete
        assertEquals(1, tableModel.getRowCount());
        assertEquals(initialItems.get(1), tableModel.getValueAt(0, 0));
    }

    @Test
    void onTableChanged_deleteMultipleOneByOne()
            throws InvocationTargetException, InterruptedException {
        // Initialise a list of files
        List<FileProcessingItem> initialItems = Arrays.asList(
                new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf"),
                new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf"),
                new FileProcessingItem("filepath/file 3.pdf", "file 3.pdf"));

        // Set controller to return the initial items
        when(mockController.getAllValues()).thenReturn(initialItems);

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Assert initial state
        assertEquals(3, tableModel.getRowCount());
        assertEquals(initialItems.get(0), tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));
        assertEquals(initialItems.get(2), tableModel.getValueAt(2, 0));

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 0, 0, FileProcessingModelEvent.DELETE));
        });

        // Assert after delete
        assertEquals(2, tableModel.getRowCount());
        assertEquals(initialItems.get(1), tableModel.getValueAt(0, 0));

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 0, 0, FileProcessingModelEvent.DELETE));
        });

        // Assert after delete
        assertEquals(1, tableModel.getRowCount());
        assertEquals(initialItems.get(2), tableModel.getValueAt(0, 0));
    }

    @Test
    void onTableChanged_deleteMultipleAtOnce()
            throws InvocationTargetException, InterruptedException {
        // Initialise a list of files
        List<FileProcessingItem> initialItems = Arrays.asList(
                new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf"),
                new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf"),
                new FileProcessingItem("filepath/file 3.pdf", "file 3.pdf"));

        // Set controller to return the initial items
        when(mockController.getAllValues()).thenReturn(initialItems);

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Assert initial state
        assertEquals(3, tableModel.getRowCount());
        assertEquals(initialItems.get(0), tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));
        assertEquals(initialItems.get(2), tableModel.getValueAt(2, 0));

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 0, 1, FileProcessingModelEvent.DELETE));
        });

        // Assert after delete
        assertEquals(1, tableModel.getRowCount());
        assertEquals(initialItems.get(2), tableModel.getValueAt(0, 0));
    }

    @Test
    void onTableChanged_deleteMultiple_whenLastrowIsMaxValue()
            throws InvocationTargetException, InterruptedException {
        // Initialise a list of files
        List<FileProcessingItem> initialItems = Arrays.asList(
                new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf"),
                new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf"),
                new FileProcessingItem("filepath/file 3.pdf", "file 3.pdf"));

        // Set controller to return the initial items
        when(mockController.getAllValues()).thenReturn(initialItems);

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Assert initial state
        assertEquals(3, tableModel.getRowCount());
        assertEquals(initialItems.get(0), tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));
        assertEquals(initialItems.get(2), tableModel.getValueAt(2, 0));

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(new FileProcessingModelEvent(this, 0,
                    Integer.MAX_VALUE, FileProcessingModelEvent.DELETE));
        });

        // Assert after delete
        assertEquals(0, tableModel.getRowCount());
    }

    // End region onTableChanged - delete - tests

    // Region onTableChanged - update - tests

    @Test
    void onTableChanged_update() throws InvocationTargetException, InterruptedException {
        // Initialise a list of files
        List<FileProcessingItem> initialItems = Arrays.asList(
                new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf"),
                new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf"));

        // Set controller to return the initial items
        when(mockController.getAllValues()).thenReturn(initialItems);

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Assert initial state
        assertEquals(2, tableModel.getRowCount());
        assertEquals(initialItems.get(0), tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));

        // Update the first item
        FileProcessingItem updatedItem = new FileProcessingItem("filepath/file UPDATED 1.pdf",
                "file UPDATED 1.pdf");
        updatedItem.setState(EFileProcessingItemState.PROCESSING);
        when(mockController.getValueAt(0)).thenReturn(updatedItem);

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 0, 0, FileProcessingModelEvent.UPDATE));
        });

        // Validate controller calls
        verify(mockController, times(1)).getValueAt(0);
        verify(mockController, times(0)).getValueAt(1);

        // Validate the table model
        assertEquals(2, tableModel.getRowCount());
        assertEquals(updatedItem, tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));
    }

    @Test
    void onTableChanged_updateMultipleOneByOne()
            throws InvocationTargetException, InterruptedException {
        // Initialise a list of files
        List<FileProcessingItem> initialItems = Arrays.asList(
                new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf"),
                new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf"),
                new FileProcessingItem("filepath/file 3.pdf", "file 3.pdf"));

        // Set controller to return the initial items
        when(mockController.getAllValues()).thenReturn(initialItems);

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Assert initial state
        assertEquals(3, tableModel.getRowCount());
        assertEquals(initialItems.get(0), tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));
        assertEquals(initialItems.get(2), tableModel.getValueAt(2, 0));

        // Update the first item
        FileProcessingItem updatedItem1 = new FileProcessingItem("filepath/file UPDATED 1.pdf",
                "file UPDATED 1.pdf");
        updatedItem1.setState(EFileProcessingItemState.PROCESSING);
        when(mockController.getValueAt(0)).thenReturn(updatedItem1);

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 0, 0, FileProcessingModelEvent.UPDATE));
        });

        // Validate controller calls
        verify(mockController, times(1)).getValueAt(0);
        verify(mockController, times(0)).getValueAt(1);
        verify(mockController, times(0)).getValueAt(2);

        // Validate the table model
        assertEquals(3, tableModel.getRowCount());
        assertEquals(updatedItem1, tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));
        assertEquals(initialItems.get(2), tableModel.getValueAt(2, 0));

        // Update the second item
        FileProcessingItem updatedItem2 = new FileProcessingItem("filepath/file UPDATED 2.pdf",
                "file UPDATED 2.pdf");
        updatedItem2.setState(EFileProcessingItemState.PROCESSING);
        when(mockController.getValueAt(1)).thenReturn(updatedItem2);

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 1, 1, FileProcessingModelEvent.UPDATE));
        });

        // Validate controller calls
        verify(mockController, times(1)).getValueAt(0);
        verify(mockController, times(1)).getValueAt(1);
        verify(mockController, times(0)).getValueAt(2);

        // Validate the table model
        assertEquals(3, tableModel.getRowCount());
        assertEquals(updatedItem1, tableModel.getValueAt(0, 0));
        assertEquals(updatedItem2, tableModel.getValueAt(1, 0));
        assertEquals(initialItems.get(2), tableModel.getValueAt(2, 0));
    }

    @Test
    void onTableChanged_updateMultipleAtOnce()
            throws InvocationTargetException, InterruptedException {
        // Initialise a list of files
        List<FileProcessingItem> initialItems = Arrays.asList(
                new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf"),
                new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf"),
                new FileProcessingItem("filepath/file 3.pdf", "file 3.pdf"));

        // Set controller to return the initial items
        when(mockController.getAllValues()).thenReturn(initialItems);

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Assert initial state
        assertEquals(3, tableModel.getRowCount());
        assertEquals(initialItems.get(0), tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));
        assertEquals(initialItems.get(2), tableModel.getValueAt(2, 0));

        // Update the first two items
        FileProcessingItem updatedItem1 = new FileProcessingItem("filepath/file UPDATED 1.pdf",
                "file UPDATED 1.pdf");
        updatedItem1.setState(EFileProcessingItemState.PROCESSING);
        FileProcessingItem updatedItem2 = new FileProcessingItem("filepath/file UPDATED 2.pdf",
                "file UPDATED 2.pdf");
        updatedItem2.setState(EFileProcessingItemState.PROCESSING);
        when(mockController.getValueAt(0)).thenReturn(updatedItem1);
        when(mockController.getValueAt(1)).thenReturn(updatedItem2);

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(
                    new FileProcessingModelEvent(this, 0, 1, FileProcessingModelEvent.UPDATE));
        });

        // Validate controller calls
        verify(mockController, times(1)).getValueAt(0);
        verify(mockController, times(1)).getValueAt(1);
        verify(mockController, times(0)).getValueAt(2);

        // Validate the table model
        assertEquals(3, tableModel.getRowCount());
        assertEquals(updatedItem1, tableModel.getValueAt(0, 0));
        assertEquals(updatedItem2, tableModel.getValueAt(1, 0));
        assertEquals(initialItems.get(2), tableModel.getValueAt(2, 0));
    }

    @Test
    void onTableChanged_updateMultiple_whenLastrowIsMaxValue()
            throws InvocationTargetException, InterruptedException {
        // Initialise a list of files
        List<FileProcessingItem> initialItems = Arrays.asList(
                new FileProcessingItem("filepath/file 1.pdf", "file 1.pdf"),
                new FileProcessingItem("filepath/file 2.pdf", "file 2.pdf"),
                new FileProcessingItem("filepath/file 3.pdf", "file 3.pdf"));

        // Set controller to return the initial items
        when(mockController.getAllValues()).thenReturn(initialItems);

        // Create the table model
        FileProcessingTableModel tableModel = new FileProcessingTableModel(mockController);

        // Capture the listener that is added to the controller
        ArgumentCaptor<IFileProcessingModelListener> listenerCaptor = ArgumentCaptor
                .forClass(IFileProcessingModelListener.class);
        verify(mockController).addFileProcessingModelListener(listenerCaptor.capture());

        // Assert initial state
        assertEquals(3, tableModel.getRowCount());
        assertEquals(initialItems.get(0), tableModel.getValueAt(0, 0));
        assertEquals(initialItems.get(1), tableModel.getValueAt(1, 0));
        assertEquals(initialItems.get(2), tableModel.getValueAt(2, 0));

        // Update the first two items
        FileProcessingItem updatedItem1 = new FileProcessingItem("filepath/file UPDATED 1.pdf",
                "file UPDATED 1.pdf");
        updatedItem1.setState(EFileProcessingItemState.PROCESSING);
        FileProcessingItem updatedItem2 = new FileProcessingItem("filepath/file UPDATED 2.pdf",
                "file UPDATED 2.pdf");
        updatedItem2.setState(EFileProcessingItemState.PROCESSING);
        when(mockController.getValueAt(0)).thenReturn(updatedItem1);
        when(mockController.getValueAt(1)).thenReturn(updatedItem2);
        when(mockController.getValueAt(2)).thenReturn(initialItems.get(2));

        // Fire the event - wait for the event to be processed
        SwingUtilities.invokeAndWait(() -> {
            listenerCaptor.getValue().onTableChanged(new FileProcessingModelEvent(this, 0,
                    Integer.MAX_VALUE, FileProcessingModelEvent.UPDATE));
        });

        // Validate controller calls
        verify(mockController, times(1)).getValueAt(0);
        verify(mockController, times(1)).getValueAt(1);
        verify(mockController, times(1)).getValueAt(2);

        // Validate the table model
        assertEquals(3, tableModel.getRowCount());
        assertEquals(updatedItem1, tableModel.getValueAt(0, 0));
        assertEquals(updatedItem2, tableModel.getValueAt(1, 0));
        assertEquals(initialItems.get(2), tableModel.getValueAt(2, 0));
    }
    
    // End region onTableChanged - update - tests
}