package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import org.apache.commons.io.FilenameUtils;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
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
	File selectedFile;
	String digitizedText;
	String decryptedText;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Set FXML to view
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
		Parent root = loader.load();
		rootLayoutController = loader.getController();
		rootLayoutController.setStatusLabel("Waiting for image to be selected... (File > Select File)");
		rootLayoutController.setMainApp(this);

		// set stage initial values
		primaryStage.initStyle(StageStyle.UNIFIED);
		primaryStage.setTitle("OCR Decrypter");
		primaryStage.setResizable(false);
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

		// Action when stage is closed
		rootLayoutController.getClose().setOnAction(event -> {
			primaryStage.close();
			System.exit(0);
		});

		// Select File action listener. disables digitize button when canceled
		rootLayoutController.getSelectFile().setOnAction(event -> {
			try {
				boolean selected = chooseFile();
				if (selected) {
					rootLayoutController.getDigitizeButton().setDisable(false);
					rootLayoutController.setStatusLabel("File selected. Press Digitize button!");
					setImage();
				} else {
					rootLayoutController.getDigitizeButton().setDisable(true);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});

		// Button to digitize image to text
		rootLayoutController.getDigitizeButton().setOnMouseClicked(event -> {
			try {
				// Set labels for digitized Text
				rootLayoutController.getTextLabel().setText("Digitized Text: ");
				rootLayoutController.setStatusLabel("Text has been digitized. Press Decrypt button! (after modifying text)");

				// Get digitized text and store it
				digitizedText = getDigitizedText();
				rootLayoutController.getTextArea().setEditable(true);
				rootLayoutController.getTextArea().setMouseTransparent(false);
				rootLayoutController.setText(digitizedText);

				// Disable digitize button and enable decrypt button
				rootLayoutController.getDigitizeButton().setDisable(true);
				rootLayoutController.getDecryptButton().setDisable(false);
				writeOuputToTextFile("-----Digitized Text------ \n" + digitizedText);
			} catch (TesseractException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		// Button to decrypt digitized text
		rootLayoutController.getDecryptButton().setOnMouseClicked(event -> {

			// User may modify digitized text (to correct errors)
			String modifiedText = rootLayoutController.getTextArea().getText();
			rootLayoutController.getDecryptButton().setDisable(true);

			try {
				writeOuputToTextFile("-----Modified Text----- \n " + modifiedText);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Decrypter method
			// decryptedText = Decrypter(modifiedText);

			// writeOuputToTextFile("-----Decrypted Text----- \n " + decryptedText);
			rootLayoutController.getTextLabel().setText("Decrypted Text: ");
			rootLayoutController
					.setStatusLabel("Text has been decrypted. Ouput has been saved as " + selectedFile.getParentFile()
							+ "/" + FilenameUtils.removeExtension(selectedFile.getName()) + ".txt");

		});

		// Rests instance. Sets all values to null
		rootLayoutController.getReset().setOnAction(event -> {
			rootLayoutController.setStatusLabel("Reset selected. All values have been reset.");
			rootLayoutController.getImageView().setImage(null);
			rootLayoutController.getDigitizeButton().setDisable(true);
			rootLayoutController.getDecryptButton().setDisable(true);
			selectedFile = null;
			digitizedText = null;
			decryptedText = null;
			rootLayoutController.setText("");
		});
	}

	// Choose image file
	private boolean chooseFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select File");

		selectedFile = chooser.showOpenDialog(this.getPrimaryStage());
		if (selectedFile == null) {
			rootLayoutController.setStatusLabel("File selection canceled.");
			return false;
		} else {
			return true;
		}
	}

	private Window getPrimaryStage() {
		return this.primaryStage;
	}

	// Set image
	public void setImage() throws IOException {
		rootLayoutController.setImage(selectedFile);
	}

	// Calls Tesseract instance and returns string of digitized text
	public String getDigitizedText() throws TesseractException {
		ITesseract instance = new Tesseract();
		instance.setDatapath("/usr/local/Cellar/tesseract/3.05.02/share/tessdata/"); // need to set this to local datapath (can't get it working on module path)
		return instance.doOCR(selectedFile);
	}

	// Method to write digitized and decrypted output to text file in same directory
	// as source file (image)
	public void writeOuputToTextFile(String text) throws IOException {
		String fileName = selectedFile.getParentFile() + "/" + FilenameUtils.removeExtension(selectedFile.getName())
				+ ".txt";
		File file = new File(fileName);
		PrintWriter out = null;
		if (file.exists() && !file.isDirectory()) {
			out = new PrintWriter(new FileOutputStream(new File(fileName), true));
		} else {
			out = new PrintWriter(fileName);
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String string = timestamp.toString() + ": \n" + text;
		out.append(string);
		out.close();
	}

}
