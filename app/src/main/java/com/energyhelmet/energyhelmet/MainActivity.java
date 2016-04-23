package com.energyhelmet.energyhelmet;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.Acceleration;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "com.energyhelmet.energyhelmet.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView xField;
        final TextView yField;
        final TextView zField;

        final List<Bean> beans = new ArrayList<>();

        final Context self = this;

        xField = (TextView) findViewById(R.id.x_field);
        yField = (TextView) findViewById(R.id.y_field);
        zField = (TextView) findViewById(R.id.z_field);

        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {
                beans.add(bean);
            }

            @Override
            public void onDiscoveryComplete() {
                for (final Bean bean : beans) {
                    Log.w(TAG, bean.getDevice().getName());       // "Bean"              (example)
                    Log.w(TAG, bean.getDevice().getAddress());    // "B4:99:4C:1E:BC:75" (example)

                    BeanListener beanListener = new BeanListener() {
                        @Override
                        public void onConnected() {
                            Log.i(TAG, "connected to Bean!");
                            bean.readDeviceInfo(new Callback<DeviceInfo>() {
                                @Override
                                public void onResult(DeviceInfo deviceInfo) {
                                    Log.w(TAG, deviceInfo.hardwareVersion());
                                    Log.w(TAG, deviceInfo.firmwareVersion());
                                    Log.w(TAG, deviceInfo.softwareVersion());
                                }
                            });
                            bean.readAcceleration(new Callback<Acceleration>() {
                                @Override
                                public void onResult(Acceleration result) {
                                    xField.setText(Double.toString(result.x()));
                                    yField.setText(Double.toString(result.y()));
                                    zField.setText(Double.toString(result.z()));
                                }
                            });

                        }

                        @Override
                        public void onConnectionFailed() {

                        }

                        @Override
                        public void onDisconnected() {
                            Log.w(TAG, "Bean disconnected!");
                        }

                        @Override
                        public void onSerialMessageReceived(byte[] data) {

                        }

                        @Override
                        public void onScratchValueChanged(ScratchBank bank, byte[] value) {

                        }

                        @Override
                        public void onError(BeanError error) {

                        }

                        @Override
                        public void onReadRemoteRssi(int rssi) {

                        }
                    };

                    bean.connect(self, beanListener);
                }
            }
        };

        BeanManager.getInstance().startDiscovery(listener);
    }
}
