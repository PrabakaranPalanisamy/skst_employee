package com.mazenet.mzs119.skst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by MZS119 on 3/31/2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "NetworkChangeReceiver";
    private boolean isConnected = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Receieved notification about network status");
        isNetworkAvailable(context);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {

                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            Log.v(LOG_TAG, "Now you are connected to Internet!");
                            Toast.makeText(context, "You are Online", Toast.LENGTH_SHORT).show();
                            isConnected = true;
                            // do your processing here ---
                            // if you need to post any data to the server or get
                            // status
                            // update from the server
                        }
                        return true;

                }
            }
        }
        Log.v(LOG_TAG, "You are not connected to Internet!");
        Toast.makeText(context, "You are Offline!", Toast.LENGTH_SHORT).show();
        isConnected = false;
        return false;
    }


}