package edu.csu2017sp314.DTR02.view;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.AfterClass;
import org.junit.Test;

public class TestWriteKmlFile {
    public ArrayList<Segment> segClass() {
	HashMap<String, String> attributes = new HashMap<>();
	attributes.put("id", "KDEN");
	attributes.put("name", "Denver International Airport");
	attributes.put("elevation", "5431");
	attributes.put("municipality", "Denver");
	attributes.put("region", "Colorado");
	attributes.put("country", "United Statues");
	attributes.put("continent", "North America");
	attributes.put("airportURL", "http://en.wikipedia.org/wiki/Denver_International_Airport");
	attributes.put("regionURL", "http://en.wikipedia.org/wiki/Colorado");
	attributes.put("countryURL", "http://en.wikipedia.org/wiki/United_States");

	Point p1 = new Point(100, 100, 39.861698150635, -104.672996521, attributes);
	Point p2 = new Point(100, 100, 39.861698150635, -104.672996521, attributes);

	Segment seg1 = new Segment(p1, p2, 1, "Miles");

	ArrayList<Segment> segarray = new ArrayList<Segment>();
	segarray.add(seg1);

	return segarray;
    }

    @Test
    public void WriteKmlTest() {
	WriteKmlFile writekml = new WriteKmlFile(segClass(), "testfilename", "testselectionname");
	assertNotNull(writekml);
	assertTrue(new File("testfilename.kml").exists());
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
	Files.delete(Paths.get("testfilename.kml"));
    }
    
    @Test
    public void WriteKmlTestTrueMap() {
	Map<String,String> mymap = new TreeMap<String,String>();
	
	mymap.put("noext", "test");
	WriteKmlFile writekml = new WriteKmlFile(segClass(), mymap);
	assertNotNull(writekml);
	assertTrue(new File("test.kml").exists());
    }

}
