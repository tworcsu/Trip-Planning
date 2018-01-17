CREATE TABLE airports(
    ident int(11) NOT NULL,
    id varchar(1000),
    type varchar(1000),
    name varchar(1000),
    latitude varchar(1000),
    longitude varchar(1000),
    elevation_ft varchar(1000),
    continent varchar(1000),
    iso_country varchar(1000),
    iso_region varchar(1000),
    municipality varchar(1000),
    scheduled_service varchar(1000),
    gps_code varchar(1000),
    iata_code varchar(1000),
    local_code varchar(1000),
    home_link varchar(1000),
    wikipedia_link varchar(1000),
    keywords varchar(1000),
    PRIMARY KEY (ident)
);

CREATE TABLE continents(
    id varchar(2) NOT NULL,
    name varchar(100),
    PRIMARY KEY (id)
);

CREATE TABLE countries(
    id int(11) NOT NULL,
    code varchar(1000),
    name varchar(1000),
    continent varchar(1000),
    wikipedia_link varchar(1000),
    keywords varchar(1000),
    PRIMARY KEY (id)
);

CREATE TABLE regions(
    id int(11) NOT NULL,
    code tinytext,
    local_code tinytext,
    name tinytext,
    continent tinytext,
    iso_country tinytext,
    wikipedia_link tinytext,
    keywords tinytext,
    PRIMARY KEY (id)
);

INSERT INTO airports VALUES (2, "OM11", "small_airport", "Abu Dhabi Northeast Airport", 24.51889991760254, 54.980098724365234, 88, "AS", "AE", "AE-AZ", "Abu Dhabi", "no","","","","","","");
INSERT INTO airports VALUES (3, "AGGH", "medium_airport", "Honiara International Airport", -9.4280004501343, 160.05499267578, 28, "OC", "SB", "SB-CT", "yes", "AGGH", "HIR","" ,"" , "http://en.wikipedia.org/wiki/Honiara_International_Airport", "Henderson Field","");
INSERT INTO airports VALUES (6523, "00A", "heliport", "Total Rf Heliport", 40.07080078125, -74.93360137939453, 11, "NA", "US", "US-PA", "Bensalem", "no", "00A", "", "00A", "", "", "");
INSERT INTO airports VALUES (6524, "00AK", "small_airport", "Lowell Field", 59.94919968, -151.695999146, 450, "NA", "US", "US-AK", "Anchor Point", "no", "00AK","", "00AK", "", "", "");
INSERT INTO airports VALUES (6525, "00AL", "small_airport", "Epps Airpark", 34.86479949951172, -86.77030181884766, 820, "NA", "US", "US-AL", "Harvest", "no", "00AL", "", "00AL", "", "", "");

INSERT INTO continents VALUES("AF", "Africa");
INSERT INTO continents VALUES("AN", "Antarctica");
INSERT INTO continents VALUES("AS", "Asia");
INSERT INTO continents VALUES("EU", "Europe");
INSERT INTO continents VALUES("NA", "North America");
INSERT INTO continents VALUES("OC", "Oceania");

INSERT INTO countries VALUES(302672, "AD", "Andorra", "EU", "http://en.wikipedia.org/wiki/Andorra", "");
INSERT INTO countries VALUES(302618, "AE", "United Arab Emirates", "AS", "http://en.wikipedia.org/wiki/United_Arab_Emirates", "UAE");
INSERT INTO countries VALUES(302619, "AF", "Afghanistan", "AS", "http://en.wikipedia.org/wiki/Afghanistan", "");
INSERT INTO countries VALUES(302722, "AG", "Antigua and Barbuda", "NA", "http://en.wikipedia.org/wiki/Antigua_and_Barbuda", "");
INSERT INTO countries VALUES(302723, "AI", "Anguilla", "NA", "http://en.wikipedia.org/wiki/Anguilla", "");
INSERT INTO countries VALUES(302781, "SB", "Solomon Islands", "OC", "http://en.wikipedia.org/wiki/Solomon_Islands", "");
INSERT INTO countries VALUES(302755, "US", "United States", "NA", "http://en.wikipedia.org/wiki/United_States", "America");

INSERT INTO regions VALUES(302811, "AD-02", 02, "Canillo", "EU", "AD", "http://en.wikipedia.org/wiki/Canillo", "");
INSERT INTO regions VALUES(302812, "AD-03", 03, "Encamp", "EU", "AD", "http://en.wikipedia.org/wiki/Encamp", "");
INSERT INTO regions VALUES(302813, "AD-04", 04, "La Massana", "EU", "AD", "http://en.wikipedia.org/wiki/La_Massana", "");
INSERT INTO regions VALUES(302814, "AD-05", 05, "Ordino", "EU", "AD", "http://en.wikipedia.org/wiki/Ordino", "");
INSERT INTO regions VALUES(302815, "AD-06", 06, "Sant Julià de Lòria", "EU", "AD", "http://en.wikipedia.org/wiki/Sant_Julià_de_Lòria", "");
INSERT INTO regions VALUES(302816, "AD-07", 07, "Andorra la Vella", "EU", "AD", "http://en.wikipedia.org/wiki/Andorra_la_Vella", "");
INSERT INTO regions VALUES(302820, "AE-AZ", 08, "Abu Dhabi Emirate", "AS", "AE", "http://en.wikipedia.org/wiki/Abu_Dhabi_(emirate)", "Abu áº’aby");
INSERT INTO regions VALUES(305375, "SB-CT", 09, "Capital Territory (Honiara)", "OC", "SB", "http://en.wikipedia.org/wiki/Capital_Territory_(Honiara)", "");
INSERT INTO regions VALUES(306114, "US-PA", 10, "Pennsylvania", "NA", "US", "http://en.wikipedia.org/wiki/Pennsylvania", ""); 
INSERT INTO regions VALUES(306076, "US-AK", 11, "Alaska", "NA", "US", "http://en.wikipedia.org/wiki/Alaska", "");
INSERT INTO regions VALUES(306077, "US-AL", 12, "Alabama", "NA", "US", "http://en.wikipedia.org/wiki/Alabama", "");
