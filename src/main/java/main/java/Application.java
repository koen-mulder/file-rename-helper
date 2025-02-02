/**
 * 
 */
package main.java;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {

    // Create a logger instance
    private static final Logger logger = LogManager.getLogger(Application.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        // Log messages at various levels
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
                    ApplicationWindow window = new ApplicationWindow();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
