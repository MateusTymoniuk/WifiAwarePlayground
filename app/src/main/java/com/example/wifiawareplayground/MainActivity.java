package com.example.wifiawareplayground;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Handler handler;
    private WifiAwareHelper mWifiAwareHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        mWifiAwareHelper = new WifiAwareHelper(this);
        mWifiAwareHelper.connect();
        handler = new Handler(Looper.getMainLooper());
    }

    public WifiAwareHelper getWifiAwareHelper() {
        return mWifiAwareHelper;
    }

    public Handler getHandler() {
        return handler;
    }
}