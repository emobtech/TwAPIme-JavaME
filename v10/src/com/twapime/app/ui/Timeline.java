package com.twapime.app.ui;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;

import com.emobtech.googleanalyticsme.PageView;
import com.emobtech.googleanalyticsme.Tracker;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;

public class Timeline implements CommandListener, SearchDeviceListener {
	/**
	 * 
	 */
	private List tweetList;
	
	/**
	 * 
	 */
	private Command read;

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
	private Vector tweets;
	
	/**
	 * @param title
	 * @param backScreen
	 * @param midlet
	 */
	public Timeline(String title, Displayable backScreen, MIDlet midlet) {
		this.midlet = midlet;
		this.backScreen = backScreen;
		tweets = new Vector(20);
		//
		tweetList = new List(title, List.IMPLICIT);
		tweetList.append("Loading...", null);
		//
		read = new Command("Read", Command.BACK, 0);
		back = new Command("Back", Command.BACK, 0);
		tweetList.addCommand(read);
		tweetList.addCommand(back);
		tweetList.setCommandListener(this);
	}
	
	/**
	 * 
	 */
	public void show() {
		Display.getDisplay(midlet).setCurrent(tweetList);
		//
		Tracker tracker = Tracker.getInstance("UA-3518295-5");
		tracker.addToQueue(new PageView("Timeline3", "/timeline3.html"));
	}

	public void commandAction(Command c, Displayable d) {
		if (c == back) {
			Display.getDisplay(midlet).setCurrent(backScreen);
		} else if (c == read) {
			read();
		}
	}
	
	/**
	 * 
	 */
	public void read() {
		Tweet[] ts = new Tweet[tweets.size()];
		tweets.copyInto(ts);
		//
		new ReadTweet(ts, tweetList, midlet).show();
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchCompleted()
	 */
	public void searchCompleted() {
		tweetList.delete(0);
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchFailed(java.lang.Throwable)
	 */
	public void searchFailed(Throwable exception) {
		tweetList.delete(0);
		//
		Alert alert =
			new Alert(
				"TwAPIme", exception.getMessage(), null, AlertType.WARNING);
		alert.setTimeout(Alert.FOREVER);
		//
		Display.getDisplay(midlet).setCurrent(alert, tweetList);
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#tweetFound(com.twitterapime.search.Tweet)
	 */
	public void tweetFound(Tweet tweet) {
		tweetList.append(
			tweet.getUserAccount().getString(MetadataSet.USERACCOUNT_USER_NAME) +
				": " +
				tweet.getString(MetadataSet.TWEET_CONTENT),
			null);
		//
		tweets.addElement(tweet);
	}
}
