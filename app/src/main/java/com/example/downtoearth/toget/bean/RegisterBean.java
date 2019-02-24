package com.example.downtoearth.toget.bean;

/**
 * Created by DownToEarth on 2018/10/17.
 */

public class RegisterBean {

    /**
     * result : {"password":"123","id":3,"username":"ykg"}
     * message : success
     */

    private ResultBean result;
    private String message;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class ResultBean {
        /**
         * password : 123
         * id : 3
         * username : ykg
         */

        private String password;
        private int id;
        private String username;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
