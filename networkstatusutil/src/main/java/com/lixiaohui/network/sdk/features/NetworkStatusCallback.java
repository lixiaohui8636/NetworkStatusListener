package com.lixiaohui.network.sdk.features;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.lixiaohui.network.sdk.NetworkManager;
import com.lixiaohui.network.sdk.utils.NetworkUtils;

/**
 * @Author: Lee
 * @Date: 2019-08-07
 * @Desc:
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkStatusCallback extends ConnectivityManager.NetworkCallback {

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        //网络有效
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        if(!NetworkUtils.isNetworkAvailable(NetworkManager.getInstance().getApplication())){
            ObserverManager.getInstance().post(NetType.DISABLE);
        }
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ){
            if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                //wifi
                ObserverManager.getInstance().post(NetType.WIFI);
            }else{
                //qita
                ObserverManager.getInstance().post(NetType.CMWAP);
            }
        }
    }
}
