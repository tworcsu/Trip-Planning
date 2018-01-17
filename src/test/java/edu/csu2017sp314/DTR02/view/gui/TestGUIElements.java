package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class TestGUIElements extends JFXTest {

	/**
	 * Verifies that the prompt text of the generated TextField is unchanged
	 */
	@Test
	public void testPromptBox() {
		String testStr = "This is a test!";
		TextField prompt = GUIElements.makePromptBox(testStr);
		assertEquals(testStr,prompt.getPromptText());
	}

	/**
	 * Checks that the generated fileChooser has the expected title and
	 * extension filters.
	 */
	@Test
	public void testFileChooser() {
		String title = "title";
		String[] filetypes = {"test", "test1", "test2", "test3"};
		FileChooser chooser = GUIElements.makeFileChooser(title, filetypes);
		assertEquals(title,chooser.getTitle());
		for (ExtensionFilter filter : chooser.getExtensionFilters()) {
			assertTrue(filter.getExtensions().contains("*.*") 
					|| filter.getExtensions().contains("*.test")
					|| filter.getExtensions().contains("*.test1")
					|| filter.getExtensions().contains("*.test2")
					|| filter.getExtensions().contains("*.test3"));
		}
	}
	
	/**
	 * Because firing the button shows a Stage, we can only verify existence and text.
	 * Existence testing is implied by testing the button text.
	 */
	@Test
	public void testFileButton() {
		String title = "Button text!";
		FileChooser chooser = GUIElements.makeFileChooser("", "");
		TextField box = GUIElements.makePromptBox("");
		Button fb = GUIElements.makeFileButton(title, chooser, box);
		assertEquals(title,fb.getText());
	}
	
	/**
	 * Tests both "select all" and "deselect all" functionality, as well as text.
	 */
	@Test
	public void testSelectorButton() {
		ArrayList<SelectableRow> list = new ArrayList<SelectableRow>();
		for (int i = 1; i <= 10; i++) {
			list.add(new SelectableRow(i+"::Country::Region::Municipality"));
		}
		for (SelectableRow row : list) {
			row.getBox().setSelected(false);
		}
		Button selectAll = GUIElements.makeSelectorButton(true, list);
		assertEquals("Select all",selectAll.getText());
		selectAll.fire();
		for (SelectableRow row : list) {
			assertTrue(row.isSelected());
		}
		Button deselectAll = GUIElements.makeSelectorButton(false, list);
		assertEquals("Deselect all",deselectAll.getText());
		deselectAll.fire();
		for (SelectableRow row : list) {
			assertFalse(row.isSelected());
		}
	}

	/**
	 * Verifies that the generated dropdown automatically selects the first
	 * parameter, and that it contains the required parameters.
	 */
	@Test
	public void testDropdown() {
		String[] parameters = {"1", "2", "3", "4", "5"};
		ChoiceBox<String> dropdown = GUIElements.makeDropdown(parameters);
		assertEquals("1",dropdown.getValue());
		for (String param : parameters) {
			assertTrue(dropdown.getItems().contains(param));
		}
	}

	/**
	 * Verifies that the generated box is not selected, and does not allow
	 * indeterminate values.
	 */
	@Test
	public void testDeterminateCheckBox() {
		CheckBox box = GUIElements.makeDeterminateCheckBox();
		assertFalse(box.indeterminateProperty().get());
		assertFalse(box.isSelected());
	}
}