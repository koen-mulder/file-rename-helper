package com.github.koen_mulder.file_rename_helper.gui.rename;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.controller.AIController.FilenameSuggestions;
import com.github.koen_mulder.file_rename_helper.interfaces.FileSelectionListener;
import com.github.koen_mulder.file_rename_helper.interfaces.FileSelectionPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.FormClearListener;
import com.github.koen_mulder.file_rename_helper.interfaces.FormClearPublisher;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionListener;
import com.github.koen_mulder.file_rename_helper.interfaces.SuggestionPublisher;
import com.google.common.collect.Lists;

public class FileRenamePanel extends JPanel implements FileSelectionPublisher, SuggestionPublisher, FormClearPublisher {

    private static final long serialVersionUID = 5393373407385885597L;

    private ArrayList<FileSelectionListener> fileSelectionListeners = Lists.newArrayList();
    private ArrayList<SuggestionListener> suggestionListeners = Lists.newArrayList();
    private ArrayList<FormClearListener> formClearListeners = Lists.newArrayList();

    public AIController aiController;
    private JTextField newFilenameField;

    private JList<String> suggestedFilenameList;

    private DefaultListModel<String> suggestedFilenameListModel;

    private JScrollPane suggestedFilenameScrollPane;

    private KeywordButtonPanel importantKeywordPanel;

