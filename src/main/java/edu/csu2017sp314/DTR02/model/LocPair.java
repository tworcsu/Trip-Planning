package edu.csu2017sp314.DTR02.model;

public class LocPair implements Comparable<LocPair> {
	private Location[] locs;
	private String ids;

	public LocPair(Location one, Location two) {
		locs = new Location[2];
		locs[0] = one.getid().compareTo(two.getid()) < 0? one : two;
		locs[1] = locs[0].equals(one)? two : one;
		ids = locs[0].getid() + locs[1].getid();
	}
	
	public Location getFirst() {
		return locs[0];
	}
	
	public Location getSecond() {
		return locs[1];
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof LocPair)) {
			return false;
		}

		LocPair otherPair = (LocPair) other;
		return (locs[0].equals(otherPair.locs[0]) && locs[1].equals(otherPair.locs[1]))
			|| (locs[0].equals(otherPair.locs[1]) && locs[1].equals(otherPair.locs[0]));
	}
	
	@Override
	public int compareTo(LocPair o) {
		return ids.compareTo(o.ids);
	}
}
