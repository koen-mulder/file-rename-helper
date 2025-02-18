package com.github.koen_mulder.file_rename_helper.processing.gui;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.interfaces.IFileProcessingModelListener;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelController;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelEvent;

public class FileProcessingTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -3670356454612137435L;

    private final List<FileProcessingItem> items = Lists.newArrayList();
    
    private final FileProcessingModelController controller;
    
    public FileProcessingTableModel(FileProcessingModelController controller) {
        this.controller = controller;
        controller.addFileProcessingModelListener(new IFileProcessingModelListener() {
            
            @Override
            public void onTableChanged(FileProcessingModelEvent e) {
                int firstRow = e.getFirstRow();
                int lastRow = e.getLastRow();
                
                if (lastRow == Integer.MAX_VALUE) {
                    lastRow = items.size();
                }
                
                if (e.getType() == FileProcessingModelEvent.DELETE) {
                    remove(firstRow, lastRow);
                } else if (e.getType() == FileProcessingModelEvent.INSERT) {
                    insert(firstRow, lastRow);
                } else if (e.getType() == FileProcessingModelEvent.UPDATE) {
                    update(firstRow, lastRow);
                }
            }
        });
        
        items.addAll(controller.getAllValues());
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "File";
    }

    @Override
    public FileProcessingItem getValueAt(int rowIndex, int columnIndex) {
        return items.get(rowIndex);
    }
    
    private void remove(int firstRow, int lastRow) {
        for (int index = lastRow; index >= firstRow; index--) {
            items.remove(index);
        }
        
        // Handle model update event in event thread
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                fireTableRowsDeleted(firstRow, lastRow);
            }
        });
    }

    private void update(int firstRow, int lastRow) {
        for (int index = firstRow; index <= lastRow; index++) {
            items.set(index, controller.getValueAt(index));
        }
        
        // Handle model update event in event thread
       EventQueue.invokeLater(new Runnable() {
           public void run() {
               fireTableRowsUpdated(firstRow, lastRow);
           }
       });
    }

    private void insert(int firstRow, int lastRow) {
        for (int index = firstRow; index <= lastRow; index++) {
            items.add(index, controller.getValueAt(index));
        }
        
        // Handle model update event in event thread
       EventQueue.invokeLater(new Runnable() {
           public void run() {
               fireTableRowsInserted(firstRow, lastRow);
           }
       });
        
    }
}
