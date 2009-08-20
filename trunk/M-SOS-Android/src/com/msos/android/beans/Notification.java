package com.msos.android.beans;

/**
 * User notification profil
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class Notification {

	/** User firstname */
	private String sms;
	
	/** User lastname */
	private String twitter;

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	
}
