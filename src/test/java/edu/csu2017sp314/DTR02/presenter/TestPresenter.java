package edu.csu2017sp314.DTR02.presenter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.csu2017sp314.DTR02.view.Segment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class TestPresenter {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        try{
            List<String> lines = Arrays.asList("﻿ID,Name,City,Latitude,Longitude,Altitude",
                    "bottomleft,The Southwest Corner of square,test1,-30,-30,6566",
                    "bottomright,The Southeast Corner of square,test2,-30,0,4950",
                    "topleft,The Northwest Corner of square,test3,0,-30,1",
                    "topright,The Northeast Corner of square,test4,0,0,4949");
            Files.write(Paths.get("test-presenter-testCSV.csv"), lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Files.delete(Paths.get("test-presenter-testCSV.csv"));
    }

    //TODO: ACCOUNT FOR THE LACK OF CSV SUPPORT
//    @Test
//    public void testLegsToSegs() {
//    	Map <String,String> files = new TreeMap<>();
//    	files.put("csv", "test-presenter-testCSV.csv");
//    	Map <Character, Boolean> flags = new TreeMap<>();
//        Presenter p = new Presenter(flags, files);
//        ArrayList<Segment> s = p.legsToSegs(p.model.getTrip());
//        double left = 494.29;
//        double right = 599.62;
//        double top = 325.5;
//        double bottom = 431.33;
//
//        assertEquals(left,s.get(0).getFromPoint().getX(),0.01);
//        assertEquals(bottom,s.get(0).getFromPoint().getY(),0.01);
//        assertEquals(right,s.get(1).getFromPoint().getX(),0.01);
//        assertEquals(bottom,s.get(1).getFromPoint().getY(),0.01);
//        assertEquals(right,s.get(2).getFromPoint().getX(),0.01);
//        assertEquals(top,s.get(2).getFromPoint().getY(),0.01);
//        assertEquals(left,s.get(3).getFromPoint().getX(),0.01);
//        assertEquals(top,s.get(3).getFromPoint().getY(),0.01);
//
//        assertEquals(right,s.get(0).getToPoint().getX(),0.01);
//        assertEquals(bottom,s.get(0).getToPoint().getY(),0.01);
//        assertEquals(right,s.get(1).getToPoint().getX(),0.01);
//        assertEquals(top,s.get(1).getToPoint().getY(),0.01);
//        assertEquals(left,s.get(2).getToPoint().getX(),0.01);
//        assertEquals(top,s.get(2).getToPoint().getY(),0.01);
//        assertEquals(left,s.get(3).getToPoint().getX(),0.01);
//        assertEquals(bottom,s.get(3).getToPoint().getY(),0.01);
//
//    }

    @Test
    public void testTranslateLongitude() {
        Presenter p = new Presenter("test-presenter-testCSV.csv", false, false, false);
        double x = p.translateLongitude(-30);
        assertEquals(494.29,x,0.01);
        double xx = p.translateLongitude(0);
        assertEquals(599.62,xx,0.01);
    }

    @Test
    public void testTranslateLatitude() {
        Presenter p = new Presenter("test-presenter-testCSV.csv", false, false, false);
        double y = p.translateLatitude(0);
        assertEquals(325.5,y,0.01);
        double yy = p.translateLatitude(-30);
        assertEquals(431.33,yy,0.01);
    }
}

