package com.example.jaiguruji.weatherlistappp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by JaiGuruji on 09/02/17.
 */

public class HelperFunctions {

    static boolean isInternetConnected(Context contrext){
        ConnectivityManager connectivityManager = (ConnectivityManager)contrext.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  networkInfo.isConnectedOrConnecting();
    }

}
