package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestOptionsPane extends JFXTest {
	@Test
	public void testCtor() {
		OptionsPane opts = new OptionsPane();
		assertNotNull(opts);
	}
	
	@Test
	public void testUseMiles() {
		OptionsPane opts = new OptionsPane();
		assertTrue(opts.useMiles());
	}
	
	@Test
	public void testGetOptLevel() {
		OptionsPane opts = new OptionsPane();
		assertEquals("Nearest Neighbor",opts.getOptLevel());
	}
	
	@Test
	public void testGetTitle() {
		OptionsPane opts = new OptionsPane();
		assertEquals("Trip Title",opts.getTitle());
	}
	
	@Test
	public void testShowDistances() {
		OptionsPane opts = new OptionsPane();
		assertFalse(opts.showDistances());
	}
	
	@Test
	public void testShowIDs() {
		OptionsPane opts = new OptionsPane();
		assertFalse(opts.showIDs());
	}
}
