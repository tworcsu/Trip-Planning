## Release Notes

#### What is done
Update Working Environment
Refactor
    - GUI was completely refactored, other classes were not.
Improved test coverage
    - We've gone from 35.86% to 54.92% by adding 25 tests.
Better Itinerary
Worldwide units
Worldwide trips
Worldwide maps


#### Known bugs/defects
- filename and xml title are hard coded right now
- region url and country url are not in itinerary
- filtering by airport type does not work


#### How to use
usage: TripCo [-Options] file.csv [map.svg] [select.xml]
Options:
    d: Display leg distance on map
    m: Display in miles
    k: Display in kilometers
    i: Display location id on map (cannot be used in conjunction with "n")
    2: Perform 2-opt improvements on the generated route
    3: Perform 3-opt improvements on the generated route
    g: Run the application in graphical mode
    select.xml: An XML file containing the lines from file.csv to use when generating a route


#### References
Great circle distance - spherical law of cosines
http://www.movable-type.co.uk/scripts/latlong.html

XML creation example
https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/

Colorado dimensions
https://en.wikipedia.org/wiki/Colorado

