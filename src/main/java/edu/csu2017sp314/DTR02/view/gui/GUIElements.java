package edu.csu2017sp314.DTR02.view.gui;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public abstract class GUIElements {
	/**
	 * Create a TextField object with pre-configured prompt text.
	 * @param prompt the text with which to prompt the user for input.
	 * @return a TextField with the promptText property set.
	 */
	public static TextField makePromptBox(String prompt) {
		TextField box = new TextField();
		box.setPromptText(prompt);
		return box;
	}

	/**
	 * Create a FileChooser object with pre-configured
	 * extension filters. Mostly just exists to save lines in start().
	 * 
	 * @param title
	 *            The desired title of the FileChooser window.
	 * @param filetypes
	 *            a list of Strings containing the file extensions to filter for.
	 * @return A FileChooser with title and ExtensionFilters set.
	 */
	public static FileChooser makeFileChooser(String title, String... filetypes) {
		FileChooser chooser = new FileChooser();
		for (String filetype : filetypes) {
			chooser.getExtensionFilters().add(
					new FileChooser.ExtensionFilter(filetype.toUpperCase() + " files", "*." + filetype.toLowerCase()));
		}
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.*"));
		chooser.setTitle(title);
		return chooser;
	}

	/**
	 * Create a Button object which opens a FileChooser when
	 * clicked and which then edits a TextField to contain a path to the chosen
	 * file.
	 * 
	 * @param title
	 *            the text to display on the button.
	 * @param chooser
	 *            the FileChooser to open when activated.
	 * @param box
	 *            the TextField to update when a file is chosen.
	 * @return a Button which opens a FileChooser and modifies a TextField.
	 */
	public static Button makeFileButton(String title, FileChooser chooser, TextField box) {
		Button button = new Button(title);
		button.setOnAction((ActionEvent e) -> {
			File file = chooser.showOpenDialog(null);
			if (file != null) {
				box.setText(file.getPath());
			}
		});
		return button;
	}

	/**
	 * Generate "select all" or "deselect all" buttons
	 * 
	 * @param selectAll
	 *            whether or not the generated button should select everything
	 *            in rows
	 * @param rows
	 *            a list of SelectableRows from which to check or uncheck checkboxes
	 * @return a button which selects or deselects all elements in boxes
	 */
	public static Button makeSelectorButton(boolean selectAll, List<SelectableRow> rows) {
		Button button = new Button((selectAll ? "S" : "Des") + "elect all");
		button.setOnAction((ActionEvent e) -> {
			for (SelectableRow row : rows) {
				row.getBox().setSelected(selectAll);
			}
		});
		return button;
	}

	/**
	 * Generate a dropdown with String choices, defaulting to the first.
	 * @param choices a list of options to display when the menu is expanded.
	 * @return a dropdown menu displaying choices and allowing the selection of one.
	 */
	public static ChoiceBox<String> makeDropdown(String... choices) {
		ChoiceBox<String> dropdown = new ChoiceBox<String>();
		dropdown.getItems().addAll(choices);
		if (choices.length > 0) {
			dropdown.setValue(choices[0]);
		}
		return dropdown;
	}

	/**
	 * Generate a checkbox which defaults to unselected and does not allow an indeterminate state.
	 * @return such a checkbox.
	 */
	public static CheckBox makeDeterminateCheckBox() {
		CheckBox box = new CheckBox();
		box.setAllowIndeterminate(false);
		box.setSelected(false);
		return box;
	}
}
