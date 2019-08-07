package com.lixiaohui.network.sdk.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lixiaohui.network.sdk.type.NetType;

/**
 * @Author: Lee
 * @Date: 2019-08-07
 * @Desc:
 */
public class NetworkUtils {

    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager == null){
            return false;
        }
        NetworkInfo[] infos = manager.getAllNetworkInfo();
        if(infos != null){
            for (NetworkInfo info : infos) {
                if(info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }

    public static NetType getNetType(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager == null){
            return NetType.NONE;
        }
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo == null){
            return NetType.NONE;
        }

        int type = networkInfo.getType();
        if(type == ConnectivityManager.TYPE_MOBILE){
            if("cmnet".equals(networkInfo.getExtraInfo().toLowerCase())){
                return NetType.CMNET;
            }else{
                return NetType.CMWAP;
            }
        }else if(type == ConnectivityManager.TYPE_WIFI){
            return NetType.WIFI;
        }
        return NetType.NONE;
    }


    public static void openNetworkSetting(Activity activity, int requestCode){
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent,requestCode);
    }
}
