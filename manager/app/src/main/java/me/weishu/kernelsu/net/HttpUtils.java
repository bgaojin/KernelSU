package me.weishu.kernelsu.net;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;

public class HttpUtils {

    public interface RequestListener {
        void onSubscribe();
        void onSuccess(String result);

        void onComplete();
        void onFailed();
    }

    public static void requestGet(String strUrl, RequestListener requestListener) {


        Observable<String> just = Observable.just(strUrl);
        Observable<Object> objectObservable = just.map(new Function<String, Object>() {
                    @Override
                    public Object apply(String s) throws Exception {
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
                            System.out.println("strResoponse=" + strResoponse);
                            return strResoponse;
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        just.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                // 订阅时的回调，可以在此处进行一些准备工作
            }

            @Override
            public void onNext(String s) {
                // 处理每个事件的回调
                requestListener.onSuccess(s);
            }

            @Override
            public void onError(Throwable e) {
                // 出错时的回调
                requestListener.onFailed();
            }

            @Override
            public void onComplete() {
                // 事件流结束时的回调
            }
        });
    }

}
