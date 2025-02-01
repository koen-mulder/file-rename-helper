package main.java;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class ApplicationWindow {

    private JFrame frame;

    /**
     * Create the application.
     */
    public ApplicationWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("File rename helper");
        //TODO: Figure out window sizing
        frame.setBounds(100, 100, 1280, 720);
//        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel statusBarPanel = new JPanel();
        frame.getContentPane().add(statusBarPanel, BorderLayout.SOUTH);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel renameTabPanel = new JPanel();
        tabbedPane.addTab("Rename", null, renameTabPanel, null);
        renameTabPanel.setLayout(new BorderLayout(0, 0));

        JSplitPane splitPane = new JSplitPane();
        renameTabPanel.add(splitPane);

        FileViewPanel fileViewPanel = new FileViewPanel(frame);
        splitPane.setLeftComponent(fileViewPanel);
        fileViewPanel.setLayout(new BoxLayout(fileViewPanel, BoxLayout.X_AXIS));
        
        FileRenamePanel fileRenamePanel = new FileRenamePanel();
        fileRenamePanel.addFileSelectionListener(fileViewPanel);
        splitPane.setRightComponent(fileRenamePanel);

        JPanel configurationTabPanel = new JPanel();
        tabbedPane.addTab("Configuration", null, configurationTabPanel, null);
    }

    protected void setVisible(boolean visible) {
        this.frame.setVisible(visible);
    }

}
