package tess4J;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.apache.commons.io.FilenameUtils;

import net.sourceforge.tess4j.*;

public class ImageSelecter {
	JFrame j1;
	JTextArea jt1;
	JButton jb1, jb2;
    JLabel jl1;
    OpenFile of;
    private String digitizedTextFilePath;
    
	

	ImageSelecter() {
		initiallize();
	}

	private void initiallize() {
		// Create JFrame 1280*720
		j1 = new JFrame();
		j1.setBounds(100, 100, 1280, 720);
		j1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j1.getContentPane().setLayout(null);
		
		// Set to BorderLayout
		BorderLayout border = new BorderLayout();
		Container content = j1.getContentPane();
	    content.setLayout(border);

	    // JLabel for image
		jl1 = new JLabel();
        jl1.setHorizontalAlignment(JLabel.CENTER);
        jl1.setVerticalAlignment(JLabel.CENTER);
        content.add(jl1, BorderLayout.CENTER);
		
		// JTextArea for deciphered message
		jt1 = new JTextArea();
		jt1.setFont(new Font("Monospaced", Font.PLAIN, 14));
		jt1.setEditable(false);
		jt1.setBounds(500, 500, 300, 200);
		jt1.setVisible(false);
		content.add(jt1, BorderLayout.EAST);
		
		// Add select image button
		jb1 = new JButton("Select Image");
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				of = new OpenFile();
				try {
					of.selectImage(); // Select Image
					refreshFrame(); // Refreshes JLabel to set image on JFrame
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		content.add(jb1, BorderLayout.SOUTH);
	}

	// Set JLabel ImageIcon to selected image
	public void refreshFrame() {
		try {
			jl1.setIcon(new ImageIcon(ImageIO.read(of.getFile().getAbsoluteFile())));
			ITesseract instance = new Tesseract();
			instance.setDatapath("C:\\Users\\Win\\git\\ist440w-TesseractDecryption\\tessdata");
			
			// Convert image to digitized text and store with .txt extension
			try {
				 String result = instance.doOCR(of.getFile());
				 // Add decipher method here
				 File newTextFile = new File(of.getFile().getParentFile() + "/" + FilenameUtils.removeExtension(of.getFile().getName()) + ".txt"); // Write in same folder with same file name
		         FileWriter fw = new FileWriter(newTextFile);
		         fw.write(result);
		         fw.close();
				 jt1.setText("Deciphered Text : \n\n\n" + result + "\n\n\nSaved to file path: " + newTextFile.getAbsolutePath()); // Set JTextArea to text from Tesseract
				 jt1.setVisible(true);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Getters and Setters
	public JFrame getJ1() {
		return j1;
	}

	public void setJ1(JFrame j1) {
		this.j1 = j1;
	}

	public JTextArea getJt1() {
		return jt1;
	}

	public void setJt1(JTextArea jt1) {
		this.jt1 = jt1;
	}

	public JButton getJb1() {
		return jb1;
	}

	public void setJb1(JButton jb1) {
		this.jb1 = jb1;
	}
	
	public String getDigitizedTextFilePath() {
		return digitizedTextFilePath;
	}

	public void setDigitizedTextFilePath(String digitizedTextFilePath) {
		this.digitizedTextFilePath = digitizedTextFilePath;
	}
	
}
