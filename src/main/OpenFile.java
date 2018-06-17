package main;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class OpenFile {

	static JFileChooser fileChooser = new JFileChooser();
	Scanner input;
	static File file;
	boolean valid;
	
	public static File selectImage() throws Exception {
		file = fileChooser.getSelectedFile();
		// Open file chooser
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			// check if file is image (png, jpg, pdf, etc)
			if(ImageIO.read(file) != null) {
				return file;
			} else {
				selectImage();
			}			
		}
		return null;
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
