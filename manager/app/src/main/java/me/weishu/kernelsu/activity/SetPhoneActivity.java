package me.weishu.kernelsu.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.Set;

import me.weishu.kernelsu.R;

/**
 * @author: wrp
 * @date : 2024/9/27
 * @desc :
 */
public class SetPhoneActivity extends AppCompatActivity {

    private Handler handler;
    private HttpURLConnection connection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_phone);
        EditText phone = findViewById(R.id.phone);
        Button save = findViewById(R.id.save);
//        VolumeReceiver volumeReceiver = new VolumeReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
//        registerReceiver(volumeReceiver,intentFilter);
        handler = new Handler();
        handler.postDelayed(runnable,3000);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




//                String strPhone = phone.getText().toString();
////                if (TextUtils.isEmpty(strPhone)){}
//                Settings.System.putString(getContentResolver(),"i_set_phone",""+strPhone);
//                Settings.System.putString(getContentResolver(),"i_can_enable","1");
//                String iSetPhone = Settings.System.getString(getContentResolver(), "i_set_phone");
//                String i_can_enable = Settings.System.getString(getContentResolver(), "i_can_enable");
//                System.out.println("iSetPhone="+iSetPhone);
//                System.out.println("i_can_enable="+i_can_enable);
//                Toast.makeText(SetPhoneActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
//
//                                String phoneNumber = "tel:13465811772";
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                Uri parse = Uri.parse("tel:13465811772");
//                callIntent.setData(parse);
//                String path = parse.getSchemeSpecificPart();
//                System.out.println("path1="+parse.getScheme());
//                System.out.println("path="+path);
//                startActivity(callIntent);
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendGet("http://124.223.96.97:9002");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if (connection!=null){
//                            connection.disconnect();
                        }
                        handler.postDelayed(runnable,3000);
                    }
                }
            }).start();
        }
    };
    private String sendGet(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(10 * 1000);//设置超时时长，单位ms
        connection.setReadTimeout(10 * 1000);
        connection.setRequestMethod("GET");//设置请求格式
        connection.setRequestProperty("Content-Type", "Application/json");//期望返回的数据格式
        connection.setRequestProperty("CharSet", "UTF-8");//设置字符集
        connection.setRequestProperty("Accept-CharSet", "UTF-8");//请求的字符集

//        String token = mSharedPrefs.getString("token", "");
//
//        if (TextUtils.isEmpty(token)) {
//            return "err";
//        }
//        connection.setRequestProperty("Authorization", token);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connection.connect();//发送请求

        int responseCode = connection.getResponseCode();//获取返回码
        System.out.println("responseCode="+responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            //TODO
            // 获取输入流
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream));

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {

                if (TextUtils.isEmpty(line)) {
                    continue;
                }
                response.append(line + "\n");
            }
//            connection.disconnect();
            // 关闭读取器
            reader.close();
            String strResoponse = response.toString();

            return strResoponse;//获取返回信息
        } else {
            return "err";
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            System.out.println("音量减");

        }else if (keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            System.out.println("音量加");
        }
        return super.onKeyDown(keyCode, event);
    }
}
