package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestItineraryLeg {
	
	/**
	 * Create test itinerary with one leg
	 */
	@BeforeClass
	public static void makeSampleXml() {
		List<String> lines = Arrays.asList(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
				"<!-- ",
				"This sample shows the information provided on each leg. ",
				"-->",
				"<trip>",
				"<!--",
				"sequence - the order of the legs",
				"start - information about the starting location for a leg",
				"finish - information about the finishing location for a leg",
				"distance - the distance (to the nearest whole unit) between the starting and finishing locations",
				"units - the distance units to display (miles or kilometers)",
				"-->",
				"<leg>",
				"  <sequence>1</sequence>",
				"  <start>",
				"    <!-- information from the airport table -->",
				"    <id>KDEN</id>",
				"    <name>Denver International Airport</name>",
				"    <latitude>39.861698150635</latitude>",
				"    <longitude>-104.672996521</longitude>",
				"    <elevation>5431</elevation>",
				"    <municipality>Denver</municipality>",
				"    <!-- names from the other tables, not the codes -->",
				"    <region>Colorado</region>",
				"    <country>United States</country>",
				"    <continent>North America</continent>",
				"    <!-- wikipedia links from the tables -->",
				"    <airportURL>http://en.wikipedia.org/wiki/Denver_International_Airport</airportURL> ",
				"    <regionURL>http://en.wikipedia.org/wiki/Colorado</regionURL>",
				"    <countryURL>http://en.wikipedia.org/wiki/United_States</countryURL>",
				"  </start>",
				"  <finish>",
				"    <!-- information from the airport table -->",
				"    <id>KDEN</id>",
				"    <name>Denver International Airport</name>",
				"    <latitude>39.861698150635</latitude>",
				"    <longitude>-104.672996521</longitude>",
				"    <elevation>5431</elevation>",
				"    <municipality>Denver</municipality>",
				"    <!-- names from the other tables, not the codes -->",
				"    <region>Colorado</region>",
				"    <country>United States</country>",
				"    <continent>North America</continent>",
				"    <!-- wikipedia links from the tables -->",
				"    <airportURL>http://en.wikipedia.org/wiki/Denver_International_Airport</airportURL> ",
				"    <regionURL>http://en.wikipedia.org/wiki/Colorado</regionURL>",
				"    <countryURL>http://en.wikipedia.org/wiki/United_States</countryURL>",
				"  </finish>",
				"  <distance>0</distance>",
				"  <units>miles</units>",
				"</leg>",
				"</trip>");
		try {
			Files.write(Paths.get("test-itinerary-leg.xml"), lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Delete the test itinerary file
	 */
	@AfterClass
	public static void tearDown() {
		try {
			Files.delete(Paths.get("test-itinerary-leg.xml"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Checks that a test leg contains values from the itinerary xml.
	 */
	@Test
	public void testCtor() {
		try {
			File xmlFile = new File("test-itinerary-leg.xml");
			if (!xmlFile.exists()) {
				System.err.println("NO ITINERARY XML TO READ");
				// If no XML, exit the method
				return;
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			//example of how to get text from something by tag name
			//String csv = doc.getElementsByTagName("filename").item(0).getTextContent();
			// In order to iterate through sequence tags
			NodeList nlist = doc.getElementsByTagName("leg");
			Node node = nlist.item(0);
			ItineraryLeg leg = new ItineraryLeg(node);
			assertNotNull(leg);
			assertEquals(1,leg.sequence);
			assertEquals(0,leg.distance);
			assertEquals("miles",leg.units);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}