package edu.csu2017sp314.DTR02.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestLeg {
	
	private static Leg makeLeg() {
		Map<String,String> attributes1 = new HashMap<String,String>();
	    attributes1.put("id", "start");
	    attributes1.put("name", "name");
	    attributes1.put("elevation", "elevation");
	    attributes1.put("municipality", "municipality");
	    attributes1.put("region", "region");
	    attributes1.put("country", "country");
	    attributes1.put("continent", "continent");
	    attributes1.put("airportURL", "airportURL");
	    attributes1.put("regionURL", "regionURL");
	    attributes1.put("countryURL", "countryURL");
	    Map<String,String> attributes2 = new HashMap<String,String>(attributes1);
	    attributes2.put("id", "end");
		Location start = new Location(40.552752, -105.096024, attributes1);
		Location end = new Location(40.552789, -105.114988, attributes2);
		return new Leg(start,end);
	}
	
	@Test
	public void testLegCtor() {
		Leg leg = makeLeg();
		assertNotNull(leg);
	}
	
	@Test
	public void testLength() {
		Leg leg = makeLeg();
		assertEquals(leg.getLength(),1);
	}

	@Test
	public void testGetStart() {
		Leg leg = makeLeg();
		assertEquals("start",leg.getStart().getid());
	}
	
	@Test
	public void testGetEnd() {
		Leg leg = makeLeg();
		assertEquals("end",leg.getEnd().getid());
	}
}