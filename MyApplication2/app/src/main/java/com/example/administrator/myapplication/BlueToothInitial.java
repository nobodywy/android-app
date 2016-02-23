package com.example.administrator.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/2/13.
 */
public class BlueToothInitial {

    boolean sup_blooth = true;//该设备是否支持蓝牙
    boolean open_blooth = false;//该设备是否打开蓝牙
    BluetoothAdapter bluetoothAdapter;
    public void initial (){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            //提示该设备不支持蓝牙
            sup_blooth = false;
        }
        if(bluetoothAdapter.isEnabled()){
            open_blooth = true;
        }
    }

    public void getPairedDevice(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        //ArrayAdapter bluetoothadpater = new ArrayAdapter();
        if(pairedDevices.size()>0){
            for(BluetoothDevice device : pairedDevices){
               bluetoothdeviceslist.add(new Bluetoothdevice(device.getName(),device.getAddress()));
            }
        }
    }

    //获得蓝牙设备信息并显示
    public class Bluetoothdevice {
        private String deviceName;
        private String deviceAderess;

        public Bluetoothdevice(String deviceName, String deviceAderess) {
            this.deviceName = deviceName;
            this.deviceAderess = deviceAderess;
        }
    }


    public List<Bluetoothdevice> getBluetoothdeviceslist() {
        getPairedDevice();
        return bluetoothdeviceslist;
    }

    List<Bluetoothdevice> bluetoothdeviceslist = new ArrayList<Bluetoothdevice>();

    public static class mdeviceAdapter extends BaseAdapter{

        private List<Bluetoothdevice> mlist;
        private LayoutInflater mlayoutInflater;

        public mdeviceAdapter(Context context , List<Bluetoothdevice> list){
            mlist = list;
            mlayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mlayoutInflater.inflate(R.layout.item,null);
            TextView devicename = (TextView)view.findViewById(R.id.textView6);
            TextView deviceadress = (TextView)view.findViewById(R.id.textView7);

            Bluetoothdevice bean =mlist.get(position);
            devicename.setText(bean.deviceName);
            deviceadress.setText(bean.deviceAderess);
            return view;
        }
    }


}
