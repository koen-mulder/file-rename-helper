/**
 * 
 */
package main.java;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Application {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		// Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		// Set up the window
		JFrame frame = new JFrame("File rename helper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//TODO: Set minimum size and default size
		frame.setSize(800, 600);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		// Setup the panels
		FileDisplayPanel fileDisplayPanel = new FileDisplayPanel();
		frame.add(fileDisplayPanel);
		
		// Display the window
		frame.setVisible(true);
	}
}
