package edu.csu2017sp314.DTR02.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestModel {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try{
			List<String> lines = Arrays.asList("﻿ID,Name,City,Latitude,Longitude,Altitude",
					"bottomleft,The Southwest Corner of Colorado,test1,37,109.05°W,6566",
					"bottomright,The Southeast Corner of Colorado,test2,37,-102.05,4950",
					"topleft,The Northwest Corner of Colorado,test3,41,109°03.0'0\"W,1",
					"topright,The Northeast Corner of Colorado,test4,41,102°03'W,4949");
			Files.write(Paths.get("test-model-testCSV.csv"), lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Files.delete(Paths.get("test-model-testCSV.csv"));
	}
	//TODO: REWRITE TESTS AROUND DATABASE
//	@Test
//	public void testGetTripLongs(){
//		Model model = new Model("test-model-testCSV.csv");
//		model.getTrip();
//		double[] tripLongs = model.getTripLongs();
//		for (int i = 0; i < model.getTrip().size(); i++){
//			assertEquals(tripLongs[i],model.getTrip().get(i).getStart().getLong(),0.0001);
//		}
//		assertEquals(tripLongs[tripLongs.length - 1], tripLongs[0], 0.0001);
//	}
	
//	@Test
//	public void testGetTripLats(){
//		Model model = new Model("test-model-testCSV.csv");
//		model.getTrip();
//		double[] tripLats = model.getTripLats();
//		for (int i = 0; i < model.getTrip().size(); i++){
//			assertEquals(tripLats[i],model.getTrip().get(i).getStart().getLat(),0.0001);
//		}
//		assertEquals(tripLats[tripLats.length - 1], tripLats[0], 0.0001);
//	}
//	
//	@Test
//	public void testGetTripNames(){
//		Model model = new Model("test-model-testCSV.csv");
//		model.getTrip();
//		String[] tripNames = model.getTripNames();
//		for (int i = 0; i < model.getTrip().size(); i++){
//			assertEquals(tripNames[i],model.getTrip().get(i).getStart().getName());
//		}
//		assertEquals(tripNames[tripNames.length - 1], tripNames[0]);
//	}
//	
//	@Test
//	public void testGetTripIDs(){
//		Model model = new Model("test-model-testCSV.csv");
//		model.getTrip();
//		String[] tripIDs = model.getTripIDs();
//		for (int i = 0; i < model.getTrip().size(); i++){
//			assertEquals(tripIDs[i],model.getTrip().get(i).getStart().getID());
//		}
//		assertEquals(tripIDs[tripIDs.length - 1], tripIDs[0]);
//	}
//	
//	@Test
//	public void testTripDistance() {
//		Model model = new Model("test-model-testCSV.csv");
//		//leg distances should be 621.5 + 587.3 + 444.8 + 444.8 = 2098.4 kilometers.
//		//that converts to 1303.88531 miles, which ought to round to 1304.
//		//however, the program doesn't convert after summing! So, we actually need:
//		//621.5 + 587.3 + 444.8 + 444.8
//		//386.1822 + 364.9313 + 276.38591 + 276.38591
//		//386 + 365 + 276 + 276
//		//1303 miles. I might look into ways to minimize that error.
//		long totalDistance = 0;
//		for (Leg leg : model.getTrip()){
//			totalDistance += leg.getLength();
//		}
//		assertEquals(1303,totalDistance);
//		
//		//we'll need to recalculate this when we want to choose the best of all NN trips
//		//because "the borders of the state" only have so many ways to be laid out.
//	}
//	
//	@Test
//	public void testGetTrip() {
//		Model model = new Model("test-model-testCSV.csv");
//		assertEquals(model.getTrip().get(0).getStart(),model.getTrip().get(model.getTrip().size() - 1).getEnd());
//		assertEquals(model.getTrip().size(),model.getLocations().size());
//		for (int i = 0; i < model.getTrip().size(); i++){
//			Leg current = model.getTrip().get(i);
//			Leg next = model.getTrip().get((i + 1) % model.getTrip().size());
//			assertEquals(current.getEnd(),next.getStart());
//		}
//	}
//	
//	@Test
//	public void testGetLocations() {
//		Model model = new Model("test-model-testCSV.csv");
//		assertNotNull(model.getLocations());
//	}
//	
//	@Test
//	public void testParseLatLong() {
//		Model model = new Model("test-model-testCSV.csv");
//		double dd = -102.060167;
//		double ddm = -102.06;
//		double dms = -102.06;
//		String ddString = "-102.060167";
//		String ddmString = "102° 03.6' W";
//		String dmsString = "102° 3' 36\" W";
//		assertEquals(dd,model.parseLatLong(ddString),0.0001);
//		assertEquals(ddm,model.parseLatLong(ddmString),0.0001);
//		assertEquals(dms,model.parseLatLong(dmsString),0.0001);
//	}
//
//	@Test
//	public void testModelCtor() {
//		Model model = new Model("test-model-testCSV.csv");
//		assertNotNull(model);
//	}

}
