package me.weishu.kernelsu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class VpnUtils {

    public static boolean hasVpn(Context context){
        // 查询网络状态，被动监听网络状态变化
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        // 获取当前来凝结网络
        Network currentNetwork = cm.getActiveNetwork();
        // 获取当前网络能力
        NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(currentNetwork);
        // 是否是VPN端口
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        // 是否为wifi
//        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) &&
//        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        // 是否为蜂窝网络
//        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) &&
//        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
