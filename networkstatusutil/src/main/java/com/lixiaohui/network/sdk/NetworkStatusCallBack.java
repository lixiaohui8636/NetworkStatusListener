package com.lixiaohui.network.sdk;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

/**
 * @Author: Lee
 * @Date: 2019-08-07
 * @Desc:
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkStatusCallBack extends ConnectivityManager.NetworkCallback {

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);

    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ){
            if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                //wifi
            }else{
                //qita
            }
        }
    }
}
