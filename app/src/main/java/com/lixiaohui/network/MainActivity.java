package com.lixiaohui.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.lixiaohui.network.sdk.NetworkManager;
import com.lixiaohui.network.sdk.features.annotation.Network;
import com.lixiaohui.network.sdk.features.NetType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkManager.getInstance().registerObserver(this);
    }

    @Network(netType = NetType.AUTO)
    public void aaa(NetType netType) {
        Log.e("lixiaohui",netType.toString());
        switch (netType) {
            case WIFI:
                Toast.makeText(this,"连接WIFI",Toast.LENGTH_SHORT).show();
                break;
            case AUTO:
                Toast.makeText(this,"网络有效",Toast.LENGTH_SHORT).show();
                break;
            case CMWAP:
                Toast.makeText(this,"连接数据网络",Toast.LENGTH_SHORT).show();
                break;
            case DISABLE:
                Toast.makeText(this,"网络无效",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"错误了",Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
