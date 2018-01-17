package edu.csu2017sp314.DTR02.view;


//Segment consists of 2 points and a mileage
public class Segment {


	private Point toPoint;
	private Point fromPoint;
	private int distance;
	private String unitLabel;

	/*************************** CONSTRUCTORS *********************/
	//MAIN Constructor
	public Segment(Point from, Point to, int distance, String unitLabel) {
		this.fromPoint = from;
		this.toPoint = to;
		this.distance = distance;
		this.unitLabel = unitLabel;

	}

	/******************************* GETTERS **********************/

	public Point getToPoint() {
		return toPoint;
	}
	public Point getFromPoint() {
		return fromPoint;
	}

	public int getDistance() {
		return distance;
	}

	public String getUnits() {
		return unitLabel;
	}


}