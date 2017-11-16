package cl.cym.android.base.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class InternetUtil {

	public final static boolean isInternetOn(Activity actividad) {

		// get Connectivity Manager object to check connection
		/*
		 * ConnectivityManager connec = (ConnectivityManager)actividad.getSystemService(actividad.getBaseContext().CONNECTIVITY_SERVICE);
		 */

		ConnectivityManager connec = (ConnectivityManager) actividad
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Check for network connections
		if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

			// if connected with internet

			// Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
			return true;

		} else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

			// Toast.makeText(this, " Not Connected ",
			// Toast.LENGTH_LONG).show();
			return false;
		}
		return false;
	}

}
