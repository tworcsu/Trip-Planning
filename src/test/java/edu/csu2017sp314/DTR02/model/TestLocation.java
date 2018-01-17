package edu.csu2017sp314.DTR02.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestLocation {
	private static Map<String,String> getAttributes() {
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
		return attributes1;
	}
	@Test
	public void testCtor() {
		//latitude,longitude,name,elevation_ft,municipality,iso_region,iso_country,continent,wikipedia_link
		Location loc = new Location(40.552752, -105.096024, getAttributes());
		assertNotNull(loc);
	}
	
	@Test
	public void testGetName() {
		Location loc = new Location(40.552752, -105.096024, getAttributes());
		assertEquals("name",loc.getName());
	}
	
	@Test
	public void testGetid() {
		Location loc = new Location(40.552752, -105.096024, getAttributes());
		assertEquals("start",loc.getid());
	}
	
	@Test
	public void testGetLat() {
		Location loc = new Location(40.552752, -105.096024, getAttributes());
		assertEquals(40.552752, loc.getLat(), 0.00001);
	}
	
	@Test
	public void testGetLong() {
		Location loc = new Location(40.552752, -105.096024, getAttributes());
		assertEquals(-105.096024,loc.getLong(),0.00001);
	}

}
