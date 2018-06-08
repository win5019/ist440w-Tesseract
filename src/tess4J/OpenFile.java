package tess4J;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class OpenFile {

	JFileChooser fileChooser = new JFileChooser();
	Scanner input;
	File file;
	boolean valid;
	
	public void selectImage() throws Exception {
		
		// Open file chooser
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			try {
			    Image image = ImageIO.read(file); 
			    // if else statement to take only image file extensions
			    if (image == null) {
			        valid = false;
			        System.out.println("The file could not be opened, it is not an image");
			        selectImage(); // loops until user selects image file
			    }
			} catch(IOException ex) {
			    valid = false;
			    System.out.println("The file could not be opened, an error occurred.");
			}
			
		}
	}

	// Getters and Setters
	JFileChooser getFileChooser() {
		return fileChooser;
	}

	void setFileChooser(JFileChooser fileChooser) {
		this.fileChooser = fileChooser;
	}

	Scanner getInput() {
		return input;
	}

	void setInput(Scanner input) {
		this.input = input;
	}

	File getFile() {
		return file;
	}

	void setFile(File file) {
		this.file = file;
	}

	boolean isValid() {
		return valid;
	}

	void setValid(boolean valid) {
		this.valid = valid;
	}
	
	

}
