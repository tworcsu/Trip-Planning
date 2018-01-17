package edu.csu2017sp314.DTR02.view;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSegment {

	@Test
	public void testSegment() {
		//segment - to, from, mileage
		//point - x,y,name,id
		Point p1 = new Point(1,2,"name","id");
		Point p2 = new Point(3,4,"name2","id2");
		Segment seg1 = new Segment(p1,p2,9999, "Miles");
		assertNotNull(seg1);
	}
	@Test
	public void testSegmentToPoint() {
		Point p1 = new Point(1,2,"name","id");
		Point p2 = new Point(3,4,"name2","id2");
		Segment seg1 = new Segment(p1,p2,9999, "Miles");
		assertEquals(seg1.getToPoint(),p2);
	}
	@Test
	public void testSegmentFPoint() {
		Point p1 = new Point(1,2,"name","id");
		Point p2 = new Point(3,4,"name2","id2");
		Segment seg1 = new Segment(p1,p2,9999, "Miles");
		assertEquals(seg1.getFromPoint(),p1);
	}
	@Test
	public void testSegmentMileage() {
		Point p1 = new Point(1,2,"name","id");
		Point p2 = new Point(3,4,"name2","id2");
		Segment seg1 = new Segment(p1,p2,9999, "Miles");
		assertEquals(seg1.getDistance(),9999);
	}

}
