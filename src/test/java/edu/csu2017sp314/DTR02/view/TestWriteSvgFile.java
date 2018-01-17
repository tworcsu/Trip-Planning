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
import java.util.Map;
import java.util.TreeMap;

import org.junit.AfterClass;
import org.junit.Test;

public class TestWriteSvgFile {

	public ArrayList<Segment> segClass() {
		Point p1 = new Point(100, 100, "name1", "id1");
		Point p2 = new Point(500, 110, "name2", "id2");
		Point p3 = new Point(300, 500, "name3", "id3");
		Point p4 = new Point(600, 700, "name4", "id4");
		Point p5 = new Point(800, 900, "name5", "id5");
		Point p6 = new Point(100, 110, "name6", "id6");
		Point p7 = new Point(120, 130, "name7", "id7");
		Point p8 = new Point(140, 150, "name8", "id8");

		Segment seg1 = new Segment(p1, p2, 1, "Miles");
		Segment seg2 = new Segment(p2, p3, 2, "Miles");
		Segment seg3 = new Segment(p3, p4, 3, "Miles");
		Segment seg4 = new Segment(p4, p5, 4, "Miles");
		Segment seg5 = new Segment(p5, p6, 5, "Miles");
		Segment seg6 = new Segment(p6, p7, 6, "Miles");
		Segment seg7 = new Segment(p7, p8, 7, "Miles");

		ArrayList<Segment> segarray = new ArrayList<Segment>();
		segarray.add(seg1);
		segarray.add(seg2);
		segarray.add(seg3);
		segarray.add(seg4);
		segarray.add(seg5);
		segarray.add(seg6);
		segarray.add(seg7);

		return segarray;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Files.delete(Paths.get("testFilename.svg"));
	}

	@Test
	public void WriteSvgTest() {
		// tripSegments,filename,mFlag,nFlag,iFlag
		Map<Character,Boolean> flags = new TreeMap<>();
		Map<String,String> files = new TreeMap<>();
		String filename = "testFilename";
		files.put("noext", filename);
		flags.put('d', false);
		flags.put('i', false);
		WriteSvgFile wsvg = new WriteSvgFile(segClass(), flags, files);
		assertNotNull(wsvg);
		assertTrue((new File("testFilename.svg")).exists());
	}

	@Test
	public void WriteSvgHeader() {
		// tripSegments,filename,mFlag,nFlag,iFlag
		Map<Character,Boolean> flags = new TreeMap<>();
		Map<String,String> files = new TreeMap<>();
		String filename = "testFilename";
		files.put("noext", filename);
		flags.put('d', false);
		flags.put('i', false);
		WriteSvgFile wsvg = new WriteSvgFile(segClass(), flags, files);
		assertNotNull(wsvg);
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;

		try {
			fileReader = new FileReader(filename + ".svg");
			bufferedReader = new BufferedReader(fileReader);

			String currentLine;

			// get header
			currentLine = bufferedReader.readLine();

			assertEquals(currentLine, "<?xml version=\"1.0\"?>");
			currentLine = bufferedReader.readLine();
			assertEquals(currentLine,
					"<svg width=\"1280\" height=\"652\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">");
			bufferedReader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
