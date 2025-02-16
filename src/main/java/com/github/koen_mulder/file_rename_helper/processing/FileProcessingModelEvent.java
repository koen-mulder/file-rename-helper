package com.github.koen_mulder.file_rename_helper.processing;

@SuppressWarnings("serial") // Same-version serialization only
public class FileProcessingModelEvent extends java.util.EventObject {

    /** Identifies the addition of new rows. */
    public static final int INSERT =  1;
    /** Identifies a change to existing data. */
    public static final int UPDATE =  0;
    /** Identifies the removal of rows. */
    public static final int DELETE = -1;
    
    /**
     * The type of the event.
     */
    protected int type;
    /**
     * The first row that has changed.
     */
    protected int firstRow;
    /**
     * The last row that has changed.
     */
    protected int lastRow;
    
    public FileProcessingModelEvent(FileProcessingModel source) {
     // Use Integer.MAX_VALUE instead of getRowCount() in case rows were deleted.
        this(source, 0, Integer.MAX_VALUE, UPDATE);
    }
    
    public FileProcessingModelEvent(FileProcessingModel source, int firstRow, int lastRow, int type) {
        super(source);
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.type = type;
    }
    
    public int getFirstRow() { return firstRow; }

    public int getLastRow() { return lastRow; }

    public int getType() { return type; }
}
