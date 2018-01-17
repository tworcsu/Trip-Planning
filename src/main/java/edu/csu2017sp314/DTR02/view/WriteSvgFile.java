package edu.csu2017sp314.DTR02.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WriteSvgFile {

	final String font = "Sans-serif";
	final String font_size = "16";
	final String title_size = "20";
	final String font_family = "Sans-serif";
	final String stroke_width = "5";
	final String strokeColor = "#999999";
	int totalMiles = 0;
	int legCount = 1;
	Map<Character, Boolean> flags = null;
	Map<String, String> filenames = null;
	String unitLabel;

	// spaces for pretty output
	final String blockSpace = "  ";
	final String innerSpace = blockSpace + "  ";

	/**
	 * Constructor which takes a pair of Maps as well as a list of Segments.
	 * 
	 * @param segments
	 *            the Segments to write onto the map.
	 * @param flags
	 *            A structure containing runtime flags.
	 * @param filenames
	 *            A structure containing the filename this class is meant to
	 *            write to
	 */
	public WriteSvgFile(ArrayList<Segment> segments, Map<Character, Boolean> flags, Map<String, String> filenames) {
		this.flags = flags;
		this.filenames = filenames;
		writeFile(segments, filenames.get("noext"), flags.get('d'), flags.get('i'));
	}

	/**
	 * Private helper method (formerly the constructor) which actually generates
	 * the SVG file.
	 * 
	 * @param tripSegments
	 *            the Segments to write onto the map.
	 * @param filename
	 *            the filename identifying to where the map should be written.
	 * @param mFlag
	 *            identifies whether or not to display mileage on the map.
	 * @param nFlag
	 *            identifies whether or not to display names on the map.
	 */
	private void writeFile(ArrayList<Segment> tripSegments, String filename, boolean mFlag, boolean iFlag) {
		final String header = "<?xml version=\"1.0\"?>";
		String svgHeader = "<svg width=\"1280\" height=\"652\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:svg=\"http://www.w3.org/2000/svg\">";

		final String legTitle = "Legs";
		final String TitleTitle = "Titles";
		final String locTitle = "Locations";
		unitLabel = (flags.containsKey('k') && flags.get('k')) ? "Kilometers" : "Miles";

		File file = new File(filename + ".svg");

		try {
			List<String> background = getBackground();

			PrintWriter printwriter = new PrintWriter(file);
			printwriter.println(header);
			printwriter.println(svgHeader);

			for (int i = 0; i < background.size(); i++) {
				String line = ((i == 0 || i == background.size() - 1) ? blockSpace : innerSpace) + background.get(i);
				printwriter.println(line);
			}

			// Leg START
			printwriter.println(startBlock());
			printwriter.println(addTitle(legTitle));
			for (Segment seg : tripSegments) {
				printwriter.println(createLeg(seg));
				legCount++;
			}
			printwriter.println(endBlock());
			// Leg END

			legCount = 1;

			// Title START
			printwriter.println(startBlock());
			printwriter.println(addTitle(TitleTitle));
			for (Segment seg : tripSegments) {
				totalMiles += seg.getDistance();
			}
			printwriter.println(createTitles());
			printwriter.println(endBlock());
			// Title END

			// Locations START
			printwriter.println(startBlock());
			printwriter.println(addTitle(locTitle));
			for (Segment seg : tripSegments) {
				printwriter.println(createLocation(seg, iFlag));
				legCount++;
			}

			printwriter.println(endBlock());
			// Locations END

			printwriter.println(endSvg());
			printwriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private List<String> getBackground() {
		List<String> background;
		try {
			background = Files.readAllLines(new File(this.getClass().getResource("/world1.svg").toURI()).toPath());
			// trim extra XML header and comment(s)
			while (!background.get(0).contains("<svg") 
				&& !background.get(0).contains("<!")) {
				background.remove(0);
			}

			return background;
		} catch (IOException | URISyntaxException e) {
			return new ArrayList<String>();
		}
	}

	/**
	 * Helper method to create SVG text objects which represent endpoints on a
	 * journey, labelled as appropriate.
	 * 
	 * @param seg
	 *            the segment containing the requisite endpoints (only From is
	 *            used)
	 * @param nFlag
	 *            whether or not to display names
	 * @param iFlag
	 *            whether or not to display IDs
	 * @return An XML string like<br>
	 *         {@code<text font-family="Sans-serif" font-size="16" id="id1" y=
	 *         "100" x="100">CityA</text>}
	 */
	private String createLocation(Segment seg, boolean iFlag) {
		String id = "";
		if (iFlag) {
			id = seg.getFromPoint().getId();
		}
		double x = seg.getFromPoint().getX();
		double y = seg.getFromPoint().getY();
		return innerSpace + "<text font-family=\"" + font_family + "\" font-size=\"" + font_size + "\" id=\"id"
				+ legCount + "\" y=\"" + y + "\" x=\"" + x + "\">" + id + "</text>";
	}

	/**
	 * Helper method to create SVG elements representing the title of the map,
	 * and the total length of the tour.
	 * 
	 * @return An XML block containing elements for the title of the map and
	 *         length of the tour.
	 */
	private String createTitles() {
		String state = "<text text-anchor=\"middle\" font-family=\"" + font_family + "\" font-size=\"" + title_size
				+ "\" id=\"state\" y=\"25\" x=\"100\">" + filenames.get("noext") + "</text>";
		String distance = "<text text-anchor=\"middle\" font-family=\"" + font_family + "\" font-size=\"" + title_size
				+ "\" id=\"distance\" y=\"620\" x=\"150\"> Total Distance: " + totalMiles + " " + unitLabel + "</text>";
		return innerSpace + state + "\n" + innerSpace + distance;
	}

	/**
	 * Helper method to draw a line from a Segment
	 * 
	 * @param seg
	 *            the Segment representing the start and endpoints of this leg
	 *            of the tour
	 * @return an XML Line object between the two coordinate points in segment.
	 *         Optionally also returns a text object which displays the
	 *         segment's distance.
	 */
	private String createLeg(Segment seg) {
		double y2 = seg.getToPoint().getY();
		double x2 = seg.getToPoint().getX();
		double y1 = seg.getFromPoint().getY();
		double x1 = seg.getFromPoint().getX();
		String segment = "";
		String distanceString = "";

		if (Math.abs(x2 - x1) > 632) {
			return splitLeg(seg);
		}

		else {
			segment = innerSpace + "<line id=" + "\"leg" + legCount + "\" y2=\"" + y2 + "\" x2=\"" + x2 + "\" y1=\""
					+ y1 + "\" x1=\"" + x1 + "\" stroke-width=\"" + stroke_width + "\" stroke=\"" + strokeColor
					+ "\" />";
		}

		if (flags.containsKey('d') && flags.get('d')) {
			double midx = (x1 + x2) / 2;
			double midy = (y1 + y2) / 2;
			String distance = Integer.toString(seg.getDistance());
			distanceString = "\n" + innerSpace + "<text font-family=\"" + font_family + "\" font-size=\"" + font_size
					+ "\" id=\"leg" + legCount + "\" y=\"" + midy + "\" x=\"" + midx + "\">" + distance + "</text>";
		}
		return segment + distanceString;
		// EXAMPLE: <line id="leg1" y2="110" x2="500" y1="100" x1="100"
		// stroke-width="3" stroke="#999999"/>
	}

	/**
	 * Helper method to draw a split line of a Segment if it needs to wrap
	 * 
	 * @param Segment
	 *            to be split
	 * @return two XML line objects representing the segment. Optionally also
	 *         returns a text object which displays the segment's distance.
	 */
	private String splitLeg(Segment seg) {
		double y2 = seg.getToPoint().getY();
		double x2 = seg.getToPoint().getX();
		double y1 = seg.getFromPoint().getY();
		double x1 = seg.getFromPoint().getX();
		String distanceString = "";

		int mapRightEdge = 1272;
		int mapLeftEdge = 8;
		// ensure x2 > x1
		if (x1 > x2) {
			double temp = x1;
			x1 = x2;
			x2 = temp;
			temp = y1;
			y1 = y2;
			y2 = temp;
		}
		double yIntercept = (((x1 + mapRightEdge) * y2 - y1 * x2) + mapRightEdge * (y1 - y2))
				/ (x1 - x2 + mapRightEdge);
		// draw line from x1,y1 to 0,yIntercept
		String line1 = innerSpace + "<line id=" + "\"leg" + legCount + "\" y2=\"" + y1 + "\" x2=\"" + x1 + "\" y1=\""
				+ yIntercept + "\" x1=\"" + mapLeftEdge + "\" stroke-width=\"" + stroke_width + "\" stroke=\""
				+ strokeColor + "\" />";
		String line2 = innerSpace + "<line id=" + "\"leg" + legCount + "\" y2=\"" + y2 + "\" x2=\"" + x2 + "\" y1=\""
				+ yIntercept + "\" x1=\"" + mapRightEdge + "\" stroke-width=\"" + stroke_width + "\" stroke=\""
				+ strokeColor + "\" />";
		if (flags.containsKey('d') && flags.get('d')) {
			String distance = Integer.toString(seg.getDistance());
			distanceString = "\n" + innerSpace + "<text font-family=\"" + font_family + "\" font-size=\"" + font_size
					+ "\" id=\"leg" + legCount + "\" y=\"" + yIntercept + "\" x=\"" + 10 + "\">" + distance + "</text>";
		}
		return line1 + "\n" + line2 + distanceString;
	}

	/**
	 * Helper method to generate an XML title block around an arbitrary title
	 * 
	 * @param title
	 *            the title to use in the XML block
	 * @return the XML title block around the arbitrary title
	 */
	private String addTitle(String title) {
		return innerSpace + "<title>" + title + "</title>";
	}

	/**
	 * Helper method to generate the start of an SVG group
	 * 
	 * @return the beginning of an SVG group, appropriately indented.
	 */
	private String startBlock() {
		return blockSpace + "<g>";
	}

	/**
	 * Helper method to generate the end of an SVG group
	 * 
	 * @return the end of an SVG group, appropriately indented.
	 */
	private String endBlock() {
		return blockSpace + "</g>";
	}

	/**
	 * Helper method to generate the end of an SVG image
	 * 
	 * @return the tag representing the end of an SVG image.
	 */
	private String endSvg() {
		return "</svg>";
	}
}
