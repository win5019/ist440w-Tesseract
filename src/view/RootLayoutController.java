package view;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.MainApp;

public class RootLayoutController implements Initializable {
	MainApp mainApp;
	@FXML private Button digitizeButton;
	@FXML private Button decryptButton;
	@FXML private TextArea textArea;
	@FXML private ImageView imageView;
	@FXML private MenuBar menuBar;
	@FXML private MenuItem close;
	@FXML private MenuItem selectFile;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void setImage(Image img) {
        getImageView().setImage(img);
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

	public MenuItem getSelectFile() {
		return selectFile;
	}

	public void setSelectFile(MenuItem selectFile) {
		this.selectFile = selectFile;
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
}
