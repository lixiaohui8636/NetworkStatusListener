package com.lixiaohui.network;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lixiaohui.network.sdk.annotation.Network;
import com.lixiaohui.network.sdk.type.NetType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Network(netType = NetType.AUTO)
    public void aaa(NetType netType){

    }


}
