package edu.csu2017sp314.DTR02.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteKmlFile {

    public WriteKmlFile(ArrayList<Segment> segments, Map<String, String> filenames) {
    	this(segments, filenames.get("noext"), filenames.get("selectionName"));
    }

    public WriteKmlFile(ArrayList<Segment> tripSegments, String filename, String selectionName) {
	try {

	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	    
	   

	    // root
	    Document doc = docBuilder.newDocument();
	    Element kmldeclaration = doc.createElement("kml");
	    doc.appendChild(kmldeclaration);
	    
	    Attr attr = doc.createAttribute("xmlns");
	    attr.setValue("http://www.opengis.net/kml/2.2");
	    kmldeclaration.setAttributeNode(attr);
	    
	    
	    Element document = doc.createElement("Document");
	    kmldeclaration.appendChild(document);

	    // placemark element
	    Element placemark = doc.createElement("Placemark");
	    document.appendChild(placemark);

	    // name
	    Element name = doc.createElement("name");
	    name.appendChild(doc.createTextNode(" " + selectionName));
	    placemark.appendChild(name);

	    // description (BLANK)
	    // Style (Blank)

	    // POINT OR COORD
	    Element linestring = doc.createElement("LineString");
	    placemark.appendChild(linestring);

	    Element extrude = doc.createElement("extrude");
	    extrude.appendChild(doc.createTextNode("1"));
	    linestring.appendChild(extrude);

	    Element tessellate = doc.createElement("tessellate");
	    tessellate.appendChild(doc.createTextNode("1"));
	    linestring.appendChild(tessellate);

	    Element altmode = doc.createElement("altitudeMode");
	    altmode.appendChild(doc.createTextNode("clampToGround"));
	    linestring.appendChild(altmode);

	    Element coord = doc.createElement("coordinates");
	    String allCoords = getAllCoords(tripSegments);
	    coord.appendChild(doc.createTextNode(allCoords));
	    linestring.appendChild(coord);

	    int itenNum = 1;
	    for (Segment seg : tripSegments) {
		Element pointmark = doc.createElement("Placemark");
		document.appendChild(pointmark);

		Element pointName = doc.createElement("name");
		pointName.appendChild(doc.createTextNode(seg.getFromPoint().getName())); // name
		pointmark.appendChild(pointName);

		Element description = doc.createElement("description");
		description.appendChild(doc.createTextNode(getDesc(seg) + "  Itinerary # " + itenNum));
		pointmark.appendChild(description);

		Element point = doc.createElement("Point");
		pointmark.appendChild(point);

		Element pointcoord = doc.createElement("coordinates");
		pointcoord.appendChild(doc.createTextNode(getCoord(seg)));
		point.appendChild(pointcoord);

		itenNum++;

	    }

	    // write the content into xml file
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    // make pretty
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

	    DOMSource source = new DOMSource(doc);
	    StreamResult result = new StreamResult(new File(filename + ".kml"));

	    // Output to console for testing
	    // StreamResult result = new StreamResult(System.out);

	    transformer.transform(source, result);

	} catch (ParserConfigurationException pce) {
	    pce.printStackTrace();
	} catch (TransformerException tfe) {
	    tfe.printStackTrace();
	}
    }

    private String getCoord(Segment seg) {
	StringBuilder sb = new StringBuilder();
	// google maps format (long,lat,altitude) 0 alt means IDC
	sb.append(seg.getFromPoint().getLongitude()).append(",").append(seg.getFromPoint().getLatitude()).append(",0");
	return sb.toString();

    }
    
    private String getDesc(Segment seg) {
    	String id = seg.getFromPoint().getId();
    	String url = seg.getFromPoint().getElements().get("airportURL");
    	if (!(url.contains("wikipedia"))) {
    		url = "about:blank";
    	}
    	return "<a href=\"" + url + "\" target=\"_blank\">" + id + "</a>";
    }

    private String getAllCoords(ArrayList<Segment> tripSegments) {
	StringBuilder sb = new StringBuilder();
	boolean isFirst = true;
	String firstPoint = "";
	for (Segment seg : tripSegments) {

	    // format is (lat,long,alt(?)) alt is const 100 for the moment
	    sb.append(seg.getFromPoint().getLongitude()).append(",").append(seg.getFromPoint().getLatitude())
		    .append(",100 ");
	    if (isFirst) {
		firstPoint = sb.toString();
		isFirst = false;
	    }

	}
	sb.append(firstPoint);
	return sb.toString();
    }

    /* MY TEMPLATE
     * <?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://www.opengis.net/kml/2.2">
  <Document>

    <Placemark>
      <name>Absolute Extruded</name>
      <description>Transparent green wall with yellow outlines</description>
      <styleUrl>#yellowLineGreenPoly</styleUrl>
      <LineString>
        <extrude>1</extrude>
        <tessellate>1</tessellate>
        <altitudeMode>clampToGround</altitudeMode>
        <coordinates> 
	4.637,114.382,100
        14.755,-86.004,100
	18.448,-67.062,100
	80,-80,100
	30,50,100
        </coordinates>
      </LineString>
    </Placemark>
   <Placemark>
        <name> FIRST</name>
        <description> Stop TIME: 19:25:20 Source: network</description>
        <Point>
            <coordinates>4.637,114.382,0</coordinates>
        </Point>
    </Placemark>
   <Placemark>
        <name> SECOND   </name>
        <description> Stop TIME: 19:25:20 Source: network</description>
        <Point>
            <coordinates>40,80 </coordinates>
        </Point>
    </Placemark>
   <Placemark>
        <name> THIRD</name>
        <description> Stop TIME: 19:25:20 Source: network</description>
        <Point>
            <coordinates>50,60,100</coordinates>
        </Point>
    </Placemark>
   <Placemark>
        <name> FOURTH</name>
        <description> Stop TIME: 19:25:20 Source: network</description>
        <Point>
            <coordinates>80,-80,100 </coordinates>
        </Point>
    </Placemark>
  </Document>
</kml>

*/
}
