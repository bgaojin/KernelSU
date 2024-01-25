package me.weishu.kernelsu.bean;

public class GetModelResult {

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
        private LastVersionsDao last_versions;

        public LastVersionsDao getLast_versions() {
            return last_versions;
        }

        public void setLast_versions(LastVersionsDao last_versions) {
            this.last_versions = last_versions;
        }

        public static class LastVersionsDao {
            private int id;
            private String category;
            private int admin_id;
            private int user_id;
            private String url;
            private String imagewidth;
            private String imageheight;
            private String imagetype;
            private int imageframes;
            private String filename;
            private int filesize;
            private String mimetype;
            private String extparam;
            private int createtime;
            private int updatetime;
            private int uploadtime;
            private String storage;
            private String sha1;
            private String thumb_style;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public int getAdmin_id() {
                return admin_id;
            }

            public void setAdmin_id(int admin_id) {
                this.admin_id = admin_id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getImagewidth() {
                return imagewidth;
            }

            public void setImagewidth(String imagewidth) {
                this.imagewidth = imagewidth;
            }

            public String getImageheight() {
                return imageheight;
            }

            public void setImageheight(String imageheight) {
                this.imageheight = imageheight;
            }

            public String getImagetype() {
                return imagetype;
            }

            public void setImagetype(String imagetype) {
                this.imagetype = imagetype;
            }

            public int getImageframes() {
                return imageframes;
            }

            public void setImageframes(int imageframes) {
                this.imageframes = imageframes;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public int getFilesize() {
                return filesize;
            }

            public void setFilesize(int filesize) {
                this.filesize = filesize;
            }

            public String getMimetype() {
                return mimetype;
            }

            public void setMimetype(String mimetype) {
                this.mimetype = mimetype;
            }

            public String getExtparam() {
                return extparam;
            }

            public void setExtparam(String extparam) {
                this.extparam = extparam;
            }

            public int getCreatetime() {
                return createtime;
            }

            public void setCreatetime(int createtime) {
                this.createtime = createtime;
            }

            public int getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(int updatetime) {
                this.updatetime = updatetime;
            }

            public int getUploadtime() {
                return uploadtime;
            }

            public void setUploadtime(int uploadtime) {
                this.uploadtime = uploadtime;
            }

            public String getStorage() {
                return storage;
            }

            public void setStorage(String storage) {
                this.storage = storage;
            }

            public String getSha1() {
                return sha1;
            }

            public void setSha1(String sha1) {
                this.sha1 = sha1;
            }

            public String getThumb_style() {
                return thumb_style;
            }

            public void setThumb_style(String thumb_style) {
                this.thumb_style = thumb_style;
            }
        }
    }
}
