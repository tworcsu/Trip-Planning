package edu.csu2017sp314.DTR02.view.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ItineraryWindow {
	private String filename;
	private Stage stage;
	private GridPane table;
	private List<ItineraryLeg> itinerary;

	public ItineraryWindow(String filename){
		this(filename,false);
	}
	
	/**
	 * Test constructor
	 * @param filename name of the XML itinerary file to display
	 * @param debug when debug is true, the ItineraryWindow doesn't open a new Stage.
	 */
	public ItineraryWindow(String filename, boolean debug) {
		itinerary = new ArrayList<ItineraryLeg>();
		if (!debug) {
			setFilename(filename);
		} else {
			this.filename = filename;
			parseFile(filename);
		}
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
		makeNewWindow();
	}
	
	public void parseFile(String xmlFile) {
		itinerary = new ArrayList<ItineraryLeg>();
		try {
			File fXmlFile = new File(xmlFile);
			if (!fXmlFile.exists()) {
				System.err.println("NO ITINERARY XML TO READ");
				// If no XML, exit the method
				return;
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nlist = doc.getElementsByTagName("leg");
			for (int i = 0; i < nlist.getLength(); i++) {
				itinerary.add(new ItineraryLeg(nlist.item(i)));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void makeNewWindow() {
		parseFile(filename);
		stage = new Stage();
		ScrollPane displayBox = new ScrollPane();
		table = new GridPane();
		table.setPadding(new Insets(10,10,10,10));
		table.setAlignment(Pos.CENTER);
		table.setHgap(20);
		table.setVgap(10);
		// first row (row 0) of the table is column labels
		int row = 0;
		table.add(new Label("Leg"), 0, row);
		table.add(new Label("From"), 1, row);
		table.add(new Label("To"), 2, row);
		table.add(new Label("Distance"), 3, row);
		for (ItineraryLeg line : itinerary) {
			row++;
			table.add(new Label(""+line.sequence), 0, row);
			table.add(line.start, 1, row);
			table.add(line.finish, 2, row);
			table.add(new Label(""+line.distance+" "+line.units), 3, row);
		}
		displayBox.setContent(table);
		Scene scene = new Scene(displayBox, 850, 400);
		stage.setTitle("Itinerary: " + filename);
		stage.setScene(scene);
		stage.show();
	}
}