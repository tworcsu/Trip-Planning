package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TestResultsPane extends JFXTest {
	@Test
	public void testCtor() {
		SelectionPane selection = new SelectionPane(new GUI());
		ResultsPane results = new ResultsPane(selection);
		ArrayList<String> airports = new ArrayList<String>();
		for (int i = 1; i <= 10; i++) {
			airports.add(i + "::Country::Region::Municipality");
		}
		results.populate(airports);
		for (Node node : results.getChildren()) {
			if (node instanceof Label) {
				assertTrue(((Label) node).getText().contains("matching records (100 max)."));
			} else if (node instanceof ScrollPane) {
				assertTrue(((ScrollPane)node).getContent() instanceof VBox);
			} else {
				assertTrue(node instanceof HBox);
			}
		}
	}
}
