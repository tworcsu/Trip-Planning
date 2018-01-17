package edu.csu2017sp314.DTR02.view;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Test;

public class TestView {

	//creates a segment arraylist
	public ArrayList<Segment> segClass() {
		HashMap<String,String> attributes = new HashMap<>();
		attributes.put("id","KDEN");
		attributes.put("name","Denver International Airport");
		attributes.put("elevation","5431");
		attributes.put("municipality","Denver");
		attributes.put("region","Colorado");
		attributes.put("country","United Statues");
		attributes.put("continent","North America");
		attributes.put("airportURL","http://en.wikipedia.org/wiki/Denver_International_Airport");
		attributes.put("regionURL","http://en.wikipedia.org/wiki/Colorado");
		attributes.put("countryURL","http://en.wikipedia.org/wiki/United_States");

		Point p1 = new Point(100, 100, 39.861698150635, -104.672996521, attributes);
		Point p2 = new Point(100, 100, 39.861698150635, -104.672996521, attributes);

		Segment seg1 = new Segment(p1, p2, 1, "Miles");

		ArrayList<Segment> segarray = new ArrayList<Segment>();
		segarray.add(seg1);

		return segarray;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//Files.delete(Paths.get("test-filename.xml"));
		//Files.delete(Paths.get("test-filename.svg"));
	}

	@Test
	public void testView() {
		// file,m,n,i
		View view = new View("test-filename", true, true, true);
		View view1 = new View("test-filename", false, true, true);
		View view2 = new View("test-filename", true, false, true);
		View view3 = new View("test-filename", true, true, false);
		View view4 = new View("test-filename", false, false, true);
		View view5 = new View("test-filename", true, false, false);
		View view6 = new View("test-filename", false, true, false);
		View view7 = new View("test-filename", false, false, false);
		assertNotNull(view);
		assertNotNull(view1);
		assertNotNull(view2);
		assertNotNull(view3);
		assertNotNull(view4);
		assertNotNull(view5);
		assertNotNull(view6);
		assertNotNull(view7);

	}

	// only checks if files ([filename].xml and [filename].svg) were created in current directory
	@Test
	public void testViewAddSegments() {
		// file,m,n,i
		String filename = "test-filename";
		View view = new View(filename, true, true, true);
		view.writeFiles(segClass());
		File f = new File(filename+".xml");
		File f2 = new File(filename+".svg");
		//assertTrue(f.exists());
		//assertTrue(f2.exists());
	}
}
