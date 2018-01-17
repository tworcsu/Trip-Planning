package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class TestSelectionPane extends JFXTest {
	@Test
	public void testCtor() {
		GUI gui = new GUI();
		SelectionPane selection = new SelectionPane(gui);
		assertNotNull(selection);
		ArrayList<SelectableRow> rows = new ArrayList<SelectableRow>();
		ArrayList<String> ids = new ArrayList<String>();
		for (int i = 1; i <= 10; i++) {
			rows.add(new SelectableRow(i + "::Country::Region::Municipality"));
		}
		for (SelectableRow row : rows) {
			ids.add(row.getIdText());
			row.getBox().setSelected(true);
			selection.addRow(row);
		}
		for (String row : selection.getSelected()) {
			assertTrue(ids.contains(row));
		}
	}
}
