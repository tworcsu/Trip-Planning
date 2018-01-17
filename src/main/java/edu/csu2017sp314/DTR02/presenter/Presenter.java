package edu.csu2017sp314.DTR02.presenter;

import edu.csu2017sp314.DTR02.model.*;
import edu.csu2017sp314.DTR02.view.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Presenter {
    protected Model model;
    protected View view;
    private Map<String, String> filenames;
    private Map<Character, Boolean> flags;

    /**
     * This constructor is currently only being used by JUnit tests.
     * 
     * @param fileName - CSV file.
     * @param optM - Do we display mileage?
     * @param optN - Do we display names?
     * @param optI - Do we display IDs?
     */
    public Presenter(String fileName, boolean optM, boolean optN, boolean optI) {
	this.model = new Model();
	// Remove .csv extension from filename
	fileName = (fileName.endsWith(".csv")) ? fileName.substring(0, fileName.length() - 4) : fileName;
	this.view = new View(fileName, optM, optN, optI);
    }

    /**
     * Simple getter to receive the view. Used to avoid constructing multiple
     * Views when launching the GUI.
     * 
     * @return the view constructed in the constructor of this class.
     */
    public View getView() {
	return view;
    }

    /**
     * Constructor used in command line
     * 
     * @param flags Map of all the flags &ltChar,Bool&gt
     * @param filenames Map of all the filenames &ltString, String&gt
     */
    public Presenter(Map<Character, Boolean> flags, Map<String, String> filenames) {
	this.flags = flags;
	this.filenames = filenames;
	if (filenames.containsKey("csv")) {
	    this.filenames.put("noext", filenames.get("csv").substring(0, filenames.get("csv").indexOf('.')));
	}
	model = new Model();
	if (filenames.containsKey("csv") && filenames.get("csv").contains(".csv")) {
		//TODO: refactor based on the fact that we can't read CSV files anymore.
	    //model.setLocations(filenames.get("csv"));
	}

	// if selection xml exists, set model's selectionLocations
	// need to give model csv first
	if (filenames.containsKey("xml") && !filenames.get("xml").equals("")) {
	    model.setSelection(filenames.get("xml"));
	}

	if (flags.containsKey('3') && flags.get('3')) {
	    use3opt();
	} else if (flags.containsKey('2') && flags.get('2')) {
	    use2opt();
	} else {
	    useNN();
	}

	this.view = new View(flags, filenames);
    }

    /**
     * Either constructs a SQL query and sends it to the Model, or communicates
     * the components in the query to the Model so that it can be built there.
     *
     * @TODO IMPLEMENT ME
     * @param fields This Map should contain all of the following keys:
     *            "continent","country","region","name","municipality","code","type"
     *            an empty string indicates no selection, so don't query for
     *            that parameter.
     * @return A List<String> containing up to 100 IDs from the database
     *         matching the filters specified in fields. Each entry has the
     *         format: ID::Country(name)::Region(Code)::Municipality
     */
    public List<String> queryFromFilters(Map<String, String> fields) {
	List<String> query = new ArrayList<String>();

	if (model.isConnected()) {
	    query = model.findFromFilter(fields);
	}
	
	return query;
    }
    
    /**
     * @return List<String> of Airports in form id::country::region::munic
     */
    public List<String> getSelectedList()
    {
	return model.getSelectionList();
	
    }

    public void setTitle(String path) {
	filenames.put("noext", path);
	view.setTitle(path);
    }

    public void useMiles(boolean miles) {
	model.useMiles(miles);
	flags.put('m', miles);
	flags.put('k', !miles);
    }

    /**
     * The GUI needs to be able to populate a dropdown of countries based on a
     * selected continent.
     *
     * 
     * @param continent the currently selected continent: used to search the
     *            database for countries
     * @return a list of country names in the database on the selected continent
     */
    public List<String> getCountries(String continent) {
	List<String> countries = new ArrayList<String>();

	if (model.isConnected()) {
	    countries = model.findCountries(continent);
	}
	
	return countries;
    }

    /**
     * The GUI needs to be able to populate a dropdown of regions based on a
     * selected country.
     *
     *
     * @param country the currently selected country: used to search the
     *            database for regions
     * @return a list of region names (codes are difficult to read) in the
     *         database in the selected country.
     */
    public List<String> getRegions(String type) {
	List<String> regions = new ArrayList<String>();
	if (model.isConnected()) {
	    regions = model.findRegions(type);
	}
	
	return regions;
    }

    /**
     * Attempts to log in to the database via the Model before submitting
     * queries.
     *
     *
     * @param username The username needed to log into the database.
     * @param password The password used with username.
     * @return Whether or not the login was successful.
     */
    public boolean login(String username, char[] password) {
	boolean loginSuccessful = false;
	if (model.isConnected()) {
	    return true;
	} else {
	    // Sets the connection in model and verifies
	    loginSuccessful = (model.connectDb(username, password));
	}

	// clean up after authentication succeeds or fails
	for (int i = 0; i < password.length; i++) {
	    password[i] = '\0';
	}
	return loginSuccessful;

	// LEAVING FOR TESTING PURPOSES
	// hardcoding username and password for now so I can test the GUI.
	// also hardcoding simple authentication (the real deal will be in the
	// DBMS, not our program).
	/*
	 * char[] passToMatch = {'p','a','s','s','w','o','r','d'}; boolean
	 * passMatched = passToMatch.length == password.length; for (int i = 0;
	 * passMatched && i < password.length; i++) { passMatched &= password[i]
	 * == passToMatch[i]; }
	 */

    }

    /**
     * Updates View with a background map
     * 
     * @param filename SVG background map
     */
    public void setBackgroundMap(String filename) {
	filenames.put("svg", filename);
	view.setBackground(filename);
    }

    /**
     * Updates Model's list of SelectLocations (a subset of Locations). Model
     * must be given the CSV before given the Selection XML
     * 
     * @param selection XML file which is subset of the CSV locations
     */
    public void setSelection(String selection) {
	filenames.put("xml", selection);
	model.setSelection(selection);
    }

    /**
     * updates the Model's selections to prepare for running a query
     * 
     * @param selection IDs to select
     */
    public void setSelection(List<String> selection) {
	model.setSelectionIDs(selection);
    }

    /**
     * Updates Model's Locations using the CSV
     * 
     * @param filename CSV file
     */
    public void setCSV(String filename) {
	filenames.put("csv", filename);
	//TODO: refactor around the fact that we don't have CSV inputs anymore
	//model.setLocations(filename);
    }

    /**
     * Sets the model's opt level to use nearest-neighbor
     *
     */
    public void useNN() {
	flags.put('2', false);
	flags.put('3', false);
	model.setOptimizationLevel(edu.csu2017sp314.DTR02.model.Model.BEST_NN);
    }

    /**
     * Sets the model's opt level to use 2-opt
     */
    public void use2opt() {
	flags.put('2', true);
	flags.put('3', false);
	model.setOptimizationLevel(edu.csu2017sp314.DTR02.model.Model.TWO_OPT);
    }

    /**
     * Sets the model's opt level to use 3-opt
     */
    public void use3opt() {
	flags.put('2', true);
	flags.put('3', true);
	model.setOptimizationLevel(edu.csu2017sp314.DTR02.model.Model.THREE_OPT);
    }

    /**
     * Creates the segments used by View from Model's list of the trip
     */
    public void tripInitiate() {
	view.writeFiles(legsToSegs(model.getTrip()));
    }

    /**
     * query model for name from selection
     * 
     * @return model's selection title
     */
    public String getSelectionName() {
	return model.getSelectionTitle();
    }

    /**
     * Sets the selection in Model
     * 
     * @param filename XML of selection
     * @return Empty String
     */
    public String getSelectionName(String filename) {
	if (filename != null && !filename.equals("") && filenames.containsKey("xml") && filenames.get("xml") != null
		&& !filename.equals(filenames.get("xml"))) {
	    setSelection(filename);
	}
	return getSelectionName();
    }

    /**
     * Gives list of Location ID's
     * 
     * @return List of ALL the ID's
     */
    public List<String> getAllIDs() {
	return model.getLocationIds();
    }

    /**
     * Gives list of selectedID's
     * 
     * @return List of SELECTED ID's
     */
    public List<String> getSelectedIDs() {
	return model.getSelectionIds();
    }

    /**
     * Gives the list of selectedID's while also updating the selection file
     * 
     * @param filename XML selection
     * @return List of SELECTED ID's
     */
    public List<String> getSelectedIDs(String filename) {
	if (filename != null && !filename.equals("") && filenames.containsKey("xml") && filenames.get("xml") != null
		&& !filename.equals(filenames.get("xml"))) {
	    setSelection(filename);
	}
	return getSelectedIDs();
    }

    /**
     * Turns a trip consisting of Legs into Segments used by the View
     * 
     * @param legs Two Locations and a length in Geo coord format
     * @return Arraylist of segments
     */
    ArrayList<Segment> legsToSegs(ArrayList<Leg> legs) {
	ArrayList<Segment> segs = new ArrayList<>();
	for (Leg leg : legs) {
	    Point start = new Point(translateLongitude(leg.getStart().getLong()),
		    translateLatitude(leg.getStart().getLat()), leg.getStart().getLat(), leg.getStart().getLong(),
		    leg.getStart().getAttributes());
	    Point end = new Point(translateLongitude(leg.getEnd().getLong()), translateLatitude(leg.getEnd().getLat()),
		    leg.getEnd().getLat(), leg.getEnd().getLong(), leg.getEnd().getAttributes());
	    long distance;
	    String units;
	    if (flags.containsKey('k') && flags.get('k')) {
		distance = leg.getKilometers();
		units = "Kilometers";
	    } else {
		distance = leg.getMiles();
		units = "Miles";
	    }
	    segs.add(new Segment(start, end, (int) distance, units));
	}
	return segs;
    }

    /**
     * Turns longitude into a spot on world map
     * 
     * @param longitude
     * @return The longitude value into cartesian form
     */
    double translateLongitude(double longitude) {
	// calibrated for file World1.svg
	final double pixelWidth = 1264; // width of world map in pixels
	final double longWidth = 360; // Width of Earth in degrees Longitude
	final double westEdge = -168.5; // West edge of world map in degrees
					// Longitude

	double pixelsPerDegreeLong = pixelWidth / longWidth;
	double shiftLong = westEdge * pixelsPerDegreeLong - 8;

	// Longitudes less than -168.5 should wrap to right side of map
	double xVal = longitude * pixelsPerDegreeLong - shiftLong;
	if (xVal < 8)
	    xVal += 1264;
	return xVal;

    }

    /**
     * Turns latitude into a spot on world map
     * 
     * @param latitude
     * @return The latitude value into cartesian form
     */
    double translateLatitude(double latitude) {
	// calibrated for file World1.svg
	final double pixelHeight = 635; // height of world map in pixels
	final double latHeight = 180; // Height of Earth in degrees latitude
	final double northEdge = 90; // north Edge of world map in degrees
				     // Latitude

	double pixelsPerDegreeLat = pixelHeight / latHeight;
	double shiftLat = northEdge * pixelsPerDegreeLat + 8;

	return -1 * latitude * pixelsPerDegreeLat + shiftLat;
    }

}
