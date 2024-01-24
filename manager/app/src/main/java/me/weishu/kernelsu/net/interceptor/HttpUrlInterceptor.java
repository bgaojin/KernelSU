package me.weishu.kernelsu.net.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import me.weishu.kernelsu.net.CommonRetrofitManager;
import me.weishu.kernelsu.utils.ApiUtils;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUrlInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request(); // 获取原始的Request对象


        HttpUrl url = originalRequest.url();

        String strUrl = url.toString();

        if (shouldChangeUrl(strUrl)) { // 根据条件判断是否需要修改URL

            String replace = strUrl.replace(CommonRetrofitManager.BASE_URL, ApiUtils.BASE_URL);
            Request modifiedRequest = originalRequest.newBuilder().url(replace).build(); // 构造新的Request对象
            return chain.proceed(modifiedRequest); // 发送新的Request请求
        } else {
            return chain.proceed(originalRequest); // 发送原始的Request请求
        }
    }

    private boolean shouldChangeUrl(String url) {

        if (url.contains("setToken") || url.contains("startVpn")|| url.contains("closeVpn") || url.contains("backUpApp")
                || url.contains("modifyPhone") || url.contains("resetApp")) {
            return true;
        }
        return false;
    }
}
