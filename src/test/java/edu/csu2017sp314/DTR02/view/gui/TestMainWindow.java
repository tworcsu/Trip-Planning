package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import javafx.application.Platform;

public class TestMainWindow extends JFXTest {
	@Test
	public void testCtor() {
		Platform.runLater(() -> {
			GUI gui = new GUI();
			OptionsPane options = new OptionsPane();
			SelectionPane selection = new SelectionPane(gui);
			ResultsPane results = new ResultsPane(selection);
			FilterPane filter = new FilterPane(gui, results);
			MainWindow panels = new MainWindow(filter, results, selection, options);
			panels.setGui(gui);
			assertNotNull(panels);
			assertNotNull(panels.getRoot());
			assertNotNull(panels.getRoot().getChildrenUnmodifiable());
		});
	}
}