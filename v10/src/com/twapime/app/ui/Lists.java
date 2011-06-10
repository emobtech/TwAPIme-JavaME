package com.twapime.app.ui;

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
import com.twitterapime.rest.ListManager;
import com.twitterapime.rest.UserAccountManager;

public class Lists implements CommandListener {
	/**
	 * 
	 */
	private List listUI;
	
	/**
	 * 
	 */
	private Command select;
	
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
	 * 
	 */
	private com.twitterapime.rest.List[] lists;
	
	/**
	 * @param userManager
	 * @param backScreen
	 * @param midlet
	 */
	public Lists(UserAccountManager userManager, Displayable backScreen,
		MIDlet midlet) {
		this.userManager = userManager;
		this.midlet = midlet;
		this.backScreen = backScreen;
		//
		listUI = new List("Lists", List.IMPLICIT);
		//
		select = new Command("Select", Command.OK, 0);
		back = new Command("Back", Command.BACK, 0);
		//
		listUI.addCommand(select);
		listUI.addCommand(back);
		listUI.setCommandListener(this);
	}
	
	/**
	 * 
	 */
	public void show() {
		Display.getDisplay(midlet).setCurrent(listUI);
		//
		new Thread() {
			public void run() {
				fetchLists();
			};
		}.start();
		//
		Tracker tracker = Tracker.getInstance("UA-3518295-5");
		tracker.addToQueue(new PageView("Lists3", "lists32.html"));
	}

	/**
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == back) {
			Display.getDisplay(midlet).setCurrent(backScreen);
		} else if (c == select) {
			listTweets();
		}
	}
	
	/**
	 * 
	 */
	public void fetchLists() {
		ListManager lstMngr = ListManager.getInstance(userManager);
		//
		try {
			showAlert("Fetching lists...", AlertType.INFO, listUI);
			//
			lists = lstMngr.getLists();
			//
			for (int i = 0; i < lists.length; i++) {
				listUI.append(lists[i].getString(MetadataSet.LIST_NAME), null);
			}
			//
			showAlert("Lists fetched!", AlertType.INFO, listUI);
		} catch (Exception e) {
			showAlert(e.getMessage(), AlertType.WARNING, listUI);
		}
	}
	
	/**
	 * 
	 */
	public void listTweets() {
		com.twapime.app.ui.Timeline ui =
			new com.twapime.app.ui.Timeline("Tweets of List", listUI, midlet);
		ui.show();
		//
		ListManager t = ListManager.getInstance(userManager);
		t.startGetListTweets(lists[listUI.getSelectedIndex()], null, ui);
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
