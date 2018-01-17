package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestSafePasswordField extends JFXTest {
	@Test
	public void testGetPassword() {
		SafePasswordField pass = new SafePasswordField();
		String test = "An example password!";
		pass.setText(test);
		try {
			char[] password = pass.getPassword();
			String passString = "";
			for (char c : password) {
				passString += c;
			}
			assertEquals(test,passString);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
