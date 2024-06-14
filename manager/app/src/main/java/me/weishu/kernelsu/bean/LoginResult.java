package me.weishu.kernelsu.bean;

public class LoginResult {
    private int code;
    private String msg;
    private long ts;
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

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public DataDao getData() {
        return data;
    }

    public void setData(DataDao data) {
        this.data = data;
    }

    public static class DataDao {
        private int id;
        private String code;
        private String token;
        private int type;
        private long createTime;
        private long updateTime;
        private long expiredTime;
        private int status;
        private int version;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public long getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(long expiredTime) {
            this.expiredTime = expiredTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }
//{"code":0,"msg":"激活失败","time":"1705462750","data":null}
//{"code":1,"msg":"激活成功","time":"1705462842","data":{"token":"30e2a6359befde5498f4e18c6ab82300"}}
//    private int code;
//    private String msg;
//    private String time;
//    private DataDao data;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public DataDao getData() {
//        return data;
//    }
//
//    public void setData(DataDao data) {
//        this.data = data;
//    }
//
//    public static class DataDao {
//        private String token;
//
//        public String getToken() {
//            return token;
//        }
//
//        public void setToken(String token) {
//            this.token = token;
//        }
//
//        @Override
//        public String toString() {
//            return "DataDao{" +
//                    "token='" + token + '\'' +
//                    '}';
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "LoginResult{" +
//                "code=" + code +
//                ", msg='" + msg + '\'' +
//                ", time='" + time + '\'' +
//                ", data=" + data +
//                '}';
//    }



}
