package edu.csu2017sp314.DTR02.view.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class SelectionPane extends VBox {
	private ScrollPane scrollBox;
	private VBox innerPane;
	private List<SelectableRow> rows;
	private Button selectAll, selectNone, load, save;
	private TextField name, path;
	private FileChooser saveLocator;
	private GUI gui;

	public SelectionPane(GUI gui) {
		this.gui = gui;
		innerPane = new VBox();
		scrollBox = new ScrollPane();
		scrollBox.setMinHeight(171);
		scrollBox.setMinWidth(300);
		scrollBox.setContent(innerPane);
		rows = new ArrayList<SelectableRow>();
		name = new TextField("Selection Name?");
		path = new TextField("Save Location");
		saveLocator = GUIElements.makeFileChooser("Selection XML", "xml");
		saveLocator.setInitialFileName("selection.xml");
		makeButtons();
		getChildren().addAll(new Label("Selected locations:"), scrollBox,
				new HBox(selectAll, selectNone),
				name, path,
				new HBox(load, save));
	}
	
	public List<String> getSelected(){
		ArrayList<String> selected = new ArrayList<String>();
		for (SelectableRow row : rows) {
			selected.add(row.getIdText());
		}
		return selected;
	}
	
	public String getSelectionName() {
		return name.getText();
	}

	private void makeButtons() {
		selectAll = new Button("Select All");
		
		selectAll.setOnAction((ActionEvent ae) -> {
			for (SelectableRow row : rows) {
				row.getBox().setSelected(true);
			}
		});
		selectNone = new Button("Clear Selection");
		selectNone.setOnAction((ActionEvent ae) -> {
			for (SelectableRow row : rows) {
				row.getBox().setSelected(false);
			}
		});
		load = new Button("Load Selection");
		load.setOnAction((ActionEvent ae) -> {
		    	File userDir = new File("./");
		    	saveLocator.setInitialDirectory(userDir);
			File file = saveLocator.showOpenDialog(null);
			if (file != null) {
				path.setText(file.getPath());
				gui.getPresenter().setSelection(file.getPath());	
				    List<String> selected = gui.getPresenter().getSelectedList();
        			    for(String airport: selected) {
        			        rows.add(new SelectableRow(airport));
        			    }
        			    name.setText(gui.getPresenter().getSelectionName());
				innerPane.getChildren().clear();
				innerPane.getChildren().addAll(rows);
			}
		});

		save = new Button("Save selection");
		save.setOnAction((ActionEvent ae) -> {
			List<SelectableRow> savedRows = new ArrayList<SelectableRow>();
			for (SelectableRow row : rows) {
				if (row.isSelected()) {
					savedRows.add(row);
				}
			}
			String path = this.path.getText().equals("Save Location")?"current.xml":this.path.getText();
			writeSelectionXML(savedRows, path, name.getText());
			this.gui.getPresenter().setSelection(path);
		});
	}

	public void addRow(SelectableRow inbound) {
		innerPane.getChildren().clear();
		if (!rows.contains(inbound)) {
			rows.add(new SelectableRow(inbound));
		}
		innerPane.getChildren().addAll(rows);
	}

	/**
	 * Helper method to generate an XML representation of the selected subset of
	 * destinations
	 * 
	 * @param filename
	 *            the path to the file this method is to write
	 * @param selectionName
	 *            an optionally-specified name for this selection
	 * @param IDs
	 *            the selected set of destinations as represented by a List of
	 *            ID strings.
	 */
	static private void writeSelectionXML(List<SelectableRow> rows, String path, String titleName) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuild = docFactory.newDocumentBuilder();
			Document doc = docBuild.newDocument();
			// ROOT ELEMENT (trip)
			Element rootElement = doc.createElement("selection");
			doc.appendChild(rootElement);
			if (!titleName.equals("")) {
				Element title = doc.createElement("title");
				rootElement.appendChild(title);
				title.appendChild(doc.createTextNode(titleName));
			}
			Element fnameTag = doc.createElement("filename");
			rootElement.appendChild(fnameTag);
			fnameTag.appendChild(doc.createTextNode(path));
			Element destinations = doc.createElement("destinations");
			rootElement.appendChild(destinations);
			for (SelectableRow row : rows) {
				Element idTag = doc.createElement("id");
				idTag.appendChild(doc.createTextNode(row.getIdText().trim()));
				destinations.appendChild(idTag);
			}
			// Write to xml
			TransformerFactory transformFactory = TransformerFactory.newInstance();
			Transformer transformer = transformFactory.newTransformer();
			// Make it Pretty
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path));
			transformer.transform(source, result);
		} catch (Exception ex) {
		}
	}
}