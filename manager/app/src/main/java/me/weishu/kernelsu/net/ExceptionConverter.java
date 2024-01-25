package me.weishu.kernelsu.net;


public class ExceptionConverter {

    private static final String TAG = "ExceptionConverter";

    public static Throwable convertException(Throwable e) {
        e.printStackTrace();
        return e;
    }
}
