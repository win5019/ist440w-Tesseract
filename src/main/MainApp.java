package main;

import java.io.IOException;
import net.sourceforge.tess4j.*;

public class MainApp {
	static ImageSelecter imageSelecter;
	
	public static void main(String[] args) throws IOException {
		imageSelecter = new ImageSelecter();
		imageSelecter.getJ1().setVisible(true);
	}
}
