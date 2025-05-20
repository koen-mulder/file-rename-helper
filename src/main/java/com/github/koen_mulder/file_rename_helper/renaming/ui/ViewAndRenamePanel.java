package com.github.koen_mulder.file_rename_helper.renaming.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.project.ActiveProjectController;
import com.github.koen_mulder.file_rename_helper.renaming.FileRenameController;
import com.github.koen_mulder.file_rename_helper.viewing.FileViewPanel;

@SuppressWarnings("serial") // Same-version serialization only
public class ViewAndRenamePanel extends JPanel {

    private static final String NO_OPENED_FILE_CARD = "No opened file card";
    private static final String OPENED_FILE_CARD = "Opened file card";
    
    private ActiveProjectController activeProjectController;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public ViewAndRenamePanel(ActiveProjectController activeProjectController,
            JFrame applicationFrame) {
        
        this.activeProjectController = activeProjectController;
        
        setLayout(new BorderLayout(0, 0));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(new JPanel(), NO_OPENED_FILE_CARD);
        cardPanel.add(createFileViewAndRenameSplitPane(applicationFrame), OPENED_FILE_CARD);
        
        add(cardPanel, BorderLayout.CENTER);
         
        addListeners();
    }
    
    public JSplitPane createFileViewAndRenameSplitPane(JFrame applicationFrame) {
        JSplitPane fileViewAndRenameSplitPane = new JSplitPane();
        
        FileViewPanel fileViewPanel = new FileViewPanel(
                activeProjectController.getOpenFileActionPublisher(), applicationFrame);
        fileViewAndRenameSplitPane.add(fileViewPanel, JSplitPane.LEFT);
        
        FileRenamePanel fileRenamePanel = new FileRenamePanel();
        fileViewAndRenameSplitPane.add(fileRenamePanel, JSplitPane.RIGHT);
        
        new FileRenameController(fileRenamePanel, activeProjectController);
        
        return fileViewAndRenameSplitPane;
    }
    

    private void addListeners() {
        activeProjectController.getOpenFileActionPublisher()
                .addOpenFileActionListener(new IOpenFileActionListener() {
            
            @Override
            public void onOpenFileAction(FileProcessingItem fileItem) {
                cardLayout.show(cardPanel, OPENED_FILE_CARD);
            }
        });
    }
    
}
