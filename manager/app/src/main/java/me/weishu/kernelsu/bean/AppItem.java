package me.weishu.kernelsu.bean;

import android.graphics.drawable.Drawable;

public class AppItem {
    private String appName;
    private Drawable appIcon;
    private String packageName;
    private boolean isCheck;
    private String rootState;
    private int uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getRootState() {
        return rootState;
    }

    public void setRootState(String rootState) {
        this.rootState = rootState;
    }
}
