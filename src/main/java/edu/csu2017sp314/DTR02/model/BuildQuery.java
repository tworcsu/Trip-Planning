package edu.csu2017sp314.DTR02.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BuildQuery {

    ArrayList<String> select = null;
    ArrayList<String> join = null;
    ArrayList<String> where = null;
    ArrayList<String> from = null;
    Map<String, String> likes = null;
    Map<String, String> equals = null;
    int limit = 100;

    /**
     * Constructor
     */
    public BuildQuery() {
	select = new ArrayList<String>();
	join = new ArrayList<String>();
	where = new ArrayList<String>();
	from = new ArrayList<String>();
	equals = new HashMap<String, String>();
	likes = new HashMap<String, String>();
    }

    /*
     * Builds the query
     */
    public String toString() {
	StringBuilder sb = new StringBuilder("SELECT ");
	if (select.isEmpty()) {
	    sb.append("*");
	}
	// APPENDLIST (sb,init,sep)
	appendList(sb, select, "", " , ");
	appendList(sb, from, " FROM ", "");
	appendList(sb, join, " INNER JOIN ", " INNER JOIN ");
	appendList(sb, where, " WHERE ", " AND ");
	sb.append(" limit " + limit + ";");

	return sb.toString();
    }

    /**
     * General string maker for each query
     * 
     * @param sb StringBuilder that is used to make the giant query
     * @param List The ArrayList of strings you're joining
     * @param init The specific way the Strings in List are initialized (from
     *            needs "FROM", join needs "JOIN", etc)
     * @param sep The way the strings in the List are separated from each other
     *            (where separated by "AND", select separated by ",")
     */
    private void appendList(StringBuilder sb, ArrayList<String> List, String init, String sep) {
	boolean first = true;
	int counter = 0;
	for (String item : List) {
	    if (first) {
		sb.append(init);
		first = false;
	    } else if (counter++ < List.size()) {
		sb.append(sep);
	    }
	    sb.append(item);
	}

    }

    /**
     * Build select query (ex. SELECT airports.id airports.name ). Must make a
     * new select if there is another table's information you want
     * 
     * @param table the table where all the values are coming from
     * @param columns the specific column in that table
     */
    public void select(String table, String... columns) {

	for (String col : columns) {
	    StringBuilder sb = new StringBuilder();
	    sb.append(table + "." + col);
	    select.add(sb.toString());
	}
    }

    /**
     * from query builder, only allows one from
     * 
     * @param otherFrom the table you're starting from
     */
    public void from(String otherFrom) {
	if (from.isEmpty())
	    from.add(otherFrom);
    }

    /**
     * Builds the join query (ex. JOIN table ON table2.col2 = table.col)
     * 
     * @param table the table you're looking to join
     * @param col the specific column on [table]
     * @param table2 the other table you're looking to join
     * @param col2 the specific column on [table2]
     */
    public void join(String table, String col, String table2, String col2) {
	StringBuilder sb = new StringBuilder();
	sb.append(table);
	sb.append(" ON ");
	sb.append(table + "." + col); // add table.col
	sb.append(" = " + table2 + "." + col2);
	join.add(sb.toString());

    }

    /**
     * where method that sets terms to equal a specific value
     * 
     * @param table The Database's table you want
     * @param column The specific field in the given table
     * @param input The value that you're looking for equality checks on (ex.
     *            airports.id = [input])
     */
    public void where(String table, String column, String input) {
	StringBuilder sb = new StringBuilder().append(table).append("." + column).append(" = '").append(input + "'");
	where.add(sb.toString());
    }

    /**
     * Overloaded where method that sets the query based on the GUI's search
     * fields
     * 
     * @param filter The Map made by the GUI's search fields
     */
    public void where(Map<String, String> filter) {
	breakFilter(filter);
	// fill where with Equals
	for (Entry<String, String> entry : equals.entrySet()) {
	    StringBuilder sb = new StringBuilder().append(entry.getKey()).append(" = '" + entry.getValue() + "'");
	    where.add(sb.toString());

	}
	// fill where with Likes
	for (Entry<String, String> entry : likes.entrySet()) {
	    StringBuilder sb = new StringBuilder().append(entry.getKey()).append(" LIKE '%" + entry.getValue() + "%'");
	    where.add(sb.toString());
	}

    }

    /**
     * Breaks up the filter for more manageability
     * 
     * @param filter The Map made by the GUI's search fields
     */
    private void breakFilter(Map<String, String> filter) {
	for (Map.Entry<String, String> entry : filter.entrySet()) {

	    if (!entry.getKey().isEmpty() && !entry.getKey().isEmpty()) {
		String filterWithCol = setFilterCol(entry.getKey());
		likesOrEquals(filterWithCol, entry.getValue());

	    }

	}

    }

    /**
     * Places values in "likes" or "equals" depending on if their key actually
     * has a value
     * 
     * @param key The key in the Map specified in the GUI
     * @param value The associated value of the key
     */
    private void likesOrEquals(String key, String value) {
	if (value == null || value.isEmpty()) {
	    value = ""; // for nullExceptions
	    likes.put(key, value);
	    return;
	}

	switch (key) // these are ALWAYS "like %%"
	{
	case ("airports.name"):
	case ("airports.municipality"):
	case ("airports.code"): {
	    likes.put(key, value);
	    return;
	}
	default:
	    break;
	}
	equals.put(key, value);
    }

    /**
     * Sets the names in the filter to specific database relations (table
     * becomes table.col)
     * 
     * @param key The key in the filter specified in the GUI
     *            (continent,country,region, etc...)
     * @return the key appended with the column it's associated to
     */
    private String setFilterCol(String key) {
	StringBuilder sb = new StringBuilder();
	switch (key) {
	case ("continent"): {
	    sb.append("continents.name");
	    break;
	}
	case ("country"): {
	    sb.append("countries.name");
	    break;
	}
	case ("region"): {
	    sb.append("regions.name");
	    break;
	}
	case ("name"): {
	    sb.append("airports.name");
	    break;
	}
	case ("municipality"): {
	    sb.append("airports.municipality");
	    break;
	}
	case ("code"): {
	    sb.append("airports.id");
	    break;
	}
	case ("type"): {
	    sb.append("airports.type");
	    break;
	}
	default:{
	    break;
	}

	}

	return sb.toString();

    }

}
