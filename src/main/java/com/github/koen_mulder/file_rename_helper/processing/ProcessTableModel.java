package com.github.koen_mulder.file_rename_helper.processing;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ProcessTableModel<T> extends AbstractTableModel {

    private ProcessModel<T> processModel;
    
    private List<Enum<?>> columns;
    
    public ProcessTableModel(List<Enum<?>> columns) {
        this.columns = columns;
    }

    @Override
    public int getRowCount() {
        return processModel.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return processModel.getValueAt(rowIndex);
    }

}
