package com.twapime.app.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

import com.emobtech.googleanalyticsme.PageView;
import com.emobtech.googleanalyticsme.Tracker;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.Tweet;

public class ReadTweet implements CommandListener {
	/**
	 * 
	 */
	private TextBox text;
	
	/**
	 * 
	 */
	private Command next;
	
	/**
	 * 
	 */
	private Command prev;
	
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
	private Tweet[] tweets;
	
	/**
	 * 
	 */
	private int cursor;
	
	/**
	 * @param tweets
	 * @param backScreen
	 * @param midlet
	 */
	public ReadTweet(Tweet[] tweets, Displayable backScreen, MIDlet midlet) {
		this.tweets = tweets;
		this.midlet = midlet;
		this.backScreen = backScreen;
		//
		text = new TextBox("", "", 140, TextField.ANY);
		//
		next = new Command("Next", Command.OK, 0);
		prev = new Command("Prev", Command.SCREEN, 0);
		back = new Command("Back", Command.BACK, 0);
		//
		text.addCommand(next);
		text.addCommand(prev);
		text.addCommand(back);
		text.setCommandListener(this);
		//
		next();
	}
	
	/**
	 * 
	 */
	public void show() {
		Display.getDisplay(midlet).setCurrent(text);
		//
		Tracker tracker = Tracker.getInstance("UA-3518295-5");
		tracker.addToQueue(new PageView("Read Tweet3", "/readtweet3.html"));
	}
	
	/**
	 * 
	 */
	public void next() {
		if (cursor +1 < tweets.length) {
			cursor++;
			load();
		}
	}

	/**
	 * 
	 */
	public void prev() {
		if (cursor > 0) {
			cursor--;
			load();
		}
	}
	
	/**
	 * 
	 */
	public void load() {
		text.setTitle(cursor + ": " + tweets[cursor].getUserAccount().getString(MetadataSet.USERACCOUNT_USER_NAME));
		text.setString(tweets[cursor].getString(MetadataSet.TWEET_CONTENT));
	}

	/**
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == prev) {
			prev();
		} else if (c == back) {
			Display.getDisplay(midlet).setCurrent(backScreen);
		} else if (c == next) {
			next();
		}
	}
}
