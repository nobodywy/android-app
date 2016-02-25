package com.example.administrator.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/2/22.
 */
public class SelectBlueToothDeviceActivity extends Activity {

    private Button searchButton;
    BlueToothInitial blueToothInitial = new BlueToothInitial();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectbuletoothavtivity);

        blueToothInitial.initial();//蓝牙初始化

        final Context context = getBaseContext();

        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                blueToothInitial.getPairedDevice();
                List<BlueToothInitial.Bluetoothdevice> listdevice= blueToothInitial.getBluetoothdeviceslist();

                if(listdevice != null){
                    ListView listView = (ListView)findViewById(R.id.list_item);
                    listView.setAdapter(new BlueToothInitial.mdeviceAdapter(context,listdevice));
                }
            }
        });

        searchButton = (Button)findViewById(R.id.button5);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blueToothInitial.setBluetoothdeviceSearchList(new ArrayList<BlueToothInitial.Bluetoothdevice>());
                Log.v("tag","startDiscovery");
                if(blueToothInitial.bluetoothAdapter.isDiscovering()){
                    blueToothInitial.bluetoothAdapter.cancelDiscovery();
                }
                blueToothInitial.bluetoothAdapter.startDiscovery();

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(blueToothInitial.mreceiver,filter);

                filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                registerReceiver(blueToothInitial.mreceiver,filter);

                List<BlueToothInitial.Bluetoothdevice> listSearchDevice = blueToothInitial.getBluetoothdeviceSearchList();



                ListView listSearchView = (ListView)findViewById(R.id.list_item_search);
                final BlueToothInitial.mdeviceAdapter mAdapter = new BlueToothInitial.mdeviceAdapter(context,listSearchDevice);
                listSearchView.setAdapter(mAdapter);

                blueToothInitial.mhandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        switch (msg.what){
                            case 13:
                                mAdapter.notifyDataSetChanged();
                                Log.v("tag","start update");
                                break;
                            default:
                                super.handleMessage(msg);
                                break;
                        }
                    }
                };

                listSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.v("tag","点击");
                    }
                });

            }
        });





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(blueToothInitial.mreceiver);
    }


}
