package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
	JFrame jframe;
	JTextArea digitzedTextArea;
	JButton selectImageButton;
    JLabel imageLabel;
    File openedFile;
    
	ImageSelecter() {
		initiallize();
	}

	private void initiallize() {
		// Create JFrame 1280*720
		jframe = new JFrame();
		jframe.setBounds(100, 100, 1280, 720);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.getContentPane().setLayout(null);
		
		// Set to BorderLayout
		BorderLayout border = new BorderLayout();
		Container content = jframe.getContentPane();
	    content.setLayout(border);

	    // JLabel for image
		imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        content.add(imageLabel, BorderLayout.CENTER);
		
		// JTextArea for deciphered message
		digitzedTextArea = new JTextArea();
		digitzedTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		digitzedTextArea.setEditable(false);
		digitzedTextArea.setBounds(500, 500, 300, 200);
		digitzedTextArea.setVisible(false);
		content.add(digitzedTextArea, BorderLayout.EAST);
		
		// Add select image button
		selectImageButton = new JButton("Select Image");
		selectImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					openedFile = OpenFile.selectImage();
					setImage();
					refreshFrame(); // Refreshes JLabel to set image on JFrame
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		content.add(selectImageButton, BorderLayout.SOUTH);
	}
	
	public void setImage() throws IOException {
		BufferedImage img = ImageIO.read(openedFile);
		Image dimg = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(),
		        Image.SCALE_SMOOTH);
		imageLabel.setIcon(new ImageIcon(dimg));	
		refreshFrame(); 

	}
	// Set JLabel ImageIcon to selected image
	public void refreshFrame() {
		ITesseract instance = new Tesseract();
		instance.setDatapath("/Users/littlejkim/ist440w-TesseractDecryption/tessdata");
		
		// Convert image to digitzed text and store with .txt extension
		try {
			 String result = instance.doOCR(openedFile);
			 // Add decipher method here
			 File newTextFile = new File(openedFile.getParentFile() + "/" + FilenameUtils.removeExtension(openedFile.getName()) + ".txt"); // Write in same folder with same file name
		     FileWriter fw = new FileWriter(newTextFile);
		     fw.write(result);
		     fw.close();
			 digitzedTextArea.setText("Deciphered Text : \n\n\n" + result + "\n\n\nSaved to file path: " + newTextFile.getAbsolutePath()); // Set JTextArea to text from Tesseract
			 digitzedTextArea.setVisible(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());	
		}
	}
	
	// Getters and Setters
	public JFrame getJ1() {
		return jframe;
	}

	public void setJ1(JFrame j1) {
		this.jframe = j1;
	}

	public JTextArea getJt1() {
		return digitzedTextArea;
	}

	public void setJt1(JTextArea jt1) {
		this.digitzedTextArea = jt1;
	}

	public JButton getJb1() {
		return selectImageButton;
	}

	public void setJb1(JButton jb1) {
		this.selectImageButton = jb1;
	}
	
	
}
