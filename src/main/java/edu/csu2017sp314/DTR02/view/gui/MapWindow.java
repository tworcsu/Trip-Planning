package edu.csu2017sp314.DTR02.view.gui;

import java.io.File;
import java.net.MalformedURLException;

import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MapWindow {
	private String filename;
	private Stage stage;
	private Scene scene;
	private WebView display;
	
	public MapWindow(String filename) {
		// need a scene
		// need a stage
		stage = new Stage();
		display = new WebView();
		setFilename(filename);
		stage.show();
		scene = new Scene(display, 1280, 651.42102);
		stage.setScene(scene);
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
		stage.setTitle("World Map: " + this.filename);
		String fileURI;
		try {
			fileURI = new File(this.filename).toURI().toURL().toExternalForm();
		} catch (MalformedURLException e) {
			return;
		}
		display.getEngine().load(fileURI);
	}
}