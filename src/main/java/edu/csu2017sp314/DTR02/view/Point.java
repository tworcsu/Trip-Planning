package edu.csu2017sp314.DTR02.view;

import java.util.Map;

//
public class Point {
	private double x;
	private double y;
	private double latitude;
	private double longitude;
	private String name;
	private String id;

	private Map<String,String> elements;

	public Point(double x, double y, double latitude, double longitude, Map<String,String> elements) {
		this.x = x;
		this.y = y;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elements = elements;
		if(this.elements.containsKey("name") && this.elements.get("name") != null)
		{
		    this.name = this.elements.get("name");
		}
		if(this.elements.containsKey("id") && this.elements.get("id") != null)
		{
		    this.id = this.elements.get("id");
		}

	}


	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public Map<String,String> getElements() {
		return elements;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public Point(double d, double e, String name, String id){
		this.x = d;
		this.y = e;
		this.name = name;
		this.id = id;
	}
}
