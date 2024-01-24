package me.weishu.kernelsu.utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

import me.weishu.kernelsu.Natives;
import me.weishu.kernelsu.bean.AppItem;


public class AppUtils {

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

    public static List<AppItem> getApps(Context context) {
        ArrayList<AppItem> list = new ArrayList();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo info : installedPackages) {
            ApplicationInfo appInfo = info.applicationInfo;
            //去除系统应用
            if (!filterApp(appInfo)) {
                continue;
            }
            //拿到应用程序的图标
            Drawable icon = appInfo.loadIcon(packageManager);
            //拿到应用程序的程序名
            String appName = appInfo.loadLabel(packageManager).toString();
            //拿到应用程序的包名
            String packageName = appInfo.packageName;

            AppItem appItem = new AppItem();
            appItem.setAppName(appName);
            appItem.setAppIcon(icon);
            appItem.setUid(appInfo.uid);
            appItem.setPackageName(packageName);
            Natives.Profile appProfile = Natives.INSTANCE.getAppProfile(packageName, appInfo.uid);
            appItem.setRootState(appProfile.getAllowSu() ? "ROOT" : "UNKNOW");
            if (!"me.weishu.kernelsu".equals(packageName)) {
                list.add(appItem);
            }

        }
        return list;
    }

    private static boolean filterApp(ApplicationInfo info) {

        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //判断是不是系统应用
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        } else if (info.packageName.equals("com.lin.oneghost")) {
            return false;
        }
        return false;
    }
}
