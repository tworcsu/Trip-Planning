package edu.csu2017sp314.DTR02.view.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class MainWindow extends Scene {
	private GUI gui;
	private HBox contents;
	private FilterPane filter;
	private ResultsPane results;
	private SelectionPane selection;
	private OptionsPane options;

	public MainWindow(FilterPane filter, ResultsPane results, SelectionPane selection, OptionsPane options) {
		super(new HBox(filter, results, selection, options), 1200, 350);
		filter.setAlignment(Pos.CENTER_LEFT);
		options.setAlignment(Pos.CENTER_RIGHT);
		this.contents = (HBox) this.getRoot();
		contents.setPadding(new Insets(25, 25, 25, 25));
		contents.setSpacing(10);
		this.filter = filter;
		this.results = results;
		this.selection = selection;
		this.options = options;
		Button start = makeStart();
		options.getChildren().add(start);
		contents.setAlignment(Pos.CENTER_RIGHT);
	}

	private Button makeStart() {
		Button start = new Button("Plan a trip!");
		start.setPrefHeight(75);
		start.setOnAction((ActionEvent ae) -> {
			gui.getPresenter().setSelection(selection.getSelected());
			switch (options.getOptLevel()) {
			case "Nearest Neighbor":
				gui.getView().useNN();
				break;
			case "2-opt":
				gui.getView().use2opt();
				break;
			case "3-opt":
				gui.getView().use3opt();
			}
			gui.getPresenter().useMiles(options.useMiles());
			gui.getFlags().put('d', options.showDistances());
			gui.getFlags().put('i', options.showIDs());
			if (!gui.getFilenames().containsKey("selectionName")
					|| gui.getFilenames().get("selectionName").equals("")) {
				gui.getFilenames().put("selectionName",
						selection.getSelectionName().equals("Selection Name?")
								? "TripCo tour: " + gui.getFilenames().get("xml")
								: selection.getSelectionName());
			}
			gui.startTrip();
		});
		return start;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}
}