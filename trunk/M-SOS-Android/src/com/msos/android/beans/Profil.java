package com.msos.android.beans;

/**
 * The User profil
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class Profil {

	/** User firstname */
	private String firstname;
	
	/** User lastname */
	private String lastname;
	
	/** User phone number */
	private String phoneNumber;
	
	/** User postal code */
	private String postalCode;
	
	/** User blood group */
	private String bloodGroup;

	
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}
	
	
	
}
