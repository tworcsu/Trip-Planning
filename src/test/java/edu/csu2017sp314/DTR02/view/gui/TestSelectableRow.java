package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestSelectableRow extends JFXTest{

	/**
	 * Verify that construction with a properly formatted String populates
	 * fields appropriately.
	 */
	@Test
	public void testStringCtor() {
		SelectableRow row = new SelectableRow("ID::Country::Region::Municipality");
		assertNotNull(row);
		assertEquals("ID",row.getIdText());
		assertEquals("Country",row.getCountry());
		assertEquals("Region",row.getRegion());
		assertEquals("Municipality",row.getMunicipality());
		assertTrue(row.isSelected());
	}
	
	/**
	 * Verify that the new object and the old object are separate objects,
	 * but the same text contents
	 */
	@Test
	public void testCopyCtor() {
		SelectableRow row1 = new SelectableRow("ID::Country::Region::Municipality");
		SelectableRow row2 = new SelectableRow(row1);
		row1.getBox().setSelected(true);
		row2.getBox().setSelected(false);
		assertTrue(row1.equals(row2));
		assertEquals(row1.isSelected(),!row2.isSelected());
	}
	
	/**
	 * Verify that the toString method returns a string in the expected format.
	 */
	@Test
	public void testToString() {
		SelectableRow row = new SelectableRow("ID::Country::Region::Municipality");
		assertEquals(row.toString(), "ID Country Region Municipality");
	}
}