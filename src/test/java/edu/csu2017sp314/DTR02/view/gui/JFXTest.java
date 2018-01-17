package edu.csu2017sp314.DTR02.view.gui;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import org.junit.BeforeClass;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

/**
 * 
 * @author fge at Stack Overflow
 * @see <a
 *      href=http://stackoverflow.com/questions/28501307/javafx-toolkit-not-initialized-in-one-test-class-but-not-two-others-where-is>Source</a>
 *
 */
public abstract class JFXTest {
	@BeforeClass
	public static void initToolkit() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		SwingUtilities.invokeLater(() -> {
			new JFXPanel(); // initializes JavaFX environment
			latch.countDown();
		});
		
		Platform.setImplicitExit(false);

		// That's a pretty reasonable delay... Right?
		if (!latch.await(5L, TimeUnit.SECONDS))
			throw new ExceptionInInitializerError();
	}
}