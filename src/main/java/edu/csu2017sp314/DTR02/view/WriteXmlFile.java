package edu.csu2017sp314.DTR02.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

//https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/

/*
 *   <start>
    <!-- information from the airport table -->
    <id>KDEN</id>
    <name>Denver International Airport</name>
    <latitude>39.861698150635</latitude>
    <longitude>-104.672996521</longitude>
    <elevation>5431</elevation>
    <municipality>Denver</municipality>
    <!-- names from the other tables, not the codes -->
    <region>Colorado</region>
    <country>United Statues</country>
    <continent>North America</continent>
    <!-- wikipedia links from the tables -->
    <airportURL>http://en.wikipedia.org/wiki/Denver_International_Airport</airportURL>
    <regionURL>http://en.wikipedia.org/wiki/Colorado</regionURL>
    <countryURL>http://en.wikipedia.org/wiki/United_States</countryURL>
  </start>
 */

public class WriteXmlFile {

	public WriteXmlFile(ArrayList<Segment> segments, Map<String, String> filenames) {
		this(segments,filenames.get("noext"));
	}

	public WriteXmlFile(ArrayList<Segment> segments, String filename)
	{

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuild = docFactory.newDocumentBuilder();
			Document doc = docBuild.newDocument();

			//ROOT ELEMENT (trip)
			Element rootElement = doc.createElement("trip");
			doc.appendChild(rootElement);

			int sequenceCounter = 1; //keeps track of leg (start at 1)

			for(Segment seg: segments)
			{
				String mileage = Integer.toString(seg.getDistance());
				String unitLabel = seg.getUnits();
				String seqCount = Integer.toString(sequenceCounter); //sequence counter to string

				//leg element
				Element leg = doc.createElement("leg");
				rootElement.appendChild(leg);

				//sequence element
				Element sequence = doc.createElement("sequence");
				sequence.appendChild(doc.createTextNode(seqCount));
				leg.appendChild(sequence);

				//start element
				Element start = doc.createElement("start");
				leg.appendChild(start);

				//attributes

				Element id = doc.createElement("id");
				id.appendChild(doc.createTextNode(seg.getFromPoint().getElements().get("id")));
				start.appendChild(id);

				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(seg.getFromPoint().getElements().get("name")));
				start.appendChild(name);

				Element latitude = doc.createElement("latitude");
				latitude.appendChild(doc.createTextNode(Double.toString(seg.getFromPoint().getLatitude())));
				start.appendChild(latitude);

				Element longitude = doc.createElement("longitude");
				longitude.appendChild(doc.createTextNode(Double.toString(seg.getFromPoint().getLongitude())));
				start.appendChild(longitude);

				Element elevation = doc.createElement("elevation");
				elevation.appendChild(doc.createTextNode(seg.getFromPoint().getElements().get("elevation")));
				start.appendChild(elevation);

				Element municipality = doc.createElement("municipality");
				municipality.appendChild(doc.createTextNode(seg.getFromPoint().getElements().get("municipality")));
				start.appendChild(municipality);

				Element region = doc.createElement("region");
				region.appendChild(doc.createTextNode(seg.getFromPoint().getElements().get("region")));
				start.appendChild(region);

				Element country = doc.createElement("country");
				country.appendChild(doc.createTextNode(seg.getFromPoint().getElements().get("country")));
				start.appendChild(country);

				Element continent = doc.createElement("continent");
				continent.appendChild(doc.createTextNode(seg.getFromPoint().getElements().get("continent")));
				start.appendChild(continent);

				Element airportURL = doc.createElement("airportURL");
				airportURL.appendChild(doc.createTextNode(seg.getFromPoint().getElements().get("airportURL")));
				start.appendChild(airportURL);

				Element regionURL = doc.createElement("regionURL");
				regionURL.appendChild(doc.createTextNode(seg.getFromPoint().getElements().get("regionURL")));
				start.appendChild(regionURL);

				Element countryURL = doc.createElement("countryURL");
				countryURL.appendChild(doc.createTextNode(seg.getFromPoint().getElements().get("countryURL")));
				start.appendChild(countryURL);

				//finish element
				Element finish = doc.createElement("finish");
				leg.appendChild(finish);

				//attributes
				id = doc.createElement("id");
				id.appendChild(doc.createTextNode(seg.getToPoint().getElements().get("id")));
				finish.appendChild(id);

				name = doc.createElement("name");
				name.appendChild(doc.createTextNode(seg.getToPoint().getElements().get("name")));
				finish.appendChild(name);

				latitude = doc.createElement("latitude");
				latitude.appendChild(doc.createTextNode(Double.toString(seg.getToPoint().getLatitude())));
				finish.appendChild(latitude);

				longitude = doc.createElement("longitude");
				longitude.appendChild(doc.createTextNode(Double.toString(seg.getToPoint().getLongitude())));
				finish.appendChild(longitude);

				elevation = doc.createElement("elevation");
				elevation.appendChild(doc.createTextNode(seg.getToPoint().getElements().get("elevation")));
				finish.appendChild(elevation);

				municipality = doc.createElement("municipality");
				municipality.appendChild(doc.createTextNode(seg.getToPoint().getElements().get("municipality")));
				finish.appendChild(municipality);

				region = doc.createElement("region");
				region.appendChild(doc.createTextNode(seg.getToPoint().getElements().get("region")));
				finish.appendChild(region);

				country = doc.createElement("country");
				country.appendChild(doc.createTextNode(seg.getToPoint().getElements().get("country")));
				finish.appendChild(country);

				continent = doc.createElement("continent");
				continent.appendChild(doc.createTextNode(seg.getToPoint().getElements().get("continent")));
				finish.appendChild(continent);

				airportURL = doc.createElement("airportURL");
				airportURL.appendChild(doc.createTextNode(seg.getToPoint().getElements().get("airportURL")));
				finish.appendChild(airportURL);

				regionURL = doc.createElement("regionURL");
				regionURL.appendChild(doc.createTextNode(seg.getToPoint().getElements().get("regionURL")));
				finish.appendChild(regionURL);

				countryURL = doc.createElement("countryURL");
				countryURL.appendChild(doc.createTextNode(seg.getToPoint().getElements().get("countryURL")));
				finish.appendChild(countryURL);

				//distance element
				Element distance = doc.createElement("distance");
				distance.appendChild(doc.createTextNode(mileage));
				leg.appendChild(distance);
				Element units = doc.createElement("units");
				units.appendChild(doc.createTextNode(unitLabel));
				leg.appendChild(units);

				sequenceCounter++;

			}

			//Write to xml
			TransformerFactory transformFactory = TransformerFactory.newInstance();
			Transformer transformer = transformFactory.newTransformer();

			//Make it Pretty
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filename +".xml"));

			//TEST
			//StreamResult result = new StreamResult(System.out);


			transformer.transform(source, result);


		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		catch (TransformerException e) {
			e.printStackTrace();
		}



	}


}
