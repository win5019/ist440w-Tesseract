package tess4J;

import java.io.File;
import java.io.IOException;

import net.sourceforge.tess4j.*;

public class test {
	public static void main(String[] args) throws IOException {
		File imageFile = new File("/Users/littlejkim/ist440w-TesseractDecryption/images/1.jpg");
		
		ITesseract instance = new Tesseract();
		instance.setDatapath("/Users/littlejkim/ist440w-TesseractDecryption/tessdata");
		
		try {
			 String result = instance.doOCR(imageFile);
			 System.out.println(result);
		} catch (TesseractException e) {
			System.out.println(e.getMessage());
		}
	}
}
