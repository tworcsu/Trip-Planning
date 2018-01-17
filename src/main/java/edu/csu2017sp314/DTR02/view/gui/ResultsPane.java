package edu.csu2017sp314.DTR02.view.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ResultsPane extends VBox {
	private Label count;
	private ScrollPane listArea;
	private VBox innerPane;
	private List<SelectableRow> rows;
	private Button selectAll, clear, storeSelected;
	private SelectionPane selectionPane;

	public ResultsPane(SelectionPane selectionPane) {
		this.selectionPane = selectionPane;
		count = new Label("Displaying 0 matching records (100 max).");
		innerPane = new VBox();
		listArea = new ScrollPane();
		listArea.setMinHeight(250);
		listArea.setMinWidth(300);
		listArea.setContent(innerPane);
		rows = new ArrayList<SelectableRow>();
		makeButtons();
		getChildren().addAll(count, listArea, new HBox(selectAll, clear, storeSelected));
	}

	private void makeButtons() {
		selectAll = new Button("Select All");
		selectAll.setOnAction((ActionEvent e) -> {
			for (SelectableRow row : rows) {
				row.getBox().setSelected(true);
			}
		});

		clear = new Button("Clear Selection");
		clear.setOnAction((ActionEvent e) -> {
			for (SelectableRow row : rows) {
				row.getBox().setSelected(false);
			}
		});

		storeSelected = new Button("Store Selected");
		storeSelected.setOnAction((ActionEvent e) -> {
			for (SelectableRow row : rows) {
				if (row.isSelected()) {
					this.selectionPane.addRow(row);
				}
			}
		});
	}

	public void populate(List<String> searchResults) {
		innerPane.getChildren().clear();
		rows = new ArrayList<SelectableRow>();
		for (String airport : searchResults) {
			rows.add(new SelectableRow(airport));
		}
		innerPane.getChildren().addAll(rows);
		listArea.setContent(innerPane);
		count.setText("Displaying " + rows.size() + " matching records (100 max).");
	}
}
