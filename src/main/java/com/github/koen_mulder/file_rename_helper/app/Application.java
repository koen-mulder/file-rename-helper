/**
 * 
 */
package com.github.koen_mulder.file_rename_helper.app;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Application {

    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        logger.debug("Main has been called!");
        
        
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
                    ApplicationController applicationController = new ApplicationController();
                    ApplicationWindow window = new ApplicationWindow(applicationController);
                    
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
