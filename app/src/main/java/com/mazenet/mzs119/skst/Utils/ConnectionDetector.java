package com.mazenet.mzs119.skst.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

public class ConnectionDetector {

    private Context context;
    private boolean isConnected = false;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {

                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    if (!isConnected) {
//                        Toast.makeText(context, "You are Online", Toast.LENGTH_SHORT).show();
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

//        Toast.makeText(context, "You are Offline!", Toast.LENGTH_SHORT).show();
        isConnected = false;
        return false;
    }
//        ConnectivityManager connectivity = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null)
//                for (int i = 0; i < info.length; i++)
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//        }
//        return false;
}