package com.github.koen_mulder.file_rename_helper.processing.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.gui.rename.workers.FilenameSuggestionWorker;
import com.github.koen_mulder.file_rename_helper.interfaces.IOpenFileActionPublisher;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModel;

public class FileProcessingPanel extends JPanel {

    private static final long serialVersionUID = 7059171924511879920L;
    
    private static final Color PROCESSED_COLOR = new Color(220, 255, 220);
    private static final Color PROCESSING_COLOR = new Color(255, 255, 200);
    private static final Color REQUEUED_COLOR = new Color(255, 182, 193);
    
    private JTable processTable;

    public FileProcessingPanel(AIController aiController, IOpenFileActionPublisher openFileActionPublisher) {

        setPreferredSize(new Dimension(250, 500));
        setMinimumSize(new Dimension(200, 500));
        
        processTable = new JTable();
        processTable.setShowVerticalLines(false);
        processTable.setShowGrid(false);
        processTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        processTable.setFillsViewportHeight(true);
        FileProcessingModel processModel = new FileProcessingModel();
        processTable.setModel(new FileProcessingTableModel(processModel));
//        processTable.setTableHeader(null);
        processTable.getColumnModel().getColumn(0).setCellRenderer(new FileProcessingItemRenderer());
        
     // Add a MouseListener to the JTable
        processTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check for double-click
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    // Get the selected row and column
                    int row = processTable.rowAtPoint(e.getPoint());
                    int col = processTable.columnAtPoint(e.getPoint());

                    // Check if a valid row and column were clicked (important!)
                    if (row >= 0 && col >= 0) {
                        // Get the value at the selected cell
                        FileProcessingItem fileItem = (FileProcessingItem)processTable.getValueAt(row, 0);
                        openFileActionPublisher.notifyOpenFileActionListeners(fileItem);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(processTable);
        scrollPane.revalidate();

        FilenameSuggestionWorker worker = new FilenameSuggestionWorker(aiController, processModel);
        worker.execute();
        
        JButton btnNewButton = new JButton(new SelectFileButtonAction(processModel, this));
        
        JButton btnNewButton_1 = new JButton("Start processing");
        
        JButton btnNewButton_1_1 = new JButton("Remove selection");
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(btnNewButton_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(btnNewButton_1_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                    .addContainerGap())
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnNewButton)
                    .addGap(8)
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(btnNewButton_1_1)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(btnNewButton_1)
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
