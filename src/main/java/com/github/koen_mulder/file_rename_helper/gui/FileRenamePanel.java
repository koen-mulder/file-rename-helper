package com.github.koen_mulder.file_rename_helper.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.github.koen_mulder.file_rename_helper.interfaces.FileSelectionListener;
import com.github.koen_mulder.file_rename_helper.interfaces.FileSelectionPublisher;
import com.google.common.collect.Lists;

public class FileRenamePanel extends JPanel implements FileSelectionPublisher {

    private static final long serialVersionUID = 5393373407385885597L;
   
    private ArrayList<FileSelectionListener> listeners;

    public FileRenamePanel() {
        listeners = Lists.newArrayList();
        
        // Create a file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        // Currently only allow PDF files to be selected
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        // Allow only processing one file at a time
        fileChooser.setMultiSelectionEnabled(false);
        // Temporary set current directory to test files
        fileChooser.setCurrentDirectory(new File("E:\\tools\\rename-pdf-files\\test-files\\unorganised"));
        // Filter to only show PDF files
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "PDF Files (.pdf)";
            }
            
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".pdf");
            }
        });
        
        JLabel selectFileLabel = new JLabel("Select file:");
        
        JTextField fileNameField = new JTextField();
        fileNameField.setEditable(false);
        fileNameField.setColumns(10);
        
        JButton selectFileButton = new JButton("Select File");
        selectFileButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(FileRenamePanel.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileNameField.setText(selectedFile.getName());
                    // Now that the GUI is all in place, we can try openning a PDF
                    notifyFileSelectionListeners(selectedFile.getAbsolutePath());
                }
            }
        });
        
        JSeparator separator = new JSeparator();
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(6)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(selectFileLabel)
                            .addGap(6)
                            .addComponent(fileNameField, GroupLayout.PREFERRED_SIZE, 301, GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(selectFileButton))
                        .addComponent(separator, GroupLayout.PREFERRED_SIZE, 444, GroupLayout.PREFERRED_SIZE)))
        );
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addGap(6)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGap(4)
                            .addComponent(selectFileLabel))
                        .addGroup(groupLayout.createSequentialGroup()
                            .addGap(1)
                            .addComponent(fileNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addComponent(selectFileButton))
                    .addGap(6)
                    .addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        setLayout(groupLayout);
    }
    
    public void addFileSelectionListener() {

    }

    @Override
    public void addFileSelectionListener(FileSelectionListener listener) {
        this.listeners.add(listener);
        
    }

    @Override
    public void removeFileSelectionListener(FileSelectionListener listener) {
        this.listeners.remove(listener);        
    }

    @Override
    public void notifyFileSelectionListeners(String filePath) {
        for (FileSelectionListener listener : listeners) {
            listener.onFileSelected(filePath);
        }
    }
}
