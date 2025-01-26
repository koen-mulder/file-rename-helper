package main.java;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;


public class FileDisplayPanel extends JPanel {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 4439798541108803748L;
	
	private JPanel filenamePanel;
	private JLabel filenameLabel;
	private JTextField filenameField;
	private JButton openFileChooserButton;
	private JFileChooser fileChooser;
	
	private JPanel pdfPanel;

	public FileDisplayPanel() {
		super();
		
		pdfPanel = new JPanel();
		
		filenamePanel = new JPanel();
		
		JLabel picLabel = new JLabel();
		pdfPanel.add(picLabel);
		
		filenameLabel = new JLabel("Filename:");
		
		filenameField = new JTextField();
		filenameField.setEditable(false);
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setMultiSelectionEnabled(false);
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
		
		openFileChooserButton = new JButton("Select file");
		openFileChooserButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = fileChooser.showOpenDialog(FileDisplayPanel.this);
				if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filenameField.setText(selectedFile.getName());
                    
                    try {
						PDDocument doc = Loader.loadPDF(selectedFile);
						PDFRenderer renderer = new PDFRenderer(doc);
//						renderer.setSubsamplingAllowed(true);
//						BufferedImage image = renderer.renderImageWithDPI(0, 100);
						BufferedImage image = renderer.renderImage(0);
						picLabel.setIcon(new ImageIcon(image));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
			}
		});
		
		// Fill fields with default values
		clearPanel();

		// Add filename fields to the filename panel
		filenamePanel.add(filenameLabel);
		filenamePanel.add(filenameField);
		filenamePanel.add(openFileChooserButton);
		
		// Add the panels to the main panel
		add(filenamePanel);
		add(pdfPanel);
	}

	/**
	 * Set fields to default values
	 */
	private void clearPanel() {
		this.filenameField.setText("No file selected");
	}
}
