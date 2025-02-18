/**
 * 
 */
package com.github.koen_mulder.file_rename_helper.application;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.koen_mulder.file_rename_helper.controller.AIController;
import com.github.koen_mulder.file_rename_helper.controller.OpenFileActionPublisher;
import com.github.koen_mulder.file_rename_helper.gui.ApplicationWindow;
import com.github.koen_mulder.file_rename_helper.processing.FileProcessingModel;


public class Application {

    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        // Log messages at various levels
        logger.debug("Main has been called!");
        
        // Setup controllers
        AIController aiController = new AIController();
        OpenFileActionPublisher openFileActionPublisher = new OpenFileActionPublisher();
        FileProcessingModel fileProcessingModel = new FileProcessingModel();
        
        // Set the look and feel to the system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            // Do nothing, just use the default look and feel
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ApplicationWindow window = new ApplicationWindow(aiController, openFileActionPublisher,
                            fileProcessingModel);
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
