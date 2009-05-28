package com.msos.android.typesafeenum;

/**
 * The Alert types
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class AlertType {

	/** Alert types */
	public static AlertType ALERT_ACCIDENT = new AlertType("ACCIDENT");
	public static AlertType ALERT_DOMESTIC = new AlertType("DOMESTIC");
	public static AlertType ALERT_FIRE = new AlertType("FIRE");
	public static AlertType ALERT_SANTE = new AlertType("SANTE");
	
	private String value;
	
	/** Private constructor */
	private AlertType(String value){
		this.value = value;
	}
	
	/**
	 * @return the TypeSafeEnum value
	 */
	public String getValue(){
		return value;
	}

}
