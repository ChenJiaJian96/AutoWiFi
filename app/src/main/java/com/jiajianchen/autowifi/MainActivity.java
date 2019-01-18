package com.jiajianchen.autowifi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiAdmin wifiAdmin = new WifiAdmin(this);
        wifiAdmin.openWifi();
        wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("XXX", "XXX", 3));
    }
}
