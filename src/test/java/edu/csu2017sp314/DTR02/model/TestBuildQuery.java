package edu.csu2017sp314.DTR02.model;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.Test;

public class TestBuildQuery {

    @Test
    public void testToString() {
	final String test1 = "SELECT table.col1 , table.col2 , table.col3 FROM myfrom WHERE table.col3 = '2' limit 100;";
	BuildQuery bq = new BuildQuery();
	bq.select("table", "col1", "col2", "col3");
	bq.from("myfrom");
	bq.where("table", "col3", "2");
	assertEquals(test1, bq.toString());
    }

    @Test
    public void testFrom() {
	final String test1 = "SELECT * FROM myfrom limit 100;";
	BuildQuery bq = new BuildQuery();
	bq.from("myfrom");
	assertEquals(test1, bq.toString());
    }

    @Test
    public void testSelect() {
	final String test1 = "SELECT air.col1 , air.col2 , air.col3 , reg.col1 , reg.col2 FROM myfrom limit 100;";
	BuildQuery bq = new BuildQuery();
	bq.select("air", "col1", "col2", "col3");
	bq.select("reg", "col1", "col2");
	bq.from("myfrom");
	assertEquals(test1, bq.toString());
    }

    @Test
    public void testJoin() {
	final String test1 = "SELECT * FROM air INNER JOIN reg ON reg.id = air.id limit 100;";
	BuildQuery bq = new BuildQuery();
	bq.from("air");
	bq.join("reg", "id", "air", "id");
	assertEquals(test1, bq.toString());
    }

    @Test
    public void testWhere() {
	final String test1 = "SELECT * FROM air WHERE air.id = '3' limit 100;";
	final String test2 = "SELECT * FROM air WHERE air.id = '3' AND air.durr = '4' limit 100;";
	BuildQuery bq = new BuildQuery();
	bq.from("air");
	bq.where("air", "id", "3");
	assertEquals(test1, bq.toString());
	bq.where("air", "durr", "4");
	assertEquals(test2, bq.toString()); // check the AND
    }

    @Test
    public void testWhereMap() {
	TreeMap<String, String> fields = new TreeMap<String, String>();
	// FULL
	fields.put("continent", "myContinent");
	fields.put("country", "myCountryUSAUSA");
	fields.put("region", "myregion");
	fields.put("name", "myName");
	fields.put("municipality", "myMunic");
	fields.put("code", "myCode");
	fields.put("type", "myType");

	final String test1 = "SELECT * FROM air" + " WHERE countries.name = 'myCountryUSAUSA'"
		+ " AND continents.name = 'myContinent'" + " AND airports.id = 'myCode'"
		+ " AND regions.name = 'myregion'" + " AND airports.type = 'myType'"
		+ " AND airports.municipality LIKE '%myMunic%'" + " AND airports.name LIKE '%myName%'" + " limit 100;";

	BuildQuery bq = new BuildQuery();
	bq.from("air");
	bq.where(fields);

	assertEquals(test1, bq.toString());

	final String test2 = "SELECT * FROM air " + "WHERE countries.name LIKE '%%'" + " AND continents.name LIKE '%%'"
		+ " AND airports.municipality LIKE '%%'" + " AND airports.id LIKE '%%'" + " AND regions.name LIKE '%%'"
		+ " AND airports.name LIKE '%%'" + " AND airports.type LIKE '%%'" + " limit 100;";
	// EMPTY
	fields.clear();
	fields.put("continent", "");
	fields.put("country", "");
	fields.put("region", "");
	fields.put("name", "");
	fields.put("municipality", "");
	fields.put("code", "");
	fields.put("type", "");

	BuildQuery bq2 = new BuildQuery();
	bq2.from("air");
	bq2.where(fields);

	assertEquals(test2, bq2.toString()); // check the AND

    }
}
