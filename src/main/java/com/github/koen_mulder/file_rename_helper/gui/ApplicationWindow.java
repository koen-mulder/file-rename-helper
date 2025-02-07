package com.github.koen_mulder.file_rename_helper.gui;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.koen_mulder.file_rename_helper.config.AIConfigManager;
import com.github.koen_mulder.file_rename_helper.config.WindowConfigManager;
import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.gui.rename.FileRenamePanel;

public class ApplicationWindow {

    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(ApplicationWindow.class);

    private JFrame frame;

    /**
     * Create the application.
     */
    public ApplicationWindow(AIController aiController) {
        initialize(aiController);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(AIController aiController) {
        WindowConfigManager configManager = WindowConfigManager.getInstance();
        AIConfigManager aiConfigManager = AIConfigManager.getInstance();

        frame = new JFrame("File rename helper");
        frame.setBounds(configManager.getWindowBounds());
        frame.setExtendedState(frame.getExtendedState() | configManager.getWindowExtendedState());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel statusBarPanel = new JPanel();
        frame.getContentPane().add(statusBarPanel, BorderLayout.SOUTH);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel renameTabPanel = new JPanel();
        tabbedPane.addTab("Rename", null, renameTabPanel, null);
        renameTabPanel.setLayout(new BorderLayout(0, 0));

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(configManager.getSplitPaneDividerLocation());
        splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                logger.debug("Split pane divider location changed and new location stored in configuration.");
                configManager.setSplitPaneDividerLocation(splitPane.getDividerLocation());
            }
        });
        renameTabPanel.add(splitPane);

        FileViewPanel fileViewPanel = new FileViewPanel(frame);
        splitPane.setLeftComponent(fileViewPanel);
        fileViewPanel.setLayout(new BoxLayout(fileViewPanel, BoxLayout.X_AXIS));

        FileRenamePanel fileRenamePanel = new FileRenamePanel(aiController);
        fileRenamePanel.addFileSelectionListener(fileViewPanel);
        splitPane.setRightComponent(fileRenamePanel);

        JPanel configurationTabPanel = new JPanel();
        tabbedPane.addTab("Configuration", null, configurationTabPanel, null);
        configurationTabPanel.setLayout(new BorderLayout(0, 0));
        
        ConfigurationPanel configurationPanel = new ConfigurationPanel();

        configurationTabPanel.add(configurationPanel, BorderLayout.CENTER);
        
        frame.addWindowListener(aiController.getWindowListener());
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (aiConfigManager.isConfigChanged()) {
                    int result = JOptionPane.showConfirmDialog(frame, "Do you want to save the configuration changes?",
                            "Save configuration changes", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                        return;
                    } else if (result == JOptionPane.YES_OPTION) {
                        logger.debug("Configuration changes saved.");
                        aiConfigManager.saveConfig();
                    }
                }
                // Always save window config
                if (configManager.isConfigChanged()) {
                    configManager.saveConfig();
                }
                System.exit(0);
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (frame.getExtendedState() == JFrame.ICONIFIED) {
                    // Don't store window minimized state because we wouldn't want the app to start minimized
                    return;
                }
                
                if (!frame.getBounds().equals(configManager.getWindowBounds())) {
                    logger.debug("Window resized and new bounds stored in configuration.");
                    configManager.setWindowExtendedState(frame.getExtendedState());
                    if (frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                        // Don't store window location and size when maximized
                        configManager.setWindowBounds(frame.getBounds());
                    }
                }
            }
        });
    }

    public void setVisible(boolean visible) {
        this.frame.setVisible(visible);
    }
}
