package com.example.administrator.myapplication;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.*;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.example.administrator.myapplication.JsonHandle;
import com.example.administrator.myapplication.BlueToothInitial;

public class MainActivity extends ActionBarActivity {

    private Button IdentifyiButton; //验证码
    private Button LoginButton;
    private EditText TextPhone;
    private EditText TextCode;
    private TextView textViewtest;
    private Button  BlueToothButton; //蓝牙测试按钮
    //private Handler handler;


    public class DataHttp { //封装了手机号，验证码，token用于message传递

        protected String phoneNumber = null;
        protected String identifyToken = null;
        protected String sms_code = null;
        protected String sms_token = null;
        protected String date = null;



        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setIdentifyToken(String identifyToken) {
            this.identifyToken = identifyToken;
        }

        public void setSms_code(String sms_code) {
            this.sms_code = sms_code;
        }

        public void setSms_token(String sms_token) {
            this.sms_token = sms_token;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getIdentifyToken() {
            return identifyToken;
        }

        public String getSms_code() {
            return sms_code;
        }

        public String getSms_token() {
            return sms_token;
        }

        public String getDate() {
            return date;
        }


    }
   // private int index = 0 ;//计时器索引
//
//    private MyRunable myRunable = new MyRunable();
//    class  MyRunable implements Runnable {
//        @Override
//        public void run() {
//            index ++;
//            index = index % 19;
//            textViewtest.setText(index + "");
//            IdentifyiButton.setText(index + "");
//            if (index == 0) {
//                handler.removeCallbacks(this);
//                IdentifyiButton.setText("验证码");
//                IdentifyiButton.setEnabled(true);
//            }
//            handler.postDelayed(this, 1000);
//        }
//    }





 class MyTimetask extends TimerTask{

     Timer timer = new Timer();
     private int index = 20;
        public void run(){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    index --;
                    IdentifyiButton.setText(index+"");
                    if(index == 0){
                        IdentifyiButton.setEnabled(true);
                        IdentifyiButton.setText("验证码");
                        timer.cancel();

                    }
                }
            });
        }
     private void startTask(){
         timer.schedule(this,1000,1000);
     }
 }
    /*private  void  setTimeTask(){
        new Timer().schedule(new myTimetask(),1000,1000);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IdentifyiButton = (Button)findViewById(R.id.button2);
        TextPhone = (EditText)findViewById(R.id.editText2);
        textViewtest = (TextView)findViewById(R.id.textView2);
        LoginButton = (Button)findViewById(R.id.button);
        TextCode = (EditText)findViewById(R.id.editText);
        BlueToothButton = (Button)findViewById(R.id.button4); //蓝牙


        /*
        蓝牙测试
         */
        BlueToothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BlueToothInitial blueToothInitial = new BlueToothInitial();
                blueToothInitial.initial();
                if(!blueToothInitial.sup_blooth){ //该设备不支持蓝牙
                    Log.v("tag","not support blooth");
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提醒")
                            .setMessage("该设备不支持蓝牙")
                            .setPositiveButton("确定",null)
                            .show();
                }
                if (!blueToothInitial.open_blooth){  //蓝牙没有打开
                    Log.v("tag","not open blooth");
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提醒")
                            .setMessage("蓝牙没有打开，是否打开？")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    startActivityForResult(enableIntent,1);
                                }
                            })
                            .show();
                }
                if (blueToothInitial.sup_blooth&&blueToothInitial.open_blooth){
                    Intent intent = new Intent(MainActivity.this,SelectBlueToothDeviceActivity.class);
                    startActivity(intent);
                }

            }
        });


        /*
                验证码操作
         */
        IdentifyiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1,校验手机号 2，http通信 3，保存token

                Runnable networktaskIdentify = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HTTPMethod(TextPhone.getText().toString() , 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                new Thread(networktaskIdentify).start();

                /*
                倒计时效果
                 */
                IdentifyiButton.setEnabled(false);
                final Runnable timetask = new Runnable() {
                    @Override
                    public void run() {
                        MyTimetask mytimetask = new MyTimetask();
                        mytimetask.startTask();
                    }
                };
                new Thread(timetask).start();
        }});
        //登录操作

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //校验验证码格式 2 http通信 3 解析、保存token
                Runnable networkTaskLogin = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HTTPMethod(TextCode.getText().toString() , 1);
                            Log.v("tag" ,"here: " + TextCode.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(networkTaskLogin).start();
                    LoginButton.setText("sd");


            }
        });
    }


    DataHttp dataHttp = new DataHttp();
    //                                      http通信
    public void HTTPMethod(String str , int t) throws JSONException {
        HttpClient httpClient = new DefaultHttpClient();
        if(t == 0){           //验证码http测试
            //boolean httpOk = false;
            HttpGet httpGet = new HttpGet("http://api.zaocan84.com/management/api/v1/managers/send_login_code.json?phone="+str);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    String content = EntityUtils.toString(response.getEntity());
                    JsonHandle jsonHandle = new JsonHandle();
                    content = jsonHandle.stringToJson(content);//验证码返回token
                    dataHttp.setPhoneNumber(str);
                    dataHttp.setIdentifyToken(content);
                    Message message = new Message();
                    message.what = 11;  //手机号api返回token的对象
                    message.obj = dataHttp;
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                httpGet.abort();
            }
        }else {

            Log.v("tag","identify: " + dataHttp.getIdentifyToken() + "data-phone: " + dataHttp.getPhoneNumber()
                + "textp[hone : " + TextPhone.getText().toString());
            //== 和 equals区别
            if(dataHttp.getIdentifyToken()!= null &&
                    dataHttp.getPhoneNumber().equals(TextPhone.getText().toString()) == true){
                Log.v("tag","enter http");
                HttpGet httpGet = new HttpGet(
                        "http://api.zaocan84.com/management/api/v1/managers/login_with_sms_code.json?"
                        + "phone=" + TextPhone.getText().toString()
                        + "&sms_code=" + str
                        + "&sms_token=" + dataHttp.getIdentifyToken()
                );
                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                        String content = EntityUtils.toString(response.getEntity());
                        JsonHandle jsonHandle = new JsonHandle();
                        content = jsonHandle.stringToJson(content);
                        dataHttp.setPhoneNumber(TextPhone.getText().toString());
                        dataHttp.setSms_token(content);
                        Message message = new Message();
                        message.what = 12;
                        message.obj = dataHttp;
                        handler.sendMessage(message);
                        Log.v("tag","enter try");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    httpGet.abort();
                }
            }
        }

    }



    private  Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 11:
                    dataHttp = (DataHttp) msg.obj;
                    textViewtest.setText("qqq" + dataHttp.getPhoneNumber() + dataHttp.getIdentifyToken());
                    break;
                case 12:
                    dataHttp =(DataHttp) msg.obj;
                    textViewtest.setText("www" + dataHttp.getPhoneNumber() + dataHttp.getSms_token());
                    Log.v("tag","sms-token: " + dataHttp.getSms_token() +
                            "phone-number: " + dataHttp.getPhoneNumber());
                    Intent intent = new Intent(MainActivity.this,ShowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Token",dataHttp.getSms_token());
                    bundle.putString("phoneNumber",dataHttp.getPhoneNumber());
                    intent.putExtras(bundle);
                    startActivity(intent);

                    finish();

                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }



        };
    };
}
