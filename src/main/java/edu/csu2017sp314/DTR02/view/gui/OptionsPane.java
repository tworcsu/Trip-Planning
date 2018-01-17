package edu.csu2017sp314.DTR02.view.gui;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OptionsPane extends VBox{
	private TextField tripTitle;
	private CheckBox displayDistances, displayIDs;
	private ChoiceBox<String> units, optLevel;
	
	public OptionsPane() {
		tripTitle = new TextField("Trip Title");
		displayDistances = GUIElements.makeDeterminateCheckBox();
		displayIDs = GUIElements.makeDeterminateCheckBox();
		units = GUIElements.makeDropdown("Miles","Kilometers");
		optLevel = GUIElements.makeDropdown("Nearest Neighbor","2-opt","3-opt");
		getChildren().addAll(tripTitle,
				new HBox(displayDistances,new Label("Display distances")),
				new HBox(displayIDs,new Label("Display IDs")),
				new Label("Distance units: "),units,
				new Label("Optimization level: "),optLevel);
	}
	
	public boolean useMiles() {
		return units.getValue().equals("Miles");
	}
	
	public String getOptLevel() {
		return optLevel.getValue();
	}
	
	public String getTitle() {
		return tripTitle.getText();
	}
	
	public boolean showDistances() {
		return displayDistances.isSelected();
	}
	
	public boolean showIDs() {
		return displayIDs.isSelected();
	}
}