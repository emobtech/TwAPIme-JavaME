package com.twapime.app.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;

import com.emobtech.googleanalyticsme.Event;
import com.emobtech.googleanalyticsme.PageView;
import com.emobtech.googleanalyticsme.Tracker;
import com.twitterapime.rest.Credential;
import com.twitterapime.rest.Timeline;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.xauth.Token;

/**
 * @author Ernandes Jr
 *
 */
public class MainMenu implements CommandListener {
	/**
	 * 
	 */
	private List menu;
	
	/**
	 * 
	 */
	private Command exit;
	
	/**
	 * 
	 */
	private Command select;
	
	/**
	 * 
	 */
	private MIDlet midlet;
	
	/**
	 * 
	 */
	private UserAccountManager userManager;
	
	/**
	 * 
	 */
	public MainMenu(MIDlet midlet) {
		this.midlet = midlet;
		//
		menu = new List("TwAPIme", List.IMPLICIT);
		//
		menu.append("New Tweet", null);
		menu.append("Timeline", null);
		menu.append("Mentions", null);
		menu.append("Direct Messages", null);
		menu.append("Retweets by Me", null);
		menu.append("Retweets To Me", null);
		menu.append("My Lists", null);
		//
		select = new Command("Select", Command.OK, 0);
		exit = new Command("Exit", Command.EXIT, 0);
		//
		menu.addCommand(select);
		menu.addCommand(exit);
		menu.setCommandListener(this);
		//
		Token tk = new Token("<your token here>", "<your token secret here>");
		Credential c = new Credential("<your username here>", "<your consumer key here>", "<your consumer secret here>", tk);
		//
		userManager = UserAccountManager.getInstance(c);
		//
		try {
			userManager.verifyCredential();
		} catch (Exception e) {
		}
	}
	
	/**
	 * 
	 */
	public void show() {
		Display.getDisplay(midlet).setCurrent(menu);
		//
		Tracker tracker = Tracker.getInstance("UA-3518295-5", 10);
		tracker.addToQueue(new PageView("/mainmenu3"));
		tracker.addToQueue(new Event("Main Menu2", "Show2", "display2", null));
		tracker.addToQueue(new Event("Main Menu2", "Show2", "play2", new Integer(10)));
	}

	/**
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == exit) {
			Tracker tracker = Tracker.getInstance("UA-3518295-5");
			tracker.flush(false);
			//
			midlet.notifyDestroyed();
		} else if (c == select) {
			switch (menu.getSelectedIndex()) {
				case 0:
					new NewTweet(userManager, menu, midlet).show(); break;
				case 1:
					timeline(); break;
				case 2:
					mentions(); break;
				case 3:
					directMessages(); break;
				case 4:
					retweetsByMe(); break;
				case 5:
					retweetsToMe(); break;
				case 6:
					new Lists(userManager, menu, midlet).show(); break;
			}
		}
	}
	
	/**
	 * 
	 */
	public void timeline() {
		com.twapime.app.ui.Timeline ui =
			new com.twapime.app.ui.Timeline("Timeline", menu, midlet);
		ui.show();
		//
		Timeline t = Timeline.getInstance(userManager);
		t.startGetHomeTweets(null, ui);
	}

	/**
	 * 
	 */
	public void mentions() {
		com.twapime.app.ui.Timeline ui =
			new com.twapime.app.ui.Timeline("Mentions", menu, midlet);
		ui.show();
		//
		Timeline t = Timeline.getInstance(userManager);
		t.startGetMentions(null, ui);
	}
	
	/**
	 * 
	 */
	public void directMessages() {
		com.twapime.app.ui.Timeline ui =
			new com.twapime.app.ui.Timeline("Direct Messages", menu, midlet);
		ui.show();
		//
		Timeline t = Timeline.getInstance(userManager);
		t.startGetDirectMessages(null, true, ui);
	}
	
	/**
	 * 
	 */
	public void retweetsByMe() {
		com.twapime.app.ui.Timeline ui =
			new com.twapime.app.ui.Timeline("Retweets by Me", menu, midlet);
		ui.show();
		//
		Timeline t = Timeline.getInstance(userManager);
		t.startGetRetweetsByMe(null, ui);
	}

	/**
	 * 
	 */
	public void retweetsToMe() {
		com.twapime.app.ui.Timeline ui =
			new com.twapime.app.ui.Timeline("Retweets to Me", menu, midlet);
		ui.show();
		//
		Timeline t = Timeline.getInstance(userManager);
		t.startGetRetweetsToMe(null, ui);
	}
}
