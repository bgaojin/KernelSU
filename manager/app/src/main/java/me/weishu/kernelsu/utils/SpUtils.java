package me.weishu.kernelsu.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {
    private static final String SP_NAME = "Config";

    private static volatile SpUtils mInstance;

    private SharedPreferences.Editor editor;

    private static SharedPreferences sp;

    public static SpUtils getInstance() {
       if (mInstance==null){
           synchronized (SpUtils.class){
               if (mInstance==null){
                   mInstance = new SpUtils();

               }
           }
       }
       return mInstance;
    }



    public void deleteKey(String paramString) {
        this.editor.remove(paramString);
        this.editor.commit();
    }

    public boolean getBoolean(String paramString, boolean paramBoolean) {
        return this.sp.getBoolean(paramString, paramBoolean);
    }

    public int getInt(String paramString, int paramInt) {
        return this.sp.getInt(paramString, paramInt);
    }

    public String getString(String paramString1, String paramString2) {
        return this.sp.getString(paramString1, paramString2);
    }

    public void initSp(Context paramContext) {
        SharedPreferences sharedPreferences = paramContext.getSharedPreferences("Config", 0);
        this.sp = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public void putBoolean(String paramString, boolean paramBoolean) {
        this.editor.putBoolean(paramString, paramBoolean);
        this.editor.commit();
    }

    public void putInt(String paramString, int paramInt) {
        this.editor.putInt(paramString, paramInt);
        this.editor.commit();
    }

    public void putString(String paramString1, String paramString2) {
        this.editor.putString(paramString1, paramString2);
        this.editor.commit();
    }
}
