package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.commons.io.FilenameUtils;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import view.*;
import net.sourceforge.tess4j.*;

public class MainApp extends Application {
	RootLayoutController rootLayoutController;
	double xOffset = 0.0;
	double yOffset = 0.0;
	private Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
		Parent root = loader.load();
		rootLayoutController = loader.getController();
		rootLayoutController.setMainApp(this);

		// set primary stage initial values
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setTitle("OCR Decrypter");
		primaryStage.setResizable(true);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

		// Set offset X, Y values
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}

		});

		// Use offset X, Y values to move stage around
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});

		// Closes application
		rootLayoutController.getClose().setOnAction(event -> {
			primaryStage.close();
			System.exit(0);
		});

		// Select File event handler
		rootLayoutController.getSelectFile().setOnAction(event -> {
			try {
				File selectedFile = chooseFile();
				setImage(selectedFile);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
	}

	// Choose image file
	private File chooseFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select File");
		String userhome = System.getProperty("user.home");
		File defaultDirectory = new File(userhome + "\\Downloads");
		chooser.setInitialDirectory(defaultDirectory);
		File file = chooser.showOpenDialog(this.getPrimaryStage());
		return file;
	}

	private Window getPrimaryStage() {
		return this.primaryStage;
	}

	public void setImage(File file) throws IOException {
		BufferedImage img = ImageIO.read(file);
		rootLayoutController.getImageView().setImage(SwingFXUtils.toFXImage(img, null));
		setText(file);
	}

	// Set JLabel ImageIcon to selected image
	public void setText(File file) {
		System.out.println("hi");
		ITesseract instance = new Tesseract();
		instance.setDatapath("/tessdata");

		// Convert image to digitzed text and store with .txt extension
		try {
			String result = instance.doOCR(file);
			// Add decipher method here
			File newTextFile = new File(file.getParentFile() + "/"
					+ FilenameUtils.removeExtension(file.getName()) + ".txt"); // Write in same folder with same
																						// file name
			FileWriter fw = new FileWriter(newTextFile);
			fw.write(result);
			fw.close();
			rootLayoutController.getTextArea().setText(
					"Deciphered Text : \n\n\n" + result + "\n\n\nSaved to file path: " + newTextFile.getAbsolutePath()); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
