package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestFilterPane extends JFXTest {

	/**
	 * The only public method in FilterPane is a constructor which takes a GUI
	 * object and a ResultsPane object.
	 */
	@Test
	public void testCtor() {
		GUI gui = new GUI();
		SelectionPane spane = new SelectionPane(gui);
		ResultsPane rpane = new ResultsPane(spane);
		FilterPane fpane = new FilterPane(gui, rpane);
		assertNotNull(fpane);
	}
}