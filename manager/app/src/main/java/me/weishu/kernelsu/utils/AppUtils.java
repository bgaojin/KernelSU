package me.weishu.kernelsu.utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import android.content.pm.PackageManager;
import android.os.Environment;


public class AppUtils {
    static String backPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/backData";



    /***
     * 根据包名获取app名字
     * @param context
     * @param pkgName
     * @return
     */
    public static String getAppName(Context context, String pkgName) {
        String appName = "";
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(pkgName, 0);
            appName = (String) pm.getApplicationLabel(appInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }
}
