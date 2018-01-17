package edu.csu2017sp314.DTR02.view.gui;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Platform;

public class TestMapWindow extends JFXTest {
	@BeforeClass
	public static void makeSampleXml() {
		List<String> lines = Arrays.asList(
				"<svg viewBox=\"0 0 125 80\" xmlns=\"http://www.w3.org/2000/svg\">",
				"  <text y=\"75\" font-size=\"100\" font-family=\"serif\"><![CDATA[10]]></text>",
				"</svg>");
		try {
			Files.write(Paths.get("test-map.svg"), lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@AfterClass
	public static void tearDown() {
		try {
			Files.delete(Paths.get("test-map.svg"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testMap() {
		Platform.runLater(() -> {
			assertNotNull(new MapWindow("test-map.svg"));
		});
	}
}