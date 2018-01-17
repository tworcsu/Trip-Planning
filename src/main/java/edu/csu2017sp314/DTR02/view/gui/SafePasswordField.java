package edu.csu2017sp314.DTR02.view.gui;

import java.lang.reflect.Field;

import javafx.scene.control.PasswordField;

/**
 * The base JavaFX PasswordField uses Strings internally. It's annoying, but
 * potentially important, to bypass this flaw. 3ph3r on Stack Overflow wrote a
 * class which does exactly that.
 * 
 * @author 3ph3r from Stack Overflow
 * @see <a
 *      href=http://stackoverflow.com/questions/29368926/safe-way-to-get-password-from-passwordfield-in-javafx>Source</a>
 *
 */
public class SafePasswordField extends PasswordField {

    public final char[] getPassword() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Content c = getContent();

        Field fld = c.getClass().getDeclaredField("characters");
        fld.setAccessible(true);

        StringBuilder sb = (StringBuilder) fld.get(c);
        char[] result = new char[sb.length()];
        sb.getChars(0, sb.length(), result, 0);

        return result;
    }
}