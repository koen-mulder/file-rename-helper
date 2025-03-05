package com.github.koen_mulder.file_rename_helper.processing.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.koen_mulder.file_rename_helper.app.Application;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModelController;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionPublisher;
import com.github.koen_mulder.file_rename_helper.suggestions.AIController;
import com.github.koen_mulder.file_rename_helper.suggestions.FilenameSuggestionWorker;

public class FileProcessingPanel extends JPanel {

    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    
    private static final long serialVersionUID = 7059171924511879920L;
    
    private static final Color RENAMED_COLOR = new Color(237, 237, 235);
    private static final Color PROCESSED_COLOR = new Color(220, 255, 220);
    private static final Color PROCESSING_COLOR = new Color(255, 255, 200);
    private static final Color REQUEUED_COLOR = new Color(255, 182, 193);
    
    private JTable processTable;

    protected FileProcessingItem activeFileItem;

    protected FileProcessingItem previousFileItem;

    public FileProcessingPanel(AIController aiController,
            FileProcessingModelController fileProcessingModelController,
            IOpenFileActionPublisher openFileActionPublisher) {

        setPreferredSize(new Dimension(250, 500));
        setMinimumSize(new Dimension(200, 500));
        
        processTable = new JTable();
        processTable.setShowVerticalLines(false);
        processTable.setShowGrid(false);
        processTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        processTable.setFillsViewportHeight(true);
        processTable.setModel(new FileProcessingTableModel(fileProcessingModelController));
        // processTable.setTableHeader(null);
        processTable.getColumnModel().getColumn(0)
                .setCellRenderer(new FileProcessingItemRenderer());

        // Add a MouseListener to the JTable
        processTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                // Check for double-click
                if (event.getClickCount() == 2 && event.getButton() == MouseEvent.BUTTON1) {
                    // Get the selected row and column
                    int row = processTable.rowAtPoint(event.getPoint());
                    int col = processTable.columnAtPoint(event.getPoint());

                    // Check if a valid row and column were clicked (important!)
                    if (row >= 0 && col >= 0) {
                        // Get the value at the selected cell
                        FileProcessingItem fileItem = (FileProcessingItem) processTable
                                .getValueAt(row, 0);

                        // Do not open the same file again
                        if (fileItem == activeFileItem) {
                            return;
                        }

                        // If the PDF viewer would not lock this hassle with the temporary
                        // file could be avoided and we would not have to notify the listeners twice
                        // TODO: Fix PDF viewer locking file

                        Path originalFilePath = Path.of(fileItem.getOriginalAbsoluteFilePath());
                        try {
                            // Copy the original file to a temporary file because the PDF viewer
                            // locks the file
                            Files.copy(originalFilePath, fileItem.getTemporaryFilePath(),
                                    StandardCopyOption.REPLACE_EXISTING);
                            // Notify listeners that a file is opened
                            openFileActionPublisher.notifyOpenFileActionListeners(fileItem);
                            
                            // Store the previous and active file item
                            previousFileItem = activeFileItem;
                            activeFileItem = fileItem;
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(processTable,
                                    "Error creating temporary file: "
                                            + fileItem.getTemporaryFilePath(),
                                    "Error", JOptionPane.ERROR_MESSAGE);

                            logger.error("Error creating temporary file for displaying.", e);
                        }
                        
                        SwingUtilities.invokeLater(() -> {
                            // Remove the previous temporary file
                            if (previousFileItem != null) {
                                try {
                                    Files.delete(previousFileItem.getTemporaryFilePath());
                                } catch (IOException e) {
                                    logger.error("Error deleting temporary file.", e);
                                }
                            }
                        });

                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(processTable);
        scrollPane.revalidate();

        FilenameSuggestionWorker worker = new FilenameSuggestionWorker(aiController, fileProcessingModelController);
        worker.execute();
        
        JButton btnNewButton = new JButton(new SelectFileButtonAction(fileProcessingModelController, this));
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                    .addContainerGap())
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnNewButton)
                    .addGap(8)
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                    .addContainerGap())
        );
        

        setLayout(groupLayout);
    }
    
    @SuppressWarnings("serial") // Same-version serialization only
    class FileProcessingItemRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {

            FileProcessingItem item = (FileProcessingItem) value;
            Component c = super.getTableCellRendererComponent(table, item.getOriginalFileName(), isSelected, hasFocus,
                    row, column);
            setToolTipText(item.getOriginalAbsoluteFilePath());

            if (!isSelected) {
                switch (item.getState()) {
                case RENAMED:
                    c.setBackground(RENAMED_COLOR);
                    break;
                case PROCESSING:
                    c.setBackground(PROCESSING_COLOR);
                    break;
                case REQUEUED:
                    c.setBackground(REQUEUED_COLOR);
                    break;
                case PROCESSED:
                    c.setBackground(PROCESSED_COLOR);
                    break;
                default:
                    c.setBackground(table.getBackground());
                    break;
                }
            }
            
            // Add some padding before and after a filename
            ((JComponent) c).setBorder(BorderFactory
                    .createCompoundBorder(getBorder(), new EmptyBorder(0, 3, 0, 3)));
            
            return c;
        }
    }

}
