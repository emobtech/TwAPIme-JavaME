package com.twapime.app.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

import com.emobtech.googleanalyticsme.Event;
import com.emobtech.googleanalyticsme.PageView;
import com.emobtech.googleanalyticsme.Tracker;
import com.twitterapime.rest.TweetER;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.Tweet;

public class NewTweet implements CommandListener {
	/**
	 * 
	 */
	private TextBox text;
	
	/**
	 * 
	 */
	private Command post;
	
	/**
	 * 
	 */
	private Command clear;
	
	/**
	 * 
	 */
	private Command back;
	
	/**
	 * 
	 */
	private Displayable backScreen;
	
	/**
	 * 
	 */
	private MIDlet midlet;
	
	/**
	 * 
	 */
	private UserAccountManager userManager;
	
	/**
	 * @param userManager
	 * @param backScreen
	 * @param midlet
	 */
	public NewTweet(UserAccountManager userManager, Displayable backScreen,
		MIDlet midlet) {
		this.userManager = userManager;
		this.midlet = midlet;
		this.backScreen = backScreen;
		//
		text = new TextBox("New Tweet", "", 140, TextField.ANY);
		//
		post = new Command("Post", Command.OK, 0);
		clear = new Command("Clear", Command.SCREEN, 0);
		back = new Command("Back", Command.BACK, 0);
		//
		text.addCommand(post);
		text.addCommand(clear);
		text.addCommand(back);
		text.setCommandListener(this);
	}
	
	/**
	 * 
	 */
	public void show() {
		Display.getDisplay(midlet).setCurrent(text);
		//
		Tracker tracker = Tracker.getInstance("UA-3518295-5");
		tracker.addToQueue(new PageView("New Tweet3", "/newtweet32.html"));
	}

	/**
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == clear) {
			text.setString("");
			//
			Tracker tracker = Tracker.getInstance("UA-3518295-5");
			tracker.addToQueue(new Event("Tweet", "Clear Tweet", null, null));
		} else if (c == back) {
			Display.getDisplay(midlet).setCurrent(backScreen);
		} else if (c == post) {
			new Thread() {
				public void run() {
					post();
				};
			}.start();
			//
			Tracker tracker = Tracker.getInstance("UA-3518295-5");
			tracker.addToQueue(new Event("Tweet", "Send Tweet", null, null));
		}
	}
	
	/**
	 * 
	 */
	public void post() {
		TweetER t = TweetER.getInstance(userManager);
		//
		try {
			Tweet tweet = new Tweet(text.getString());
			//
			showAlert("Posting...", AlertType.INFO, text);
			//
			tweet = t.post(tweet);
			//
			showAlert("Tweet posted!", AlertType.INFO, backScreen);
		} catch (Exception e) {
			showAlert(e.getMessage(), AlertType.WARNING, text);
		}
	}
	
	/**
	 * @param message
	 * @param type
	 * @param backScreen
	 */
	public void showAlert(String message, AlertType type,
		Displayable backScreen) {
		Alert alert = new Alert("TwAPIme", message, null, type);
		alert.setTimeout(Alert.FOREVER);
		//
		Display.getDisplay(midlet).setCurrent(alert, backScreen);
	}
}
