package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Optional;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
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
	int type = 0; // -1 is image, 0 is text, 1 is .txt file

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
		primaryStage.initStyle(StageStyle.UNDECORATED);
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
		rootLayoutController.getSelectFile().setOnMouseClicked(event -> {
			resetValues();
			try {
				chooseFile();
				if (type == -1) { // is image
					rootLayoutController.getDigitizeButton().setDisable(false);
					rootLayoutController.getDecryptButton().setDisable(true);
					rootLayoutController.setStatusLabel("Image selected. Press Digitize button!");
					setImage();
				} else { // is .txt file
					String text = null;
					text = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()))); // read txt file and show in text area
					rootLayoutController.getTextArea().setEditable(true);
					rootLayoutController.getTextArea().setMouseTransparent(false);
					rootLayoutController.setText(text);
					rootLayoutController.setStatusLabel("Text file selected. Press Decrypt button!");
					rootLayoutController.getDigitizeButton().setDisable(true);
					rootLayoutController.getDecryptButton().setDisable(false);
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
				rootLayoutController
						.setStatusLabel("Text has been digitized. Press Decrypt button! (after modifying text)");

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

		// Opens dialog to input text
		rootLayoutController.getInsertText().setOnMouseClicked(event -> {
			resetValues();
			TextInputDialog dialog = new TextInputDialog();
			dialog.initStyle(StageStyle.UTILITY);
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.setTitle("Text Input Dialog");
			dialog.setHeaderText("Insert Text");
			dialog.setContentText("Text to be decrypted:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				rootLayoutController.setText(result.get());
				rootLayoutController.getDecryptButton().setDisable(false);
				rootLayoutController.getDigitizeButton().setDisable(true);
			} else {
				rootLayoutController.setStatusLabel("No text was inserted");
				rootLayoutController.setText("");

			}
		});
		// Button to decrypt digitized text
		rootLayoutController.getDecryptButton().setOnMouseClicked(event -> {

			// User may modify digitized text (to correct errors)
			String modifiedText = rootLayoutController.getTextArea().getText();
			rootLayoutController.getDecryptButton().setDisable(true);

			try {
				if(type == -1 || type == 1) {
					writeOuputToTextFile("-----Modified Text----- \n " + modifiedText);
				} else {
					writeOuputToTextFile("-----Input Text------ \n" + modifiedText);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Decrypter method
			// decryptedText = Decrypter(modifiedText);

			// writeOuputToTextFile("-----Decrypted Text----- \n " + decryptedText);
			rootLayoutController.getTextLabel().setText("Decrypted Text: ");

			if (selectedFile != null) {
				rootLayoutController.setStatusLabel(
						"Text has been decrypted. Ouput has been saved as " + selectedFile.getParentFile() + "/"
								+ FilenameUtils.removeExtension(selectedFile.getName()) + ".txt");
			} else {
				rootLayoutController.setStatusLabel("Text has been decrypted and saved to desktop");
			}

		});

		// Change language to Korean
		rootLayoutController.getKoreanOption().setOnAction(event -> {
			rootLayoutController.getMenu().setText(rootLayoutController.getKoreanOption().getText());
			rootLayoutController.setStatusLabel("Option changed to Korean");
		});
		
		// Change language to English
		rootLayoutController.getEnglishOption().setOnAction(event -> {
			rootLayoutController.getMenu().setText(rootLayoutController.getEnglishOption().getText());
			rootLayoutController.setStatusLabel("Option changed to English");
		});
		
		// Change language to Chinese
		rootLayoutController.getChineseOption().setOnAction(event -> {
			rootLayoutController.getMenu().setText(rootLayoutController.getChineseOption().getText());
			rootLayoutController.setStatusLabel("Option changed to Chinese");
		});
		
		// Rests instance. Sets all values to null
		rootLayoutController.getReset().setOnAction(event -> {
			rootLayoutController.setStatusLabel("Reset selected. All values have been reset.");
			resetValues();
		});
	}

	public void resetValues() {
		rootLayoutController.getImageView().setImage(null);
		rootLayoutController.getDigitizeButton().setDisable(true);
		rootLayoutController.getDecryptButton().setDisable(true);
		selectedFile = null;
		digitizedText = null;
		decryptedText = null;
		rootLayoutController.setText("");
	}

	// Choose image file
	private void chooseFile() {
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image or .txt files", "*.txt", "*.jpg", "*.jpeg", "*.png", "*.pdf");
		chooser.getExtensionFilters().add(extFilter);

		chooser.setTitle("Select File");
		selectedFile = chooser.showOpenDialog(this.getPrimaryStage());

		if (selectedFile == null) {
			rootLayoutController.setStatusLabel("File selection canceled.");
			rootLayoutController.getDigitizeButton().setDisable(true);
		} else {
			// determines extension of file
			String extension = "";
			int i = selectedFile.getName().lastIndexOf('.');
			if (i > 0) {
				extension = selectedFile.getName().substring(i + 1);
			}
			System.out.println(extension);
			if (extension.equals("txt")) {
				type = 1;
			} else {
				type = -1;
			}
			System.out.println(type);
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
		instance.setDatapath("/usr/local/Cellar/tesseract/3.05.02/share/tessdata/"); // need to set this to local
																						// datapath (can't get it
																						// working on module path)
		return instance.doOCR(selectedFile);
	}

	// Method to write digitized and decrypted output to text file in same directory
	// as source file (image)
	public void writeOuputToTextFile(String text) throws IOException {
		String fileName;
		if (selectedFile != null) {
			fileName = selectedFile.getParentFile() + "/" + FilenameUtils.removeExtension(selectedFile.getName())
					+ ".txt";
		} else {
			fileName = System.getProperty("user.home") + "/Desktop/" + rootLayoutController.getTextArea().getText()
					+ ".txt";
		}

		File file = new File(fileName);
		PrintWriter out = null;
		if (file.exists() && !file.isDirectory()) {
			out = new PrintWriter(new FileOutputStream(new File(fileName), true));
		} else {
			out = new PrintWriter(fileName);
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String string = "\n" + timestamp.toString() + ": \n" + text + "\n";
		out.append(string);
		out.close();
	}

}
