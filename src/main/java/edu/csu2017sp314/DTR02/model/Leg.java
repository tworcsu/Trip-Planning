package edu.csu2017sp314.DTR02.model;


public class Leg {
	private Location start;
	private Location end;
	private long miles;
	private long kilometers;
	
	public Leg(Location start, Location end){
		this.start = start;
		this.end = end;
		//compute great circle distance from start to end (using spherical law of cosines)
		 double distance = Math.acos(
				   Math.sin(Math.toRadians(start.getLat()))
				 * Math.sin(Math.toRadians(end.getLat()))
				 + Math.cos(Math.toRadians(start.getLat()))
				 * Math.cos(Math.toRadians(end.getLat())) 
				 * Math.cos(Math.toRadians((end.getLong()-start.getLong()))));
		 kilometers = Math.round(distance * 6371); //multiply by Earth's radius in kilometers
		 miles = Math.round(distance * 3959); //multiply by Earth's radius in miles
	}
	
	public long getLength() {
		return miles;
	}
	
	public long getMiles() {
		return miles;
	}
	
	public long getKilometers() {
		return kilometers;
	}
	
	public Location getStart() {
		return start;
	}
	
	public Location getEnd() {
		return end;
	}

}