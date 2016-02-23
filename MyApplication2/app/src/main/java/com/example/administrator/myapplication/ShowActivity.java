package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;


/**
 * Created by Administrator on 2016/2/1.
 */
public class ShowActivity extends Activity {

    private TextView textViewTest;
    private TextView textViewDate;
    private DatePicker datePicker;
    private  int dayofMonth = 0; //月份从0开始，用此表示真实月份
    private String dateOfOrder = null;
    private Button buttonDate;
    private String token;
//    private Handler handler = new Handler();
    private TextView textViewTestToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shwoactivity);

        datePicker = (DatePicker)findViewById(R.id.datePicker);
        textViewTest = (TextView)findViewById(R.id.textView3);
        textViewDate = (TextView)findViewById(R.id.textView4);
        buttonDate = (Button)findViewById(R.id.button3);
        textViewTestToken = (TextView)findViewById(R.id.textView5);

        Bundle bundle = this.getIntent().getExtras();
        token = bundle.getString("Token");
        textViewTest.setText(token);


        datePicker.setCalendarViewShown(false);


        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayofMonth = datePicker.getMonth() + 1;
                textViewDate.setText(""+datePicker.getDayOfMonth() + ":" +
                        dayofMonth + ":" +datePicker.getYear());
                dateOfOrder = datePicker.getYear() + "-" + dayofMonth + "-" + datePicker.getDayOfMonth();

                Runnable netTaskGetOrder = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HTTPMethod(token,dateOfOrder);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(netTaskGetOrder).start();
            }
        });



    }

    public void HTTPMethod(String token ,String date) throws JSONException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://api.zaocan84.com/management/api/v1/orders.json?date=" + date);
        httpGet.addHeader("access-token",token);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                final String content = EntityUtils.toString(response.getEntity());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textViewTestToken.setText(content + "");
                    }
                });
//                JsonHandle jsonHandle = new JsonHandle();
//                content = jsonHandle.stringToJson(content);//验证码返回token
//
//                Message message = new Message();
//                message.what = 11;//手机号api返回token的对象
//                message.obj = dataHttp;
//                handler.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpGet.abort();
        }
    }

}
