package me.weishu.kernelsu.bean;

public class LoginResult {
//{"code":0,"msg":"激活失败","time":"1705462750","data":null}
//{"code":1,"msg":"激活成功","time":"1705462842","data":{"token":"30e2a6359befde5498f4e18c6ab82300"}}
    private int code;
    private String msg;
    private String time;
    private DataDao data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DataDao getData() {
        return data;
    }

    public void setData(DataDao data) {
        this.data = data;
    }

    public static class DataDao {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
