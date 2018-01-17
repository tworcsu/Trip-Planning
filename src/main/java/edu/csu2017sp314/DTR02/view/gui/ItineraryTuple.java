package edu.csu2017sp314.DTR02.view.gui;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * A simple container class for storing parsed information about a single
 * airport in one object.
 */
public class ItineraryTuple extends VBox{
	String iata = ""; 		 // <id>KDEN</id>
	String name = ""; 		 // <name>Denver International Airport</name>
	double latitude; 		 // <latitude>39.861698150635</latitude>
	double longitude; 		 // <longitude>-104.672996521</longitude>
	//Integer (as an object) in order to facilitate NULL for unlisted values
	String elevation;	     // <elevation>5431</elevation>
	String municipality = "";// <municipality>Denver</municipality>
	String region = ""; 	 // <region>Colorado</region>
	String country = ""; 	 // <country>United Statues</country>
	String continent = ""; 	 // <continent>North America</continent>
	URL airportWiki; 		 // <airportURL>http://en.wikipedia.org/wiki/Denver_International_Airport</airportURL>
	URL regionWiki; 		 // <regionURL>http://en.wikipedia.org/wiki/Colorado</regionURL>
	URL countryWiki; 		 // <countryURL>http://en.wikipedia.org/wiki/United_States</countryURL>

	private void populateFields(NodeList elements) {
		for (int i = 0; i < elements.getLength(); i++) {
			Node current = elements.item(i);
			switch (current.getNodeName()) {
			case "id":
				iata = current.getTextContent();
				break;
			case "name":
				name = current.getTextContent();
				break;
			case "latitude":
				latitude = Double.parseDouble(current.getTextContent());
				break;
			case "longitude":
				longitude = Double.parseDouble(current.getTextContent());
				break;
			case "elevation":
				elevation = current.getTextContent();
				break;
			case "municipality":
				municipality = current.getTextContent();
				break;
			case "region":
				region = current.getTextContent();
				break;
			case "country":
				country = current.getTextContent();
				break;
			case "continent":
				continent = current.getTextContent();
				break;
			case "airportURL":
				try {
					airportWiki = new URI(current.getTextContent()).toURL();
				} catch (IllegalArgumentException | MalformedURLException | DOMException | URISyntaxException e) {
					airportWiki = null;
				}
				break;
			case "regionURL":
				try {
					regionWiki = new URI(current.getTextContent()).toURL();
				} catch (IllegalArgumentException | MalformedURLException | DOMException | URISyntaxException e) {
					regionWiki = null;
				}
				break;
			case "countryURL":
				try {
					countryWiki = new URI(current.getTextContent()).toURL();
				} catch (IllegalArgumentException | MalformedURLException | DOMException | URISyntaxException e) {
					countryWiki = null;
				}
				break;
			}
		}
	}

	private void addChildren() {
		/*	KDEN
		 *	Denver International Airport
		 *	39.86N, 104.67W, 5431 ft
		 *	Denver, Colorado, United States,
		 *	North America
		 */
		getChildren().add(new Label(iata));
		getChildren().add(makeLink(name,airportWiki));
		getChildren().add(latLonElev(latitude,longitude,elevation));
		getChildren().add(countryRow(
				makeLink((region.equals("")?"":region+", "),regionWiki),
				makeLink(country,countryWiki)));
		getChildren().add(new Label(continent));
	}

	private HBox countryRow(Hyperlink region, Hyperlink country) {
		HBox row = new HBox();
		row.setAlignment(Pos.BASELINE_CENTER);
		row.getChildren().addAll(new Label(municipality.equals("")?"":municipality+", "),region,country);
		return row;
	}

	private Label latLonElev(double latitude, double longitude, String elevation) {
		latitude = Math.round(latitude * 100) / 100.0;
		longitude = Math.round(longitude * 100) / 100.0;
		String contents = "" + Math.abs(latitude) + (latitude<0?"S":"N") + ", "
							 + Math.abs(longitude) + (longitude <0?"W":"E");
		if (elevation != null) {
			contents += ", " + elevation + " ft";
		}
		return new Label(contents);
	}

	private static Hyperlink makeLink(String label, URL dest) {
		Hyperlink link = new Hyperlink(label);
		link.setOnAction((ActionEvent ae)->{
			if (dest != null) {
				showPage(dest);
			}
		});
		return link;
	}

	private static void showPage(URL page) {
		Stage stage = new Stage();
		WebView browser = new WebView();
		browser.getEngine().load(page.toString());
		Scene scene = new Scene(browser,800,600);
		stage.setScene(scene);
		stage.show();
	}

	public ItineraryTuple(Node loc) {
		elevation = null;
		populateFields(loc.getChildNodes());
		addChildren();
	}
}
