package view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.MainApp;

public class RootLayoutController implements Initializable {
	MainApp mainApp;
	@FXML
	private Button digitizeButton;
	@FXML
	private Button decryptButton;
	@FXML
	private TextArea textArea;
	@FXML
	private ImageView imageView;
	@FXML
	private MenuBar menuBar;
	@FXML
	private MenuItem close;
	@FXML
	private Button selectFile;
	@FXML
	private Button insertText;
	@FXML
	private MenuItem reset;
	@FXML
	private Label textLabel;
	@FXML
	private Label statusLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Initially disable buttons
		digitizeButton.setDisable(true);
		decryptButton.setDisable(true);
		
		// Preserve image ratios for imageview
		imageView.setPreserveRatio(true);
		
		// Set focus to false
		selectFile.setFocusTraversable(false);
		insertText.setFocusTraversable(false);

		// Disable focus and editable on text area
		textArea.setEditable(false);
		textArea.setMouseTransparent(true);
		textArea.setFocusTraversable(false);
	}

	// Set status label text
	public void setStatusLabel(String text) {
		statusLabel.setText(text);
	}

	// Set image to image view (centered and scaled)
	public void setImage(File file) {
		Image img = new Image(file.toURI().toString());
		if (img != null) {
			double w = 0;
			double h = 0;

			double ratioX = imageView.getFitWidth() / img.getWidth();
			double ratioY = imageView.getFitHeight() / img.getHeight();

			double reducCoeff = 0;
			if (ratioX >= ratioY) {
				reducCoeff = ratioY;
			} else {
				reducCoeff = ratioX;
			}

			w = img.getWidth() * reducCoeff;
			h = img.getHeight() * reducCoeff;

			imageView.setX((imageView.getFitWidth() - w) / 2);
			imageView.setY((imageView.getFitHeight() - h) / 2);

		}
		imageView.setImage(img);
	}

	// Set text area's text
	public void setText(String text) {
		textArea.setText(text);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public MenuItem getClose() {
		return close;
	}

	public void setClose(MenuItem close) {
		this.close = close;
	}

	public Button getSelectFile() {
		return selectFile;
	}

	public void setSelectFile(Button button) {
		this.selectFile = button;
	}

	public TextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(TextArea textArea) {
		this.textArea = textArea;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public Button getDigitizeButton() {
		return digitizeButton;
	}

	public void setDigitizeButton(Button digitizeButton) {
		this.digitizeButton = digitizeButton;
	}

	public Button getDecryptButton() {
		return decryptButton;
	}

	public void setDecryptButton(Button decryptButton) {
		this.decryptButton = decryptButton;
	}

	public MenuBar getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(MenuBar menuBar) {
		this.menuBar = menuBar;
	}

	public Label getTextLabel() {
		return textLabel;
	}

	public void setTextLabel(Label textLabel) {
		this.textLabel = textLabel;
	}

	public MainApp getMainApp() {
		return mainApp;
	}

	public MenuItem getReset() {
		return reset;
	}

	public void setReset(MenuItem reset) {
		this.reset = reset;
	}

	public Button getInsertText() {
		return insertText;
	}

	public void setInsertText(Button insertText) {
		this.insertText = insertText;
	}
}
