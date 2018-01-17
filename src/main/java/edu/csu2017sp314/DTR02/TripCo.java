package edu.csu2017sp314.DTR02;

import java.util.Map;
import java.util.TreeMap;

import edu.csu2017sp314.DTR02.presenter.Presenter;
import edu.csu2017sp314.DTR02.view.View;

public class TripCo {

  public static void main (String [] args) {
	  //consider passing Maps around instead of long lists of parameters?
	  //more flags might get added later, or we might get graded on BetterCodeHub scoring.
	  Map<String, String> filenames = new TreeMap<String, String>();
      Map<Character, Boolean> flags = new TreeMap<Character, Boolean>();
      flags.put('i',false);
      flags.put('m',false);
      flags.put('k',false);
      flags.put('d',false);
      flags.put('g',false);
      flags.put('2',false);
      flags.put('3',false);

      if (args.length < 1) usage();

      // parse the args.
      // Allows options to be specified either one by one e.g. -i -m or together e.g. -im
		for (String arg : args) {
			// if it starts with a '-' it's an option
			if (arg.charAt(0) == '-') {
				for (int i = 1; i < arg.length(); i++) {
					switch (arg.charAt(i)) {
					case 'i':
					case 'm':
					case 'k':
					case 'd':
					case 'g':
					case '2':
					case '3':
						flags.put(arg.charAt(i), true);
						break;
					default:
						usage();
						break;
					}
				}
			}
			// Else it's a filename
			else if (arg.contains(".csv") && !filenames.containsKey("csv")) {
				filenames.put("csv", arg);
				filenames.put("noext", filenames.get("csv").substring(0,filenames.get("csv").indexOf('.')));
			} else if (arg.contains(".xml") && !filenames.containsKey("xml")) {
				filenames.put("xml", arg);
			} else if (arg.contains(".svg") && !filenames.containsKey("svg")) {
				filenames.put("svg", arg);
			}
			// User gave too many arguments
			else {
				usage();
			}
		}
		// Can't ask for both miles and kilometers
		if (flags.get('m') && flags.get('k'))
			usage();
		// csv no longer required
		//if (!filenames.containsKey("csv"))
		//	usage();

		// Start the application
		if (flags.get('g')) {
			View.launchGui();
		} else {
			Presenter p = new Presenter(flags,filenames);
			p.tripInitiate();
		}

  }

    private static void usage() {
        System.out.println("usage: TripCo [-Options] file.csv [map.svg] [select.xml]");
        System.out.println("Options:");
        System.out.println("    m: Display distances in miles");
        System.out.println("    k: Display distances in kilometers");
        System.out.println("    d: Display distance of each leg on map");
        System.out.println("    i: Display location id on map");
        System.out.println("    2: Perform 2-opt improvements on the generated route");
        System.out.println("    3: Perform 3-opt improvements on the generated route");
        System.out.println("    g: Run the application in graphical mode");
        System.out.println("    file.csv: A csv file containing fields \"id\", \"name\", \"latitude\", \"longitude\"");
        System.out.println("    map.svg: An SVG file containing a background map upon which to render the generated route");
        System.out.println("    select.xml: An XML file containing the lines from file.csv to use when generating a route");
        System.exit(1);
    }
}
