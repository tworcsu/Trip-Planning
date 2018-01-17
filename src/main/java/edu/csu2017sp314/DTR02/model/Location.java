package edu.csu2017sp314.DTR02.model;

import java.util.Map;

import org.gavaghan.geodesy.GlobalCoordinates;

public class Location {
	private GlobalCoordinates coords;
	//latitude,longitude,name,elevation_ft,municipality,iso_region,iso_country,continent,wikipedia_link
	private Map<String,String> attributes;

	public Location(double latitude, double longitude, Map<String,String> attributes) {
		coords = new GlobalCoordinates(latitude,longitude);
		this.attributes = attributes;
	}
	
	/**
	 * Assume that Locations are uniquely identified by ID.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Location)) {
			return false;
		}
		Location otherLocation = (Location) other;
		return this.attributes.get("id").equals(otherLocation.attributes.get("id"));
	}

	public GlobalCoordinates getCoords() {
		return coords;
	}
	
	public Map<String,String> getAttributes() {
		return this.attributes;
	}
	
	public String getid(){
	   return attributes.get("id").isEmpty() ? "null" : attributes.get("id");
	    
	}
	public String getMunicipality(){
	    return attributes.get("municipality").isEmpty() ? "null" : attributes.get("municipality");
	}
	public String getRegion(){
	    return attributes.get("region").isEmpty() ? "null" : attributes.get("region");
	}
	
	public String getCountry(){
	    return attributes.get("country").isEmpty() ? "null" : attributes.get("country");
	}
	
	public String getName(){
		return attributes.get("name");
	}

	public double getLat(){
		return coords.getLatitude();
	}

	public double getLong(){
		return coords.getLongitude();
	}

	public String toString(){
		return getid() + " " + getName() + " " + getLat() + " " + getLong();
	}
}
