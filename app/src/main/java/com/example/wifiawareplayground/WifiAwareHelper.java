package com.example.wifiawareplayground;

import android.content.Context;
import android.net.wifi.aware.AttachCallback;
import android.net.wifi.aware.WifiAwareManager;
import android.net.wifi.aware.WifiAwareSession;
import android.util.Log;

public class WifiAwareHelper {
    public static final String SERVICE_NAME = "Teste-Service-Name";
    private static final String TAG = "WifiAwareHelper";
    private WifiAwareManager mWifiAwareManager;
    private WifiAwareSession mWifiAwareSession;

    public WifiAwareHelper(Context context) {
        mWifiAwareManager = (WifiAwareManager) context.getSystemService(Context.WIFI_AWARE_SERVICE);
    }

    public WifiAwareSession getWifiAwareSession() {
        return mWifiAwareSession;
    }

    public void connect() {
        if (mWifiAwareManager != null) {
            mWifiAwareManager.attach(new AttachCallback() {
                @Override
                public void onAttached(WifiAwareSession session) {
                    mWifiAwareSession = session;
                    // TODO: Handle session creation
                }

                @Override
                public void onAttachFailed() {
                    // TODO: Handle attach failure
                    Log.e(TAG, "onAttachFailed");
                }
            }, null);
        }
    }
}
