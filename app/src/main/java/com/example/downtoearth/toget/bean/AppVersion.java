package com.example.downtoearth.toget.bean;

import java.util.List;

public class AppVersion {


    /**
     * code : 200
     * data : [{"apkName":"toget1.0.2.apk","downloadUrl":"toget1.0.2.apk","id":6,"versionCode":2,"versionName":"1.0.2"}]
     * message : null
     */

    private int code;
    private Object message;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * apkName : toget1.0.2.apk
         * downloadUrl : toget1.0.2.apk
         * id : 6
         * versionCode : 2
         * versionName : 1.0.2
         */

        private String apkName;
        private String downloadUrl;
        private int id;
        private int versionCode;
        private String versionName;

        public String getApkName() {
            return apkName;
        }

        public void setApkName(String apkName) {
            this.apkName = apkName;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }
    }
}
