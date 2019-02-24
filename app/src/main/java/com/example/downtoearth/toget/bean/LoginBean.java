package com.example.downtoearth.toget.bean;

/**
 * Created by DownToEarth on 2018/10/19.
 */

public class LoginBean {

    /**
     * message : success
     * object : {"headPic":null,"headPicName":null,"id":7,"nickName":null,"password":"123","phone":null,"sex":null,"username":"fghk"}
     */

    private String message;
    private ObjectBean object;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    public static class ObjectBean {
        /**
         * headPic : null
         * headPicName : null
         * id : 7
         * nickName : null
         * password : 123
         * phone : null
         * sex : null
         * username : fghk
         */

        private String headPic;
        private String headPicName;
        private int id;
        private String nickName;
        private String password;
        private String phone;
        private int sex;
        private String username;

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getHeadPicName() {
            return headPicName;
        }

        public void setHeadPicName(String headPicName) {
            this.headPicName = headPicName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
