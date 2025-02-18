package com.github.koen_mulder.file_rename_helper.processing;

import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessedListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessedPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelListener;
import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelPublisher;

/**
 * Controller class separating the {@link FileProcessingModel} from the rest of
 * the application. This controller listens to the model events and passes them
 * on to listeners.
 */
public class FileProcessingModelController implements IFileProcessingModelPublisher, IFileProcessingModelListener,
        IFileProcessedPublisher, IFileProcessedListener {

    private final List<IFileProcessingModelListener> modelListeners = Lists.newArrayList();
    private final List<IFileProcessedListener> processedListeners = Lists.newArrayList();

    private FileProcessingModel model;

    public FileProcessingModelController() {
        model = new FileProcessingModel();

        model.addFileProcessingModelListener(this);
        model.addFileProcessedListener(this);
    }

    public FileProcessingItem getValueAt(int rowIndex) {
        return model.getValueAt(rowIndex);
    }

    public List<FileProcessingItem> getAllValues() {
        return model.getAllValues();
    }

    public FileProcessingItem getNext() {
        return model.getNext();
    }

    public boolean add(List<FileProcessingItem> items) {
        return model.add(items);
    }
    
    public boolean requeue(int rowIndex) {
        return model.requeue(0);
    }

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
        FileProcessingModelEvent newEvent = new FileProcessingModelEvent(this, event.getFirstRow(), event.getLastRow(),
                event.getType());
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
    public void onTableChanged(FileProcessingModelEvent event) {
        fireModelChanged(event);
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

    @Override
    public void onFileProcessed(FileProcessingItem fileItem) {
        // Pass on
        notifyFileProcessedListeners(fileItem);
    }
}
