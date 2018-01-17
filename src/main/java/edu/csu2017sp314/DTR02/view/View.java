package edu.csu2017sp314.DTR02.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import edu.csu2017sp314.DTR02.presenter.Presenter;
import edu.csu2017sp314.DTR02.view.gui.GUI;

//Will take arraylist of segments/leg, break them into pieces and produce
//a corresponding XML and SVG file
public class View {
	private Map<Character, Boolean> flags;
	private Map<String, String> filenames;
	private Presenter presenter;
	private GUI gui = null;

	/**
	 * Updates XML (selection) filename in View and in the Presenter.
	 * 
	 * @param selection
	 *            XML file which is subset of the CSV locations
	 */
	public void setSelection(String selection) {
		filenames.put("xml", selection);
		presenter.setSelection(selection);
	}

	/**
	 * Updates output filename (without extension).
	 * 
	 * @param filename
	 *            no-extension filename
	 */
	public void setTitle(String filename) {
		filenames.put("noext", filename);
	}

	/**
	 * Updates Presenter's optimization level to use Nearest Neighbor.
	 */
	public void useNN() {
		flags.put('2', false);
		flags.put('3', false);
		presenter.useNN();
	}

	/**
	 * Updates Presenter's optimization level to use 2-opt
	 */
	public void use2opt() {
		flags.put('2', true);
		flags.put('3', false);
		presenter.use2opt();
	}

	/**
	 * Updates Presenter's optimization level to use 3-opt
	 */
	public void use3opt() {
		flags.put('2', true);
		flags.put('3', true);
		presenter.use3opt();
	}

	/**
	 * Simple setter for the GUI object held by View. Not initialized in
	 * constructor.
	 *
	 * @param gui
	 *            the GUI object to be held by View.
	 */
	public void setGui(GUI gui) {
		this.gui = gui;
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	// Constructor
	/**
	 * Legacy constructor. Here to support JUnit tests that haven't been
	 * updated.
	 *
	 * @param filename
	 *            the no-extension filename of the CSV input file, and of the
	 *            SVG and XML output files.
	 * @param mFlag
	 *            "Are we displaying mileage?"
	 * @param nFlag
	 *            "Are we displaying names?"
	 * @param iFlag
	 *            "Are we displaying IDs?"
	 */
	public View(String filename, boolean mFlag, boolean nFlag, boolean iFlag) {
		flags = new TreeMap<Character, Boolean>();
		filenames = new TreeMap<String, String>();
		flags.put('d', mFlag);
		flags.put('n', nFlag);
		flags.put('i', iFlag);
		filenames.put("noext", filename);
	}

	/**
	 * Constructor which takes a data structure to represent flags and
	 * filenames.
	 *
	 * @param flags
	 *            A mapping of chars to boolean values: for some char c, "is 'c'
	 *            the flag set?"
	 * @param filenames
	 *            A mapping of Strings to Strings, to enable asking questions
	 *            like "Where is the csv file?"
	 */
	public View(Map<Character, Boolean> flags, Map<String, String> filenames) {
		this.flags = flags;
		this.filenames = filenames;
		if (filenames.containsKey("csv")) {
			filenames.put("noext", filenames.get("csv").substring(0, filenames.get("csv").indexOf('.')));
		}
	}

	/**
	 * A public setter to update the location of the SVG background map
	 *
	 * @param filename
	 *            the new location of the background map
	 */
	public void setBackground(String filename) {
		if (filename != null && (new File(filename).exists())) {
			filenames.put("svg", filename);
		} else {
			filenames.put("svg", "");
		}
	}

	/**
	 * A public method to launch the GUI interface from classes which do not
	 * extend Application
	 */
	static public void launchGui() {
		GUI.launchApplication();
	}

	/**
	 * no-arg constructor: everything is initialized to a null or new value.
	 * Used when constructed from GUI. In particular, the fields gui and
	 * presenter are null. Be wary of them.
	 */
	public View() {
	}

	/**
	 * Generates the XML itinerary and SVG map and KML files from a list of
	 * segments.
	 *
	 * @param tripSegments
	 *            the tour segments to insert into the itinerary and map.
	 */
	public void writeFiles(ArrayList<Segment> tripSegments) {
		createXML(tripSegments);
		createSVG(tripSegments);
		createKml(tripSegments);
	}

	/**
	 * Helper method which calls the constructor of the WriteKmlFile object
	 * 
	 * @param tripSegments
	 *            the tour segments being written to the itinerary.
	 */
	private void createKml(ArrayList<Segment> tripSegments) {
		new WriteKmlFile(tripSegments, filenames);
		if (flags.containsKey('g') && flags.get('g')) {
			gui.displayGoogleMap(filenames.get("noext") + ".kml");
		}
	}

	/**
	 * Helper method which calls the constructor of the WriteXmlFile object, and
	 * displays the generated file on the GUI if the GUI is enabled.
	 *
	 * @param tripSegments
	 *            the tour segments being written to the itinerary.
	 */
	private void createXML(ArrayList<Segment> tripSegments) {
		generateNoext();
		new WriteXmlFile(tripSegments, filenames);
		if (flags.containsKey('g') && flags.get('g')) {
			gui.displayItinerary(filenames.get("noext") + ".xml");
		}
	}

	/**
	 * Generates a noext (no-extension) filename based on current option flags.
	 */
	private void generateNoext() {
		String noext = filenames.get("xml");
		if (noext == null || noext.equals("")) {
			noext = "selection";
		} else {
			noext = noext.substring(0, noext.indexOf(".xml"));
		}
		for (Map.Entry<Character, Boolean> flag : flags.entrySet()) {
			if (flag.getValue() && !"g23".contains(""+flag.getKey())) {
				noext += "-" + flag.getKey();
			}
		}
		if (flags.containsKey('3') && flags.get('3')) {
			noext += "-3";
		} else if (flags.containsKey('2') && flags.get('2')) {
			noext += "-2";
		}
		noext += "-t02";
		filenames.put("noext", noext);
	}

	/**
	 * Helper method which calls the constructor of the WriteSvgFile object.
	 *
	 * @TODO: display the generated file on the GUI if the GUI is enabled.
	 * @param tripSegments
	 *            the tour segments being written to the map.
	 */
	private void createSVG(ArrayList<Segment> tripSegments) {
		new WriteSvgFile(tripSegments, flags, filenames);
		if (flags.containsKey('g') && flags.get('g')) {
			gui.displayMap(filenames.get("noext") + ".svg");
		}
	}
}