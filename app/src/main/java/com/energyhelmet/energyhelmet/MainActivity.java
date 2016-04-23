package com.energyhelmet.energyhelmet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "com.energyhelmet.energyhelmet.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Bean> beans = new ArrayList<>();

        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {
                beans.add(bean);
            }

            @Override
            public void onDiscoveryComplete() {
                for (Bean bean : beans) {
                    Log.w(TAG, bean.getDevice().getName());       // "Bean"              (example)
                    Log.w(TAG, bean.getDevice().getAddress());    // "B4:99:4C:1E:BC:75" (example)
                }
            }
        };

        BeanManager.getInstance().startDiscovery(listener);
    }
}
