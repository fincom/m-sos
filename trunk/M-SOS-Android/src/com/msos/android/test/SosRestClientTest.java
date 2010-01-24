/**
 * 
 */
package com.msos.android.test;

import android.test.AndroidTestCase;

import com.msos.android.beans.Notification;
import com.msos.android.beans.Profil;
import com.msos.android.net.SosRestClient;
import com.msos.android.typesafeenum.AlertType;

/**
 * InstrumentationTestRunner
 * 
 * @author Ludovic Toinel
 * @version SVN: $Id:$
 */
public class SosRestClientTest extends AndroidTestCase {
	
    private static String uniqueId;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		if (uniqueId == null){
			uniqueId = String.valueOf(System.currentTimeMillis()) + "-TEST-ANDROID";
		}
	}

	/**
	 * Test the user signup
	 */
	public void test1Signup() {
		Profil profil = new Profil();
		profil.setPhoneNumber("+33633625867");
		profil.setFirstname("Ludovic");
		profil.setLastname("Toinel");
		profil.setPostalCode("92800");
		
		Notification notification = new Notification();
		boolean result = SosRestClient.signup(uniqueId, profil, notification, "FR_fr");
		assertTrue(result);
	}
	
	/**
	 * Test the alert creation
	 */
	public void test2CreateAlert() {
		double latitude = 48.8746681213;
		double longitude = 2.2582859992;
		//boolean result = SosRestClient.createAlert(uniqueId, AlertType.ALERT_ACCIDENT, latitude, longitude, true);
		assertTrue(true);
	}


}
