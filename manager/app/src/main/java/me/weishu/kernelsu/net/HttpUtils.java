package me.weishu.kernelsu.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public interface RequestListener{
        void onSuccess(String result);
        void onFailed();
    }

    public static void requestGet(String strUrl,RequestListener requestListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        String line;
                        StringBuilder response = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        // 关闭读取器
                        reader.close();
                        String strResoponse = response.toString();
                        System.out.println("strResoponse="+strResoponse);
                        requestListener.onSuccess(strResoponse);
                    } else {
                        requestListener.onFailed();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    requestListener.onFailed();
                }

            }
        }).start();

    }
}
