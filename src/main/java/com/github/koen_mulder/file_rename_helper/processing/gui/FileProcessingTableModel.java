package com.github.koen_mulder.file_rename_helper.processing.gui;

import java.awt.EventQueue;

import javax.swing.table.AbstractTableModel;

import com.github.koen_mulder.file_rename_helper.interfaces.FileProcessingModelListener;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModel;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelEvent;

public class FileProcessingTableModel extends AbstractTableModel implements FileProcessingModelListener {

    private static final long serialVersionUID = -3670356454612137435L;

    private FileProcessingModel processModel;
    
    
    public FileProcessingTableModel(FileProcessingModel processModel) {
        this.processModel = processModel;
        processModel.addFileProcessingModelListener(this);
    }

    @Override
    public int getRowCount() {
        return processModel.getRowCount();
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
        return processModel.getValueAt(rowIndex);
    }

    @Override
    public void tableChanged(FileProcessingModelEvent e) {
        // Handle model update event in event thread
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (e.getType() == FileProcessingModelEvent.DELETE) {
                    fireTableRowsDeleted(e.getFirstRow(), e.getLastRow());
                } else if (e.getType() == FileProcessingModelEvent.INSERT) {
                    fireTableRowsInserted(e.getFirstRow(), e.getLastRow());
                } else if (e.getType() == FileProcessingModelEvent.UPDATE) {
                    fireTableRowsUpdated(e.getFirstRow(), e.getLastRow());
                } else {
                    fireTableDataChanged();
                }
            }
        });
    }
}
