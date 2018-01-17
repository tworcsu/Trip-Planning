package edu.csu2017sp314.DTR02.model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Model {
    // optimization level constants so we don't have to use bools or magic
    // numbers
    public static final int BEST_NN = 1;
    public static final int TWO_OPT = 2;
    public static final int THREE_OPT = 3;

    private ArrayList<Leg> trip = null;
    private ArrayList<Location> locations = null;
    private ArrayList<Location> selectionLocations = null;
    private ArrayList<String> selectionIDs = null;
    private String selectionTitle;
    private int optimizationLevel;
    private boolean useMiles = false;
    private Connection connection = null; // Connection...bad practice(TODO: use
					  // connection pool)
    private List<String> selectionList = null;

    public void useMiles(boolean miles) {
	useMiles = miles;
    }

    /**
     * no-arg constructor to avoid null pointer exception when launching GUI.
     * (There isn't a CSV filename at time of GUI launch.) Will also make sense
     * to have one when querying a database instead of a CSV file.
     */
    public Model() {
	this.selectionIDs = new ArrayList<String>();
	this.optimizationLevel = Model.BEST_NN;
	this.selectionTitle = "";
    }

    /**
     * sets selected IDs for the purposes of querying
     *
     * @param ids a list of selected IDs
     */
    public void setSelectionIDs(List<String> ids) {
	selectionIDs = new ArrayList<String>();
	for (String id : ids) {
	    selectionIDs.add(id);
	}
    }

    /**
     * sets the optimization level
     *
     * @param level BEST_NN - unoptimized nearest neighbor tour TWO_OPT - 2-opt
     *            optimization THREE_OPT - 3-opt optimization
     */
    public void setOptimizationLevel(int level) {
	this.optimizationLevel = level;
    }

    /**
     * @return boolean if the connection is valid (current timeout = 0)
     */
    public boolean isConnected() {
	if (connection != null) {
	    try { // timeout = 0 "A value of 0 indicates a timeout is not
		  // applied to the database operation."
		return connection.isValid(0); // TODO: change timeout to
					      // something meaningful
	    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	return false;
    }

    // TODO: GUI inits fields that don't exist
    // REMOVE!!!
    public void connectOverride(String un, String pw) {
	connectDb(un, pw.toCharArray());
    }

    /**
     * Disconnects from DB and sets it to null
     */
    public void disconnectDb() {
	try {
	    this.connection.close();
	    this.connection = null;
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    /**
     * @param username Student E-id
     * @param password Student E-num
     * @return a boolean if connection was established
     */
    public boolean connectDb(String username, char[] password) {
	String myDriver = "com.mysql.jdbc.Driver";
	String myUrl = "jdbc:mysql://faure.cs.colostate.edu/cs314";
	// System.out.println(password);

	try { // connect to the database
	    Class.forName(myDriver);
	    String temp = "";
	    for (int i = 0; i < password.length; i++) // safe password to
						      // string...
		temp += password[i];

	    Connection conn = DriverManager.getConnection(myUrl, username, temp);
	    temp = ""; // probably doesn't help
	    this.connection = conn;
	    return true;

	} catch (Exception e) {
	    System.err.printf("Exception: ");
	    System.err.println(e.getMessage());
	}
	return false;
    }

    /**
     * sets selectionLocations based on location ids present in an XML file
     *
     * @param xmlFile - a file containing the subset of ids to be selected
     */
    public void setSelection(String xmlFile) {
	if (xmlFile.equals("")) {
	    // if filename is an empty string, select everything.
	    selectionLocations = new ArrayList<Location>(locations);
	} else {
	    try {
		this.selectionLocations = new ArrayList<Location>();
		File fXmlFile = new File(xmlFile);

		if (!fXmlFile.exists()) {
		    System.err.println("NO SELECTION XML TO READ");
		    // If no XML, exit the method
		    return;
		}
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		// Set the title
		if (doc.getElementsByTagName("title").getLength() != 0) {
		    this.selectionTitle = doc.getElementsByTagName("title").item(0).getTextContent();
		} else {
		    this.selectionTitle = "";
		}

		// In order to iterate through id tags
		NodeList nlist = doc.getElementsByTagName("id");
		Statement st = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
			java.sql.ResultSet.CONCUR_READ_ONLY);
		for (int i = 0; i < nlist.getLength(); i++) {
		    String id = nlist.item(i).getTextContent();

		    selectionIDs.add(id);
		    String myquery = getIdQuery(id);

		    ResultSet rs = st.executeQuery(myquery);
		    rs.next();
		    double latitude = Double.parseDouble(rs.getString("latitude"));
		    double longitude = Double.parseDouble(rs.getString("longitude"));
		    
		    Map<String, String> attributes = new HashMap<>();
		    placeAttributes(rs,attributes,id);
		    
		    Location loc = new Location(latitude, longitude, attributes);
		    this.selectionLocations.add(loc);

		    rs.close();
		}

		st.close();
		// Sets up the strings for Gui to read
		setSelectionList();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * @param rs The result set (query garbage)
     * @param attributes Map of all the attributes that a Location has
     * @param id the Id specified in the selection XML
     */
    private void placeAttributes(ResultSet rs, Map<String, String> attributes, String id) {
	try {
	    attributes.put("id", id);
	    attributes.put("name", rs.getString("name"));
	    attributes.put("elevation", rs.getString("elevation_ft"));
	    attributes.put("municipality", rs.getString("municipality"));
	    attributes.put("region", rs.getString("iso_region"));
	    attributes.put("country", rs.getString("iso_country"));
	    attributes.put("continent", rs.getString("continent"));
	    attributes.put("airportURL", rs.getString("airports.wikipedia_link"));
	    attributes.put("regionURL", rs.getString("regions.wikipedia_link"));
	    attributes.put("countryURL", rs.getString("countries.wikipedia_link"));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Breaks up Set Selection so BetterCodeHub will stfu
     * @param id the id given to by the selection xml
     * @return Query String in all its glory
     */
    private String getIdQuery(String id) {
	BuildQuery bq = new BuildQuery();
	bq.select("airports", "id", "iso_country", "iso_region", "continent", "wikipedia_link", "municipality",
		"elevation_ft", "name", "latitude", "longitude");
	bq.select("countries", "wikipedia_link");
	bq.select("regions", "wikipedia_link");
	bq.from("airports");
	bq.join("regions", "code", "airports", "iso_region");
	bq.join("countries", "code", "regions", "iso_country");
	bq.where("airports", "id", id);
	return bq.toString();
    }

    /**
     * Initializes the selection List in GUI readable form (
     * id::country::region::munic)
     */
    private void setSelectionList() {
	this.selectionList = new ArrayList<String>();
	for (Location loc: selectionLocations)
	{
	    //selectionList
	    //break it into "id::country:region::municipality"
	    String full = loc.getid()+ "::" + loc.getCountry() + "::" 
		    	   + loc.getRegion() + "::" + loc.getMunicipality();
	    selectionList.add(full);
	    
	}
	
    }
    
    /**
     * @return List<String> to presenter for GUI to create selectable rows
     */
    public List<String> getSelectionList()
    {
	return  this.selectionList;
	
    }
    

    /**
     * returns the trip calculated based on current selection and optimization
     * level
     *
     * @return - the trip, as an ArrayList of legs
     */
    public ArrayList<Leg> getTrip() {
	this.buildNearestNeighborTrip(this.selectionLocations);
	// this.applyTwoOpt(this.trip);
	// switch (this.optimizationLevel) {
	// case TWO_OPT:
	// this.applyTwoOpt(this.trip);
	// break;
	// case THREE_OPT:
	// this.applyThreeOpt(this.trip);
	// break;
	// default:
	// break;
	// }
	return trip;
    }

    /**
     * returns a list of all location IDs
     *
     * @return - list of location IDs
     */
    public List<String> getLocationIds() {
	ArrayList<String> ids = new ArrayList<>();
	for (Location loc : this.locations) {
	    ids.add(loc.getid());
	}
	return ids;
    }

    /**
     * returns a list of all location names
     *
     * @return - list of all location names
     */
    public List<String> getLocationNames() {
	ArrayList<String> names = new ArrayList<>();
	for (Location loc : this.locations) {
	    names.add(loc.getName());
	}
	return names;
    }

    /**
     * returns a list of IDs in the current selection
     *
     * @return - list of IDs in current selection
     */
    public List<String> getSelectionIds() {
	ArrayList<String> ids = new ArrayList<>();
	for (Location loc : this.selectionLocations) {
	    ids.add(loc.getid());
	}
	return ids;
    }

    /**
     * returns a list of names in the current selection
     *
     * @return - list of location names in current selection
     */
    public List<String> getSelectionNames() {
	ArrayList<String> names = new ArrayList<>();
	for (Location loc : this.selectionLocations) {
	    names.add(loc.getName());
	}
	return names;
    }

    /**
     * @return - the title of the selection
     */
    public String getSelectionTitle() {
	return this.selectionTitle;
    }

    /**
     * currently unused
     *
     * @return - array of latitudes in current trip
     */
    public double[] getTripLats() {
	double[] tripLatitudes = new double[trip.size() + 1];
	for (int i = 0; i < trip.size(); i++) {
	    tripLatitudes[i] = trip.get(i).getStart().getLat();
	}
	tripLatitudes[trip.size()] = tripLatitudes[0];
	return tripLatitudes;
    }

    /**
     * currently unused
     *
     * @return - array of longitudes in current trip
     */
    public double[] getTripLongs() {
	double[] tripLongitudes = new double[trip.size() + 1];
	for (int i = 0; i < trip.size(); i++) {
	    tripLongitudes[i] = trip.get(i).getStart().getLong();
	}
	tripLongitudes[trip.size()] = tripLongitudes[0];
	return tripLongitudes;
    }

    /**
     * currently unused
     *
     * @return - array of location names in current trip
     */
    public String[] getTripNames() {
	String[] tripNames = new String[trip.size() + 1];
	for (int i = 0; i < trip.size(); i++) {
	    tripNames[i] = trip.get(i).getStart().getName();
	}
	tripNames[trip.size()] = tripNames[0];
	return tripNames;
    }

    /**
     * currently unused
     *
     * @return - array of IDs in current trip
     */
    public String[] getTripIDs() {
	String[] tripIDs = new String[trip.size() + 1];
	for (int i = 0; i < trip.size(); i++) {
	    tripIDs[i] = trip.get(i).getStart().getid();
	}
	tripIDs[trip.size()] = tripIDs[0];
	return tripIDs;
    }
    
    /**
     * Converts latitude or longitude to decimal form
     *
     * @param coord - string representing a latitude or longitude
     * @return - double representing the lat. or long. in decimal form
     */
    public double parseLatLong(String coord) {
	double result = 0;
	// rip out all unrelated characters, then split on all known (to me, so
	// far) delimiters.
	String[] split = coord.replaceAll("[^0-9\\-.+:*°'\"NESW]", "").split("[:*°\"']");

	double deg = 0;
	double min = 0;
	double sec = 0;

	// determine if we need to produce a negative coordinate
	boolean negative = false;
	if (split[split.length - 1].matches("[NESW]")) {
	    switch (split[split.length - 1]) {
	    case "W":
	    case "S":
		negative = true;
	    case "N":
	    case "E":
		break;
	    }
	    // truncate last index (it won't parse as a double, after all)
	    split = Arrays.copyOf(split, split.length - 1);
	}

	switch (split.length) {
	case 3:
	    sec = Double.parseDouble(split[2]);
	case 2:
	    min = Double.parseDouble(split[1]);
	case 1:
	    deg = Double.parseDouble(split[0]);
	    break;
	default:
	    System.err.println("Unable to parse string " + coord);
	}

	// compute final decimalized coordinate, inverting if needed
	result = deg + (min / 60) + (sec / 3600);

	if (negative) {
	    result *= -1;
	}

	return result;
    }

    /**
     * Builds the trip based on the current optimization level Mutates member
     * variable "Trip"
     *
     * @param locations - a list of location objects which constitute a trip
     */
    private void buildNearestNeighborTrip(ArrayList<Location> locations) {
	long bestDistance = Long.MAX_VALUE;
	// note that index j is never used. Instead, the first location is moved
	// to the end at the bottom of the loop.
	for (int j = 0; j < locations.size(); j++) {
	    // we need a list of locations we can destroy.
	    ArrayList<Location> workingSet = new ArrayList<Location>(locations);

	    // we'll need to build a trip if we want to measure its total
	    // distance.
	    ArrayList<Leg> currentTrip = new ArrayList<Leg>();
	    // choose initial point to be locations[0]
	    Location current = workingSet.remove(0);
	    // because we remove an element every iteration
	    while (workingSet.size() > 0) {
		// build a first leg, any leg, starting from locations[0]
		Leg currentLeg = new Leg(current, workingSet.get(0));
		// we don't have a better leg yet, after all
		Leg bestLeg = currentLeg;
		for (int i = 0; i < workingSet.size(); i++) {
		    // construct a new leg for each Location in the working set
		    currentLeg = new Leg(current, workingSet.get(i));
		    // if it's better, remember which one was the best
		    if (currentLeg.getLength() < bestLeg.getLength()) {
			bestLeg = currentLeg;
		    }
		}
		currentTrip.add(bestLeg);
		// update current with where the last leg ended
		workingSet.remove(bestLeg.getEnd());
		current = bestLeg.getEnd();
	    }
	    // add a leg from the end to the start, otherwise it isn't a closed
	    // loop
	    currentTrip.add(new Leg(currentTrip.get(currentTrip.size() - 1).getEnd(), currentTrip.get(0).getStart()));

	    // apply two opt or three opt here
	    switch (this.optimizationLevel) {
	    case TWO_OPT:
		currentTrip = this.applyTwoOpt(currentTrip);
		break;
	    case THREE_OPT:
		currentTrip = this.applyThreeOpt(currentTrip);
		break;
	    }

	    long currentTripDistance = 0;
	    for (Leg leg : currentTrip) {
		currentTripDistance += leg.getLength();
	    }
	    if (currentTripDistance < bestDistance) {
		bestDistance = currentTripDistance;
		this.trip = currentTrip;
	    }

	    // move the current first Location to the end of the list
	    Location newEnd = locations.remove(0);
	    locations.add(newEnd);
	}
    }

    /**
     * Repeatedly performs a 2-opt pass until no improvements are made
     *
     * @param trip - a list of legs
     * @return - the 2-opt optimized trip
     */
    private ArrayList<Leg> applyTwoOpt(ArrayList<Leg> trip) {
	boolean improved = true;
	while (improved) {
	    ArrayList<Leg> result = this.twoOptPass(trip);
	    if (result == null) {
		improved = false;
	    } else {
		trip = result;
	    }
	    // System.out.println("Pass completed");
	}
	return trip;
    }

    /**
     * Iterates through a list of legs and performs a 2-opt swap if it makes the
     * trip shorter
     *
     * @param trip - a list of legs
     * @return - a better list of legs
     */
    private ArrayList<Leg> twoOptPass(ArrayList<Leg> trip) {
	boolean improved = false;
	for (int i = 0; i < trip.size() - 1; i++) {
	    for (int j = i + 1; j < trip.size(); j++) {
		// construct two new edges
		Leg edge1 = new Leg(trip.get(i).getStart(), trip.get(j).getStart());
		Leg edge2 = new Leg(trip.get(i).getEnd(), trip.get(j).getEnd());
		// see if the new edges are shorter
		if (edge1.getLength() + edge2.getLength() < trip.get(i).getLength() + trip.get(j).getLength()) {
		    improved = true;
		    // create new trip using the new edges
		    // all edges before i stay the same
		    ArrayList<Leg> newTrip = new ArrayList<>(trip.subList(0, i));
		    // add first new edge
		    newTrip.add(edge1);
		    // reverse all edges between i and j
		    for (int k = j - 1; k > i; k--) {
			newTrip.add(new Leg(trip.get(k).getEnd(), trip.get(k).getStart()));
		    }
		    // add second new edge
		    newTrip.add(edge2);
		    newTrip.addAll(trip.subList(j + 1, trip.size()));
		    // System.out.println("Swapped: " + i + " and " + j);
		    trip = newTrip;
		}
	    }
	}
	if (improved) {
	    return trip;
	}
	return null;
    }

    /**
     * Repeatedly performs a 3-opt pass until no improvements are made
     *
     * @param trip - a list of legs
     * @return - the 3-opt optimized trip
     */
    private ArrayList<Leg> applyThreeOpt(ArrayList<Leg> trip) {
	boolean improved = true;
	while (improved) {
	    ArrayList<Leg> result = this.threeOptPass(trip);
	    if (result == null) {
		improved = false;
	    } else {
		trip = result;
	    }
	    // System.out.println("Pass completed");
	}
	return trip;
    }

    /**
     * Iterates through a list of legs and determines the best swap for each
     * combination of 3 legs
     *
     * @param trip - a list of legs
     * @return - a better list of legs
     */
    private ArrayList<Leg> threeOptPass(ArrayList<Leg> trip) {
	boolean improved = false;
	for (int i = 0; i < trip.size() - 2; i++) {
	    for (int j = i + 1; j < trip.size() - 1; j++) {
		for (int k = j + 1; k < trip.size(); k++) {
		    // try all the swaps and apply best one
		    long bestLength = Integer.MAX_VALUE;
		    long currentLength = Integer.MAX_VALUE;
		    int bestSwap = 0;
		    Leg edge1, edge2, edge3;
		    for (int s = 0; s < 8; s++) {
			switch (s) {
			case 0: // base case
			    currentLength = trip.get(i).getLength() + trip.get(j).getLength() + trip.get(k).getLength();
			    break;
			case 1:
			    edge1 = trip.get(i);
			    edge2 = new Leg(trip.get(j).getStart(), trip.get(k).getStart());
			    edge3 = new Leg(trip.get(j).getEnd(), trip.get(k).getEnd());
			    currentLength = edge1.getLength() + edge2.getLength() + edge3.getLength();
			    break;
			case 2:
			    edge1 = new Leg(trip.get(i).getStart(), trip.get(k).getStart());
			    edge2 = trip.get(j);
			    edge3 = new Leg(trip.get(i).getEnd(), trip.get(k).getEnd());
			    currentLength = edge1.getLength() + edge2.getLength() + edge3.getLength();
			    break;
			case 3:
			    edge1 = new Leg(trip.get(i).getStart(), trip.get(j).getStart());
			    edge2 = new Leg(trip.get(i).getEnd(), trip.get(j).getEnd());
			    edge3 = trip.get(k);
			    currentLength = edge1.getLength() + edge2.getLength() + edge3.getLength();
			    break;
			case 4:
			    edge1 = new Leg(trip.get(i).getStart(), trip.get(j).getEnd());
			    edge2 = new Leg(trip.get(j).getStart(), trip.get(k).getEnd());
			    edge3 = new Leg(trip.get(k).getStart(), trip.get(i).getEnd());
			    currentLength = edge1.getLength() + edge2.getLength() + edge3.getLength();
			    break;
			case 5:
			    edge1 = new Leg(trip.get(i).getStart(), trip.get(k).getStart());
			    edge2 = new Leg(trip.get(j).getEnd(), trip.get(i).getEnd());
			    edge3 = new Leg(trip.get(j).getStart(), trip.get(k).getEnd());
			    currentLength = edge1.getLength() + edge2.getLength() + edge3.getLength();
			    break;
			case 6:
			    edge1 = new Leg(trip.get(i).getStart(), trip.get(j).getEnd());
			    edge2 = new Leg(trip.get(k).getStart(), trip.get(j).getStart());
			    edge3 = new Leg(trip.get(i).getEnd(), trip.get(k).getEnd());
			    currentLength = edge1.getLength() + edge2.getLength() + edge3.getLength();
			    break;
			case 7:
			    edge1 = new Leg(trip.get(i).getStart(), trip.get(j).getStart());
			    edge2 = new Leg(trip.get(i).getEnd(), trip.get(k).getStart());
			    edge3 = new Leg(trip.get(j).getEnd(), trip.get(k).getEnd());
			    currentLength = edge1.getLength() + edge2.getLength() + edge3.getLength();
			    break;
			}

			if (currentLength < bestLength) {
			    bestLength = currentLength;
			    bestSwap = s;
			}
		    }
		    if (bestSwap > 0) {
			improved = true;
			// System.out.println("swap found: " + i + " " + j + " "
			// + k + " ");
			trip = performThreeOptSwap(i, j, k, bestSwap, trip);
		    }
		}
	    }
	}
	if (improved)
	    return trip;
	return null;
    }

    /**
     * Performs a 3-opt swap on the provided legs
     *
     * @param i - first leg to be swapped
     * @param j - second leg to be swapped
     * @param k - third leg to be swapped
     * @param swap - which swap to perform (1-7)
     * @param trip - the trip on which to perform the swap
     * @return the trip that results from the swap
     */
    private ArrayList<Leg> performThreeOptSwap(int i, int j, int k, int swap, ArrayList<Leg> trip) {
	Leg edge1, edge2, edge3;
	ArrayList<Leg> newTrip = new ArrayList<>();
	switch (swap) {
	// cases 1-3 are equivalent to 2-opt swaps
	case 1:
	    edge1 = new Leg(trip.get(j).getStart(), trip.get(k).getStart());
	    edge2 = new Leg(trip.get(j).getEnd(), trip.get(k).getEnd());
	    // create new trip using the new edges
	    // all edges before j stay the same
	    newTrip = new ArrayList<>(trip.subList(0, j));
	    // add first new edge
	    newTrip.add(edge1);
	    // reverse all edges between j and k
	    for (int x = k - 1; x > j; x--) {
		newTrip.add(new Leg(trip.get(x).getEnd(), trip.get(x).getStart()));
	    }
	    // add second new edge
	    newTrip.add(edge2);
	    // add the rest
	    newTrip.addAll(trip.subList(k + 1, trip.size()));
	    break;
	case 2:
	    edge1 = new Leg(trip.get(i).getStart(), trip.get(k).getStart());
	    edge2 = new Leg(trip.get(i).getEnd(), trip.get(k).getEnd());
	    // create new trip using the new edges
	    // all edges before i stay the same
	    newTrip = new ArrayList<>(trip.subList(0, i));
	    // add first new edge
	    newTrip.add(edge1);
	    // reverse all edges between i and k
	    for (int x = k - 1; x > i; x--) {
		newTrip.add(new Leg(trip.get(x).getEnd(), trip.get(x).getStart()));
	    }
	    // add second new edge
	    newTrip.add(edge2);
	    // add the rest
	    newTrip.addAll(trip.subList(k + 1, trip.size()));
	    break;
	case 3:
	    edge1 = new Leg(trip.get(i).getStart(), trip.get(j).getStart());
	    edge2 = new Leg(trip.get(i).getEnd(), trip.get(j).getEnd());
	    // create new trip using the new edges
	    // all edges before i stay the same
	    newTrip = new ArrayList<>(trip.subList(0, i));
	    // add first new edge
	    newTrip.add(edge1);
	    // reverse all edges between i and j
	    for (int x = j - 1; x > i; x--) {
		newTrip.add(new Leg(trip.get(x).getEnd(), trip.get(x).getStart()));
	    }
	    // add second new edge
	    newTrip.add(edge2);
	    // add the rest
	    newTrip.addAll(trip.subList(j + 1, trip.size()));
	    break;
	// cases 4-7 perform 3-opt swaps
	case 4:
	    edge1 = new Leg(trip.get(i).getStart(), trip.get(j).getEnd());
	    edge2 = new Leg(trip.get(k).getStart(), trip.get(i).getEnd());
	    edge3 = new Leg(trip.get(j).getStart(), trip.get(k).getEnd());
	    // create new trip using the new edges
	    // go from 0 to i
	    newTrip = new ArrayList<>(trip.subList(0, i));
	    // add edge i -> j+1
	    newTrip.add(edge1);
	    // go from j+1 -> k
	    newTrip.addAll(trip.subList(j + 1, k));
	    // add edge k -> i+1
	    newTrip.add(edge2);
	    // go from i+1 -> j
	    newTrip.addAll(trip.subList(i + 1, j));
	    // add edge j -> k+1
	    newTrip.add(edge3);
	    // add the rest
	    newTrip.addAll(trip.subList(k + 1, trip.size()));
	    break;
	case 5:
	    edge1 = new Leg(trip.get(i).getStart(), trip.get(k).getStart());
	    edge2 = new Leg(trip.get(j).getEnd(), trip.get(i).getEnd());
	    edge3 = new Leg(trip.get(j).getStart(), trip.get(k).getEnd());
	    // create new trip using the new edges
	    // go from 0 to i
	    newTrip = new ArrayList<>(trip.subList(0, i));
	    // add edge i -> k
	    newTrip.add(edge1);
	    // go backwards from k -> j+1
	    for (int x = k - 1; x > j; x--) {
		newTrip.add(new Leg(trip.get(x).getEnd(), trip.get(x).getStart()));
	    }
	    // add edge j+1 -> i+1
	    newTrip.add(edge2);
	    // go from i+1 -> j
	    newTrip.addAll(trip.subList(i + 1, j));
	    // add edge j -> k+1
	    newTrip.add(edge3);
	    // add the rest
	    newTrip.addAll(trip.subList(k + 1, trip.size()));
	    break;
	case 6:
	    edge1 = new Leg(trip.get(i).getStart(), trip.get(j).getEnd());
	    edge2 = new Leg(trip.get(k).getStart(), trip.get(j).getStart());
	    edge3 = new Leg(trip.get(i).getEnd(), trip.get(k).getEnd());
	    // create new trip using the new edges
	    // go from 0 to i
	    newTrip = new ArrayList<>(trip.subList(0, i));
	    // add edge i -> j+1
	    newTrip.add(edge1);
	    // go from j+1 -> k
	    newTrip.addAll(trip.subList(j + 1, k));
	    // add edge k -> j
	    newTrip.add(edge2);
	    // go backwards from j -> i+1
	    for (int x = j - 1; x > i; x--) {
		newTrip.add(new Leg(trip.get(x).getEnd(), trip.get(x).getStart()));
	    }
	    // add edge i+1 -> k+1
	    newTrip.add(edge3);
	    // add the rest
	    newTrip.addAll(trip.subList(k + 1, trip.size()));
	    break;
	case 7:
	    edge1 = new Leg(trip.get(i).getStart(), trip.get(j).getStart());
	    edge2 = new Leg(trip.get(i).getEnd(), trip.get(k).getStart());
	    edge3 = new Leg(trip.get(j).getEnd(), trip.get(k).getEnd());
	    // create new trip using the new edges
	    // go from 0 to i
	    newTrip = new ArrayList<>(trip.subList(0, i));
	    // add edge i -> j
	    newTrip.add(edge1);
	    // go backwards from j -> i+1
	    for (int x = j - 1; x > i; x--) {
		newTrip.add(new Leg(trip.get(x).getEnd(), trip.get(x).getStart()));
	    }
	    // add edge i+1 -> k
	    newTrip.add(edge2);
	    // go backwards from k -> j+1
	    for (int x = k - 1; x > j; x--) {
		newTrip.add(new Leg(trip.get(x).getEnd(), trip.get(x).getStart()));
	    }
	    // add edge j+1 -> k+1
	    newTrip.add(edge3);
	    // add the rest
	    newTrip.addAll(trip.subList(k + 1, trip.size()));
	    break;
	}
	return newTrip;
    }

    /**
     * @return The list of all locations
     */
    public ArrayList<Location> getLocations() {
	return locations;
    }

    /**
     * @return The list of currently selected locations
     */
    public ArrayList<Location> getSelectionLocations() {
	return selectionLocations;
    }

    /**
     * @param continent The continent you need to find the countries of
     * @return The list of countries
     */
    public List<String> findCountries(String continent) {

	List<String> countries = new ArrayList<String>();
	if (!isConnected()) {
	    System.err.println("No Connection");
	    return null;
	    // TODO: reconnect Option
	}
	try {
	    BuildQuery bq = new BuildQuery();
	    bq.select("countries","name");
	    bq.from("continents");
	    bq.join("countries", "continent","continents","id");
	    bq.where("continents","name",continent);

	    Statement st = connection.createStatement();
	    ResultSet rs = st.executeQuery(bq.toString());
	    
	    countries.add("");//FIRST ENTRY BLANK 
	    while (rs.next())
	    {
		countries.add(rs.getString("name"));
	    }
	   
	    rs.close();
	    st.close();
	    // disconnectDB();
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return countries;
    }

    /**
     * @param country The country you want to find the regions of
     * @return The list of regions in the country
     */
    public List<String> findRegions(String country) {
	List<String> regions = new ArrayList<String>();

	if (!isConnected()) {
	    System.err.println("No Connection");
	    return null;
	    // TODO: reconnect Option
	}
	try {

	    Statement st = connection.createStatement();
	    BuildQuery bq = new BuildQuery();
	    bq.select("regions","name");
	    bq.from("countries");
	    bq.join("regions", "iso_country","countries","code");
	    bq.where("countries","name",country);

	    ResultSet rs = st.executeQuery(bq.toString());
	    regions.add("");
	    while (rs.next()) {
		regions.add(rs.getString("name"));
	    }
	    rs.close();
	    st.close();
	    // disconnectDB();
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return regions;

    }

    /**
     * @param fields Map<String,String> of
     *            continent","country","region","name","municipality","code","type"
     * @return List<String> where each element is in form
     *         "id::country:region::municipality"
     */
    public List<String> findFromFilter(Map<String, String> fields) {
	// "continent","country","region","name","municipality","code","type"

	List<String> filter = new ArrayList<String>();
	if (!isConnected()) {
	    System.err.println("No Connection");
	    return null;
	    // TODO: reconnect Option
	}

	try {
	    Statement st = connection.createStatement();
	    st.setFetchSize(100);
	    
	    BuildQuery bq = new BuildQuery();
	    bq.select("airports", "id", "iso_country", "iso_region", "municipality");
	    bq.from("continents");
	    bq.join("countries", "continent", "continents", "id");
	    bq.join("regions", "iso_country", "countries", "code");
	    bq.join("airports", "iso_region", "regions", "code");
	    bq.where(fields);

	    String filterToAdd = "";
	    
	    ResultSet rs = st.executeQuery(bq.toString());
	    ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
	    int cols = rsmd.getColumnCount();

	    while (rs.next()) {
		filterToAdd = "";
		for (int i = 1; i <= cols; i++) {
		    filterToAdd += rs.getString(i);
		    if (rs.getString(i).isEmpty())
			filterToAdd += "null";
		    if (i + 1 <= cols)
			filterToAdd += "::";
		}
		filter.add(filterToAdd);
	    }
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return filter;
    }

}