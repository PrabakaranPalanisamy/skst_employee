package com.mazenet.mzs119.skst.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

	private Context context;

	public ConnectionDetector(Context context) {
		this.context = context;
	}

	public boolean isConnectedToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null)
				if (info != null)
					for (int i = 0; i < 1; i++)
						if (info.getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
		}
		return false;
	}
}