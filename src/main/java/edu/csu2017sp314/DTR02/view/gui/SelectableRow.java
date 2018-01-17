package edu.csu2017sp314.DTR02.view.gui;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

class SelectableRow extends HBox {
	private Label id, country, region, municipality;
	private CheckBox isSelected;
	
	@Override
	public String toString() {
		return id.getText() + country.getText() + region.getText() + municipality.getText();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof SelectableRow)) {
			return false;
		}
		SelectableRow otherRow = (SelectableRow) other;
		boolean idsMatch = this.id.getText().equals(otherRow.id.getText());
		boolean countriesMatch = this.country.getText().equals(otherRow.country.getText());
		boolean regionsMatch = this.region.getText().equals(otherRow.region.getText());
		boolean municipalitiesMatch = this.municipality.getText().equals(otherRow.municipality.getText());
		return idsMatch && countriesMatch && regionsMatch && municipalitiesMatch;
	}

	/**
	 * constructor for the row
	 * 
	 * @param airportInfo
	 *            in the format
	 *            ID::Country(name)::Region(Code)::Municipality
	 */
	public SelectableRow(String airportInfo) {
		String[] fields = airportInfo.split("::");
		if (fields.length != 4) {
			return;
		}
		id = new Label(fields[0] + " ");
		country = new Label(fields[1] + " ");
		region = new Label(fields[2] + " ");
		municipality = new Label(fields[3]);
		isSelected = GUIElements.makeDeterminateCheckBox();
		isSelected.setSelected(true);
		getChildren().addAll(isSelected, id, country, region, municipality);
	}
	
	/**
	 * Basically a copy constructor
	 * @param other another SelectableRow from which to copy the row's elements
	 */
	public SelectableRow(SelectableRow other) {
		this.id = new Label(other.id.getText());
		this.country = new Label(other.country.getText());
		this.region = new Label(other.region.getText());
		this.municipality = new Label(other.municipality.getText());
		isSelected = GUIElements.makeDeterminateCheckBox();
		isSelected.setSelected(other.isSelected());
		getChildren().addAll(isSelected, id, country, region, municipality);
	}

	/**
	 * A method to identify whether or not the checkbox in the row is
	 * selected.
	 * 
	 * @return the value of the property isSelected of the checkbox in this
	 *         row.
	 */
	public boolean isSelected() {
		return isSelected.isSelected();
	}

	/**
	 * A method to directly obtain a reference to the checkbox in this row.
	 * 
	 * @return the row's checkbox.
	 */
	public CheckBox getBox() {
		return isSelected;
	}

	/**
	 * A method to obtain the id of the airport represented by this row.
	 * 
	 * @return a String representation of the airport's IATA code.
	 */
	public String getIdText() {
		return id.getText().trim();
	}

	/**
	 * A method to obtain the country of the airport represented by this
	 * row.
	 * 
	 * @return a String representation of the airport's country.
	 */
	public String getCountry() {
		return country.getText().trim();
	}

	/**
	 * A method to obtain the region of the airport represented by this row.
	 * 
	 * @return a String representation of the airport's region.
	 */
	public String getRegion() {
		return region.getText().trim();
	}

	/**
	 * A method to obtain the municipality of the airport represented by
	 * this row.
	 * 
	 * @return a String representation of the airport's municipality.
	 */
	public String getMunicipality() {
		return municipality.getText().trim();
	}
}