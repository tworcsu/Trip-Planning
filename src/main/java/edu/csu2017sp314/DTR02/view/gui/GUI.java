package edu.csu2017sp314.DTR02.view.gui;

import java.util.Map;
import java.util.TreeMap;

import edu.csu2017sp314.DTR02.presenter.Presenter;
import edu.csu2017sp314.DTR02.view.View;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
	private View view;
	private Presenter presenter;
	private Map<Character, Boolean> flags;
	private Map<String, String> filenames;
	
	/**
	 * Enables the launching of the GUI Application from outside the class.
	 */
	static public void launchApplication() {
		launch();
	}
	
	/**
	 * A no-arg constructor is required in order to extend Application.
	 */
	public GUI() {
		initialize();
	}
	
	/**
	 * Attempts to display an XML itinerary at filename in a new window.
	 * @param filename a path to an XML itinerary.
	 */
	public void displayItinerary(String filename) {
		if (filename.toLowerCase().contains(".xml")) {
			new ItineraryWindow(filename);
		}
	}

	/**
	 * Attempts to display an SVG map in a new window.
	 * @param filename a path to an SVG map.
	 */
	public void displayMap(String filename) {
		if (filename.toLowerCase().contains(".svg")) {
			new MapWindow(filename);
		}
	}
	
	/**
	 * Attempts to display an interactive Google map with the tour overlaid.
	 * 
	 * @param filename
	 *            a path to a KML tour.
	 */
	public void displayGoogleMap(String filename) {
		if (filename.toLowerCase().contains(".kml")) {
			new GoogleWindow(filename);
		}
	}

	/**
	 * Does the heavy lifting for a constructor. Also permits initialization inside start().
	 */
	private void initialize() {
		initFlags();
		initFilenames();
		presenter = new Presenter(flags, filenames);
		view = presenter.getView();
		view.setPresenter(presenter);
		view.setGui(this);
	}
	
	/**
	 * Initializes flags, putting false everywhere but 'g': if we're here, we're launching a GUI.
	 */
	private void initFlags() {
		flags = new TreeMap<Character, Boolean>();
		flags.put('i', false);
		flags.put('n', false);
		flags.put('g', true);
		flags.put('2', false);
		flags.put('3', false);
		flags.put('k', false);
		flags.put('m', false);
		flags.put('d', false);
	}
	
	/**
	 * Initializes filenames to blank Strings except for csv, which needs a '.' to avoid null pointer exceptions.
	 */
	private void initFilenames() {
		filenames = new TreeMap<String, String>();
		filenames.put("csv", ".");
		filenames.put("svg", "");
		filenames.put("xml", "");
		filenames.put("noext", "");
		filenames.put("selectionName", "");
	}
	
	public Map<Character,Boolean> getFlags(){
		return flags;
	}
	
	public Map<String,String> getFilenames(){
		return filenames;
	}
	
	View getView() {
		return view;
	}
	
	Presenter getPresenter() {
		return presenter;
	}
	
	void startTrip() {
		presenter.tripInitiate();
	}
	
	private void login(Stage mainStage) {
		Stage stage = new Stage();
		VBox rows = new VBox();
		rows.setAlignment(Pos.TOP_CENTER);
		TextField uname = new TextField();
		SafePasswordField pword = new SafePasswordField();
		uname.setPromptText("Username");
		pword.setPromptText("Password");
		Button loginButton = makeLoginButton(uname,pword,stage,mainStage);
		uname.setOnAction((ActionEvent e)-> {
			loginButton.fire();
		});
		pword.setOnAction((ActionEvent e)-> {
			loginButton.fire();
		});
		rows.getChildren().addAll(uname, pword, loginButton);
		Scene scene = new Scene(rows,300,85);
		stage.setScene(scene);
		stage.show();
	}
	
	private Button makeLoginButton(TextField username, SafePasswordField password, Stage login, Stage mainStage) {
		Button button = new Button("Login");
		button.setOnAction((ActionEvent)->{
			try {
				if(!presenter.login(username.getText(), password.getPassword())) {
					makeLoginErrorWindow();
				} else {
					login.hide();
					mainStage.show();
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				//this is the wrong way to handle these exceptions.
				return;
			}
		});
		return button;
	}
	
	private Button makeOkButton(Stage stage) {
		Button button = new Button("OK");
		button.setOnAction((ActionEvent)->{
			stage.hide();
		});
		return button;
	}
	
	private void makeLoginErrorWindow() {
		Stage stage = new Stage();
		VBox rows = new VBox(new Label("Unable to login with the given username and password.\n"),makeOkButton(stage));
		rows.setAlignment(Pos.TOP_CENTER);
		Scene scene = new Scene(rows,400,85);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * The main entry point for the JavaFX Application.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		initialize();
		OptionsPane options = new OptionsPane();
		SelectionPane selection = new SelectionPane(this);
		ResultsPane results = new ResultsPane(selection);
		FilterPane filter = new FilterPane(this, results);
		MainWindow panels = new MainWindow(filter, results, selection, options);
		panels.setGui(this);
		stage.setTitle("TripCo planner");
		stage.setScene(panels);
		login(stage);
	}
}