    public FileRenamePanel(AIController aiController) {
        this.aiController = aiController;

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
                    // Now that the GUI is all in place, we can try opening a PDF
                    notifyFileSelectionListeners(selectedFile.getAbsolutePath());
                    // Start worker for the slow process of LLM API calls
                    startGeneratePossibleFileNamesWorker(selectedFile.getAbsolutePath());
                    // Clear forms of previous suggestions
                    notifyFormClearListeners();
                }
            }
        });

        JSeparator separator = new JSeparator();

        JLabel lblNewLabel = new JLabel("Select suggested filename:");

        JLabel lblNewLabel_1 = new JLabel("New filename:");

        newFilenameField = new JTextField();
        newFilenameField.setColumns(10);

        suggestedFilenameScrollPane = new JScrollPane();

        JPanel replaceCharacterPanel = new JPanel();
        FlowLayout fl_replaceCharacterPanel = (FlowLayout) replaceCharacterPanel.getLayout();
        fl_replaceCharacterPanel.setAlignment(FlowLayout.LEFT);
        replaceCharacterPanel
                .setBorder(new TitledBorder(null, "Replace", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        JPanel removeCharactersPanel = new JPanel();
        FlowLayout fl_removeCharactersPanel = (FlowLayout) removeCharactersPanel.getLayout();
        fl_removeCharactersPanel.setAlignment(FlowLayout.LEFT);
        removeCharactersPanel
                .setBorder(new TitledBorder(null, "Remove", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        importantKeywordPanel = new KeywordButtonPanel(newFilenameField);
        addFormClearListener(importantKeywordPanel);
        
        JButton moreSuggestionsButton = new JButton("Get more suggestions");
        moreSuggestionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGenerateAdditionalPossibleFileNamesWorker();
            }
        });

        JButton clearSuggestionsButton = new JButton("Clear suggestion list");
        clearSuggestionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                suggestedFilenameListModel.clear();
            }
        });

        JPanel panel_3 = new JPanel();
        FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
        flowLayout_3.setAlignment(FlowLayout.LEFT);
        panel_3.setBorder(new TitledBorder(null, "Add", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
                .createSequentialGroup().addContainerGap()
                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                        .addComponent(suggestedFilenameScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 789,
                                Short.MAX_VALUE)
                        .addComponent(separator, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                        .addGroup(Alignment.LEADING,
                                groupLayout.createSequentialGroup().addComponent(selectFileLabel)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(fileNameField, GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(selectFileButton))
                        .addComponent(lblNewLabel, Alignment.LEADING)
                        .addGroup(Alignment.LEADING,
                                groupLayout.createSequentialGroup().addComponent(moreSuggestionsButton)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(clearSuggestionsButton))
                        .addComponent(importantKeywordPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 789,
                                Short.MAX_VALUE)
                        .addGroup(Alignment.LEADING,
                                groupLayout.createSequentialGroup().addComponent(lblNewLabel_1)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(newFilenameField, GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE))
                        .addComponent(removeCharactersPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 789,
                                Short.MAX_VALUE)
                        .addComponent(replaceCharacterPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 789,
                                Short.MAX_VALUE))
                .addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup().addContainerGap()
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(selectFileLabel)
                                .addComponent(fileNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(selectFileButton))
                        .addGap(8)
                        .addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(lblNewLabel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(suggestedFilenameScrollPane, GroupLayout.PREFERRED_SIZE, 195,
                                GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(moreSuggestionsButton).addComponent(clearSuggestionsButton))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_1)
                                .addComponent(newFilenameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(importantKeywordPanel, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addComponent(replaceCharacterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(removeCharactersPanel,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

        JButton btnNewButton_4 = new JButton("New button");
        panel_3.add(btnNewButton_4);

        JButton removeConsecutiveSpecialCharactersButton = new JButton("Consecutive special characters (! ?/\\_-)");
        removeCharactersPanel.add(removeConsecutiveSpecialCharactersButton);

        JButton replaceSpaceWithUnderscoreButton = new JButton("[space] → _ [underscore]");
        replaceSpaceWithUnderscoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newFilenameField.setText(newFilenameField.getText().replace(" ", "_"));
            }
        });
        replaceCharacterPanel.add(replaceSpaceWithUnderscoreButton);

        JButton replaceDashWithUnderscoreButton = new JButton("[dash] - → _ [underscore]");
        replaceDashWithUnderscoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newFilenameField.setText(newFilenameField.getText().replace("-", "_"));
            }
        });
        replaceCharacterPanel.add(replaceDashWithUnderscoreButton);

        JButton replaceUnderscoreWithSpace = new JButton("[underscore] _ → [space]");
        replaceUnderscoreWithSpace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newFilenameField.setText(newFilenameField.getText().replace("_", " "));
            }
        });
        replaceCharacterPanel.add(replaceUnderscoreWithSpace);

        suggestedFilenameList = new JList<>();
        suggestedFilenameListModel = new DefaultListModel<String>();
        suggestedFilenameList.setModel(suggestedFilenameListModel);
        suggestedFilenameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestedFilenameList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String suggestedFilename = suggestedFilenameList.getSelectedValue();

                if (suggestedFilename == null) {
                    // No file selected so do nothing
                    return;
                }

                newFilenameField.setText(suggestedFilename);
                newFilenameField.grabFocus();

                int extensionIndex = suggestedFilename.lastIndexOf(".");
                if (extensionIndex > 0) {
                    newFilenameField.setCaretPosition(extensionIndex);
                }

                suggestedFilenameList.clearSelection();
            }
        });
        suggestedFilenameScrollPane.setViewportView(suggestedFilenameList);
        suggestedFilenameList.setVisibleRowCount(10);
        setLayout(groupLayout);
    }

    protected void startGeneratePossibleFileNamesWorker(String filePath) {
        SwingWorker<FilenameSuggestions, Integer> worker = new SwingWorker<>() {

            @Override
            protected FilenameSuggestions doInBackground() throws Exception {
                return aiController.generatePossibleFileNames(filePath);
            }

            @Override
            protected void done() {
                // this method is called when the background
                // thread finishes execution
                try {
                    FilenameSuggestions result = get();

                    suggestedFilenameListModel.clear();
                    suggestedFilenameListModel.addAll(result.possibleFilenames());

                    notifySuggestionListeners(result);

                    importantKeywordPanel.revalidate();
                    importantKeywordPanel.repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    protected void startGenerateAdditionalPossibleFileNamesWorker() {
        SwingWorker<FilenameSuggestions, Integer> worker = new SwingWorker<>() {

            @Override
            protected FilenameSuggestions doInBackground() throws Exception {
                return aiController.getAdditionalFilenameSuggestions();
            }

            @Override
            protected void done() {
                // this method is called when the background
                // thread finishes execution
                try {
                    FilenameSuggestions result = get();
                    suggestedFilenameListModel.addAll(result.possibleFilenames());
                    int size = suggestedFilenameListModel.getSize();
                    suggestedFilenameList.ensureIndexIsVisible(size - 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    @Override
    public void addFileSelectionListener(FileSelectionListener listener) {
        this.fileSelectionListeners.add(listener);

    }

    @Override
    public void removeFileSelectionListener(FileSelectionListener listener) {
        this.fileSelectionListeners.remove(listener);
    }

    @Override
    public void notifyFileSelectionListeners(String filePath) {
        for (FileSelectionListener listener : fileSelectionListeners) {
            listener.onFileSelected(filePath);
        }
    }

    @Override
    public void addSuggestionListener(SuggestionListener listener) {
        this.suggestionListeners.add(listener);
    }

    @Override
    public void removeSuggestionListener(SuggestionListener listener) {
        this.suggestionListeners.remove(listener);
        
    }

    @Override
    public void notifySuggestionListeners(FilenameSuggestions suggestions) {
        for (SuggestionListener listener : suggestionListeners) {
            listener.onSuggestionsGenerated(suggestions);
        }
    }

    @Override
    public void addFormClearListener(FormClearListener listener) {
        this.formClearListeners.add(listener);
    }

    @Override
    public void removeFormClearListener(FormClearListener listener) {
        this.formClearListeners.remove(listener);
    }

    @Override
    public void notifyFormClearListeners() {
        for (FormClearListener listener : formClearListeners) {
            listener.onClearFormEvent();
        }
    }
}
