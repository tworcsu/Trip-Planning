package edu.csu2017sp314.DTR02.view.gui;

import java.util.TreeMap;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FilterPane extends VBox{
	private ChoiceBox<String> type, continent, country, region;
	private TextField name, municipality, iata;
	private Button search, reset;
	private GUI gui;
	private ResultsPane output;
	
	public FilterPane(GUI gui, ResultsPane output) {
		this.output = output;
		this.gui = gui;
		initializeElements();
		HBox countryRow = new HBox(new Label("Country: "), country);
		HBox regionRow = new HBox (new Label("Region: "), region);
		HBox typeRow = new HBox(new Label("Type: "), type);
		getChildren().addAll(continent,countryRow,regionRow,typeRow,name,municipality,iata,search,reset);
	}
	
	private void initializeElements() {
		type = GUIElements.makeDropdown(
				"","Large_Airport","Medium_Airport","Small_Airport","Heliport","Seaplane_Base","Baloon Port","Closed");
		continent = GUIElements.makeDropdown(
				"","Africa", "Antarctica","Asia","Europe","North America","Oceania","South America");
		country = GUIElements.makeDropdown();
		populateCountry();
		region = GUIElements.makeDropdown();
		populateRegion();
		name = GUIElements.makePromptBox("Airport name:");
		municipality = GUIElements.makePromptBox("Municipality:");
		iata = GUIElements.makePromptBox("Airport ID (IATA code):");
		makeSearchButton();
		makeResetButton();
		this.continent.setOnAction((ActionEvent ae) -> {
			populateCountry();
		});
		this.country.setOnAction((ActionEvent ae) -> {
			populateRegion();
		});
		reset.fire();
	}
	
	private void populateCountry() {
		country.getItems().clear();
		country.getItems().addAll(gui.getPresenter().getCountries(continent.getValue()));
		if (country.getItems().size() > 0) {
			country.setValue(country.getItems().get(0));
		}
	}
	
	private void populateRegion() {
		region.getItems().clear();
		region.getItems().addAll(gui.getPresenter().getRegions(country.getValue()));
		if (region.getItems().size() > 0) {
			region.setValue(region.getItems().get(0));
		}
	}
	
	private void makeResetButton() {
		reset = new Button("Reset");
		reset.setOnAction((ActionEvent ae) -> {
			type.setValue("");
			continent.setValue("");
			country.setValue("");
			region.setValue("");
			name.setText("");
			municipality.setText("");
			iata.setText("");
		});
	}
	
	private void makeSearchButton() {
		search = new Button("Search!");
		search.setOnAction((ActionEvent ae)->{
			TreeMap<String,String> fields = new TreeMap<String,String>();
			fields.put("continent", continent.getValue());
			fields.put("country", country.getValue());
			fields.put("region", region.getValue());
			fields.put("name", name.getText() );
			fields.put("municipality",  municipality.getText());
			fields.put("code", iata.getText() );
			fields.put("type", type.getValue());
			output.populate(gui.getPresenter().queryFromFilters(fields));
		});
	}
}
