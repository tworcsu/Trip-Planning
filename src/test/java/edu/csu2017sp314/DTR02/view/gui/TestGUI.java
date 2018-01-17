package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestGUI {

	/**
	 * The only externally accessible methods in GUI that don't open a new X
	 * window (which won't work on Travis) are getPresenter and getView, both of
	 * which are package-protected rather than public.
	 */
	@Test
	public void testCtor() {
		GUI gui = new GUI();
		assertNotNull(gui.getPresenter());
		assertNotNull(gui.getView());
	}
}