package com.example.administrator.myapplication;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/2/29.
 */
public class BluttoothSendData {


    private static OutputStream outputStream = null;
    public void sendData(String getdata){
        try {
            byte[] data = getdata.getBytes("gbk");
            outputStream.write(data,0,data.length);
            outputStream.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
