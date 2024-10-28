package me.weishu.kernelsu.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

public class PlatformUtils {


    public static void saveParament(String fileName, String strJson) {
        try {
            StringBuilder stringBuilder1 = new StringBuilder();
            String str = stringBuilder1.append(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()).append(
                    "/backup/").toString();
            File file = new File(str);
            if (!file.exists()) file.mkdirs();
            file = new File(str + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }


            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(strJson);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public static String readFile(String filename) {
        FileInputStream inputStream = null;
        String filePath =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .getAbsolutePath()+"/backup/";
        try {
            inputStream = new FileInputStream(new File(filePath + filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static void setPro(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(null, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String getPro(String key) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("get", String.class);
            String invoke = (String) set.invoke(null, key);
            return invoke;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setProInt(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(null, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getSystemPar() {
        String hardWare = getPro("ro.hardware");
        String manufacturer = getPro("ro.product.manufacturer");
        String displayID = getPro("ro.build.display.id");
        String buildID = getPro("ro.build.id");
        String productName = getPro("ro.product.name");
        String productModel = getPro("ro.product.model");
        getPro("ro.product.device");
        getPro("ro.product.brand");
        getPro("ro.build.version.sdk");
        getPro("ro.vendor.api_level");
        getPro("ro.product.bootimage.brand");
        getPro("ro.product.bootimage.device");
        getPro("ro.product.bootimage.manufacturer");
        getPro("ro.product.bootimage.model");
        getPro("ro.product.bootimage.name");
        getPro("ro.build.version.release_or_codename");
        getPro("ro.build.version.release_or_preview_display");
        getPro("no.such.thing");
    }
}
