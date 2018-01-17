package edu.csu2017sp314.DTR02.view;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestPoint {

	@Test
	public void testPoint(){
		//x,y,name,id
		Point p1 = new Point(1,2,"name","id");
		assertNotNull(p1);	
	}
	
	@Test
	public void testPointsNotSame(){
		//x,y,name,id
		Point p1 = new Point(1,2,"name","id");
		Point p2 = new Point(1,2,"name","id");
		assertNotSame(p1,p2);	
	}
	
	@Test
	public void testGetX(){
		//x,y,name,id
		Point p1 = new Point(1,2,"name","id");
		assertEquals(p1.getX(),1,0.0001);
	}
	
	@Test
	public void testGetY(){
		//x,y,name,id
		Point p1 = new Point(1,2,"name","id");
		assertEquals(p1.getY(),2,0.0001);
	}
	
	@Test
	public void testGetName(){
		//x,y,name,id
		Point p1 = new Point(1,2,"name","id");
		assertEquals(p1.getName(),"name");
	}
	
	@Test
	public void testGetId(){
		//x,y,name,id
		Point p1 = new Point(1,2,"name","id");
		assertEquals(p1.getId(),"id");
	}
}
