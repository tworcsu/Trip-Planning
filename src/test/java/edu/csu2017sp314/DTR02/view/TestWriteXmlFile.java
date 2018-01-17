package edu.csu2017sp314.DTR02.view;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Test;

public class TestWriteXmlFile {
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
		Files.delete(Paths.get("testfilename.xml"));
	}

	@Test
	public void WriteXmlTest() {
		WriteXmlFile writexml = new WriteXmlFile(segClass(), "testfilename");
		assertNotNull(writexml);
		assertTrue(new File("testfilename.xml").exists());
	}

	@Test
	public void WriteXmlHeader() {
		String filename = "testfilename";
		WriteXmlFile writexml = new WriteXmlFile(segClass(), filename);
		assertNotNull(writexml);
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;

		try {
			fileReader = new FileReader(filename + ".xml");
			bufferedReader = new BufferedReader(fileReader);

			String currentLine;

			// get header
			currentLine = bufferedReader.readLine();

			assertTrue(currentLine.matches("<\\?xml version=\"1.0\"( encoding=\"UTF-8\")?( standalone=\"no\")?\\?>"));
			bufferedReader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Test # of lines in relation to how man segments there are
	@Test
	public void WriteXmlLineCount() {
		String filename = "testfilename";
		ArrayList<Segment> segments = segClass();
		WriteXmlFile writexml = new WriteXmlFile(segClass(), filename);
		assertNotNull(writexml);
		int segCount = 0;
		int totalLines = 0;
		// count the segs
		for (Segment seg : segments) {
			if (seg != null)
				segCount++;
		}

		try {
			List<String> lines = Files.readAllLines(Paths.get(filename + ".xml"));
			totalLines = segCount;
			totalLines *= 33; // a segment has 4 attributes for each segment and
								// 2 leg blocks for each seg
			totalLines += 3; // + header + 2 trip blocks
			assertEquals(totalLines, lines.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
