package me.weishu.kernelsu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.weishu.kernelsu.R;


public class PwdDialog extends Dialog {

    public SaveClick saveClick;
    private EditText etUserName;

    public PwdDialog(Context paramContext) {
        super(paramContext);
    }

    public PwdDialog(Context paramContext, int paramInt) {
        super(paramContext, R.style.FullScreenDialog);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setCancelable(false);
        setContentView(R.layout.dialog_pwd);
        Window window = getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = (int) (display.getHeight());
        layoutParams.width = (int) (display.getWidth());
        window.setAttributes(layoutParams);
        Button btnSave = findViewById(R.id.btn_save);
        etUserName = findViewById(R.id.user_name);
        EditText etPWD = findViewById(R.id.pwd);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                String pwd = etPWD.getText().toString().trim();
                String userName = etUserName.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(PwdDialog.this.getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(PwdDialog.this.getContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
//
//                if (saveClick != null) {
//                    saveClick.onSave(str);
//                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendPost(userName,pwd);
                    }
                }).start();



//                SharedPreferences mSharedPrefs = LauncherPrefs.getPrefs(getContext());
//                try {
//                    String decrypt = DesUtils.decrypt("11112222", replace);
                    SharedPreferences.Editor actionDate = getContext().getSharedPreferences("",1).edit().putInt("actionDate",1);
                    actionDate.commit();
//                    Toast.makeText(getContext(), "认证成功", Toast.LENGTH_SHORT).show();
//                    dismiss();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(getContext(), "密钥错误", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    public void setSaveClick(SaveClick paramSaveClick) {
        this.saveClick = paramSaveClick;
    }

    public static interface SaveClick {
        void onSave(String pwd);
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
    }


    public void sendPost(String userName,String pwd) {
        String urlString = "http://124.223.96.97:9000/prod-api/login";
        try {
            // 创建URL对象
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为POST
            connection.setRequestMethod("POST");

            // 设置请求头，如Content-Type
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // 发送POST请求不包含body时为true
            connection.setDoOutput(true);

            // 写入POST数据
            String postData = "{\"username\":\""+userName+"\",\"password\":\""+pwd+"\",\"loginFrom\":\"1\",\"imei\":\"21312312\"}";
//            String data =
            byte[] outputInBytes = postData.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(outputInBytes);
            os.close();

            // 获取服务器响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取服务器响应
                InputStream responseStream = connection.getInputStream();
                // 处理响应...

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(responseStream));

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {

                    if (TextUtils.isEmpty(line)) {
                        continue;
                    }
                    response.append(line + "\n");
                }

                // 关闭读取器
                reader.close();
                String strResoponse = response.toString();
                JSONObject object = new JSONObject(strResoponse);
                int code = (int) object.get("code");
                if (code==200){
                    String token = (String) object.get("token");
//                    SharedPreferences mSharedPrefs = LauncherPrefs.getPrefs(getContext());
//                    mSharedPrefs.edit().putInt("token", Integer.valueOf(token));
                    dismiss();
                }else{
                    showMsg((String) object.get("msg"));
                }

                System.out.println("login=" + strResoponse);
                responseStream.close();
            } else {
                showMsg("登录失败，-1");
            }

            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            showMsg("登录失败，-2");
            e.printStackTrace();
        }
    }

    private void showMsg(String msg){
        etUserName.post(new Runnable() {
            @Override
            public void run() {
              Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String sendGet(String strUrl) {
        try {

            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);//设置超时时长，单位ms
            connection.setRequestMethod("GET");//设置请求格式
            connection.setRequestProperty("Content-Type", "Application/json");//期望返回的数据格式
            connection.setRequestProperty("CharSet", "UTF-8");//设置字符集
            connection.setRequestProperty("Accept-CharSet", "UTF-8");//请求的字符集
            connection.connect();//发送请求

            int responseCode = connection.getResponseCode();//获取返回码

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

                // 关闭读取器
                reader.close();
                String strResoponse = response.toString();

                return strResoponse;//获取返回信息
            } else {
                return "err";
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return "err";
        }

    }
}
