package com.msos.android.typesafeenum;

/**
 * The Alert types
 * 
 * @author Ludovic Toinel
 */
public class AlertType {

	/** Alert types */
	public static AlertType ALERT_ACCIDENT = new AlertType(1);
	public static AlertType ALERT_DOMESTIC = new AlertType(2);
	public static AlertType ALERT_FIRE = new AlertType(3);
	public static AlertType ALERT_SANTE = new AlertType(4);
	
	private int value;
	
	/** Private constructor */
	private AlertType(int value){
		this.value = value;
	}
	
	/**
	 * @return the TypeSafeEnum value
	 */
	public int getValue(){
		return value;
	}

}
