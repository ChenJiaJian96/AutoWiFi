package com.jiajianchen.autowifi;

import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.TreeMap;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private SimpleDraweeView mConnectBt;
    private EditText mAccountEv;
    private EditText mPasswordEv;
    private CheckBox mRememberCb;

    private boolean isAirModeOn = false;

    private WifiAdmin wifiAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProject();
        setContentView(R.layout.activity_main);

        initView();
        initData();

//        wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("XXX", "XXX", 3));
    }

    private void initProject() {
        Fresco.initialize(this);
    }

    private void initView() {
        mConnectBt = findViewById(R.id.connect_icon);
        mAccountEv = findViewById(R.id.account_edit);
        mPasswordEv = findViewById(R.id.pw_edit);
        mRememberCb = findViewById(R.id.remember_cb);
    }

    private void initData() {
        wifiAdmin = new WifiAdmin(this);
        mConnectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestConnect();
            }
        });
    }

    /**
     * 请求连接
     * 打开WiFi -> 查询校园网WiFi -> 连接校园网 -> 实现portal验证
     */
    private void requestConnect() {
        if (isNeededOpenWifi()) {
            Log.d(TAG, "requestConnect: Open WiFi Now !");
            wifiAdmin.openWifi();
            Log.d(TAG, "requestConnect: Open WiFi Successfully.");
        }
        if (wifiAdmin.checkState() == WIFI_STATE_ENABLED) {
            if (isCampusNetworkExisted()) {
                Log.d(TAG, "requestConnect: Campus-Network exists.");
                requestConnectToCampus();
            } else {
                Log.d(TAG, "requestConnect: Campus-Network not exists.");
            }
        }
    }

    /**
     * 判断是否需要直接打开WiFi
     */
    private boolean isNeededOpenWifi() {
        Log.d(TAG, "Checking network state.");
        if (!NetWorkUtils.isNetworkConnected(this)) {
            Log.d(TAG, "Networks are off.");
            // If is airplane-mode
            if (NetWorkUtils.IsAirModeOn(this)) {
                isAirModeOn = true;
                Log.d(TAG, "Airplane-mode on.");
                // dialog ask
                askIsNeededCloseAM();
                return !isAirModeOn;
            } else {
                Log.d(TAG, "Airplane-mode off");
                // can open wifi directly
                return true;
            }
        } else {
            Log.d(TAG, "Wifi or 3G is on.");
            return true;
        }
    }

    /**
     * 询问是否需要关闭飞行模式(Airplane mode)
     */
    private void askIsNeededCloseAM() {
        String mTitle = "检测到当前开启了飞行模式\n是否允许关闭飞行模式并开启WIFI？";
        ConfirmOrCancelDialog.Builder builder = new ConfirmOrCancelDialog.Builder(this, mTitle);
        builder.setClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case BUTTON_POSITIVE:
                        NetWorkUtils.requestCloseAM(MainActivity.this);
                        isAirModeOn = false;
                        break;
                    case BUTTON_NEGATIVE:
                        isAirModeOn = true;
                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * 判断所在范围内是否存在本校园网
     */
    private boolean isCampusNetworkExisted() {
        wifiAdmin.startScan();
        List<ScanResult> mScanResultList = wifiAdmin.getWifiList();
        sortList(mScanResultList);
        Log.d(TAG, "isCampusNetworkExisted: " + mScanResultList);
        return true;
    }

    /**
     * 请求连接至校园网
     */
    private void requestConnectToCampus() {
    }


    private void sortList(List<ScanResult> list) {
        TreeMap<String, ScanResult> map = new TreeMap<>();
        // sort by SSID
        for (ScanResult mScanResult : list)
            map.put(mScanResult.SSID, mScanResult);
        list.clear();
        list.addAll(map.values());
    }
}
