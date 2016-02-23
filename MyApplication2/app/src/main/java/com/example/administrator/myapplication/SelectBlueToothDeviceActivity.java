package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import java.util.List;


/**
 * Created by Administrator on 2016/2/22.
 */
public class SelectBlueToothDeviceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectbuletoothavtivity);

        Runnable getdevice = new Runnable() {
            @Override
            public void run() {
                BlueToothInitial blueToothInitial = new BlueToothInitial();
                List<BlueToothInitial.Bluetoothdevice> listdevice= blueToothInitial.getBluetoothdeviceslist();

                ListView listView = (ListView)findViewById(R.id.list_item);
                listView.setAdapter(new BlueToothInitial.mdeviceAdapter(this,listdevice));
            }
        };
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                BlueToothInitial blueToothInitial = new BlueToothInitial();
                List<BlueToothInitial.Bluetoothdevice> listdevice= blueToothInitial.getBluetoothdeviceslist();

                ListView listView = (ListView)findViewById(R.id.list_item);
                listView.setAdapter(new BlueToothInitial.mdeviceAdapter(this,listdevice));
            }
        });
        new Thread(getdevice).run();

    }
}
