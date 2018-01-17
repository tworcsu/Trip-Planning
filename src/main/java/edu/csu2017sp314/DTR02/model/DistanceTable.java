package edu.csu2017sp314.DTR02.model;

import java.util.Map;
import java.util.TreeMap;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;

public class DistanceTable {
	private Map<LocPair, Double> distances;
	private GeodeticCalculator geoCalc;
	Ellipsoid reference = Ellipsoid.WGS84;
	private boolean useMiles;
	
	public DistanceTable() {
		geoCalc = new GeodeticCalculator();
		distances = new TreeMap<LocPair, Double>();
		useMiles = false;
	}
	
	public DistanceTable(boolean useMiles) {
		this();
		this.useMiles = useMiles;
	}
	
	public void useMiles() {
		if (!useMiles) {
			useMiles = true;
			//convert or recompute distances from kilometers
		}
	}
	
	public void useKilometers() {
		if (useMiles) {
			useMiles = false;
			//convert or recompute distances from miles
		}
	}
	
	private void computeDistance(LocPair locs) {
		// calculate the geodetic curve
		GeodeticCurve geoCurve = 
				geoCalc.calculateGeodeticCurve(reference, locs.getFirst().getCoords(), locs.getSecond().getCoords());
		if (useMiles) {
			double ellipseMiles = geoCurve.getEllipsoidalDistance() * 0.000621371192;
			distances.put(locs, ellipseMiles);
		} else {
			double ellipseKilometers = geoCurve.getEllipsoidalDistance() / 1000.0;
			distances.put(locs, ellipseKilometers);
		}
	}
	
	public long get(Location loc1, Location loc2) {
		LocPair pair = new LocPair(loc1,loc2);
		if (!distances.containsKey(pair)) {
			computeDistance(pair);
		}
		return Math.round(distances.get(pair));
	}
}
