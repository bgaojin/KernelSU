package me.weishu.kernelsu.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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

    public static List<AppItem> getApps(Context context, boolean hasRoot) {
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
            if (context.getPackageName().equals(packageName)) {
                continue;
            }
            if ("com.android.shell".equals(info.packageName) && !hasRoot) {
                continue;
            }
            if ("com.android.ghost.service".equals(packageName)) {
                continue;
            }
            list.add(appItem);
        }
        return list;
    }

    private static boolean filterApp(ApplicationInfo info) {

        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //判断是不是系统应用
        } else if ("com.android.ghost.service".equals(info.packageName)||"com.android.shell".equals(info.packageName)) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }

    public static boolean isAppAlive(Context context, @NonNull String clsName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            String name = service.service.getClassName();
            if (clsName.equals(name)) {
                // Service 已经启动
                return true;
            }
        }

        return false;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static boolean rootAdb(String packageName) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请root权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm uninstall " + packageName + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "uninstall msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (Exception e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }
}