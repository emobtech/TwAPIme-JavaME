package com.twapime.app.midlet;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.twapime.app.ui.MainMenu;

/**
 * @author Ernandes Jr
 *
 */
public class TwAPImeMIDlet extends MIDlet {
	/**
	 * 
	 */
	private MainMenu mainMenu;
	
	/**
	 * 
	 */
	public TwAPImeMIDlet() {
		mainMenu = new MainMenu(this);
	}

	/**
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean unconditional) {
	}

	/**
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {
	}

	/**
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		mainMenu.show();
	}
}
