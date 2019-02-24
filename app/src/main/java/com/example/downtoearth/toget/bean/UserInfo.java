package com.example.downtoearth.toget.bean;

public class UserInfo {


    /**
     * code : 200
     * data : {"token":"D0CBB1D88E9658DD26F6C12624E1AA96","userInfo":{"birthday":"1997-11-20","gender":true,"headPic":"1548642013570","heartWord":"快来发布心情吧~","id":1000,"nickname":"徐磕碜","password":"123456","phone":"15252478436"}}
     * message : success
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * token : D0CBB1D88E9658DD26F6C12624E1AA96
         * userInfo : {"birthday":"1997-11-20","gender":true,"headPic":"1548642013570","heartWord":"快来发布心情吧~","id":1000,"nickname":"徐磕碜","password":"123456","phone":"15252478436"}
         */

        private String token;
        private UserInfoBean userInfo;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfoBean {
            /**
             * birthday : 1997-11-20
             * gender : true
             * headPic : 1548642013570
             * heartWord : 快来发布心情吧~
             * id : 1000
             * nickname : 徐磕碜
             * password : 123456
             * phone : 15252478436
             */

            private String birthday;
            private boolean gender;
            private String headPic;
            private String heartWord;
            private int id;
            private String nickname;
            private String password;
            private String phone;

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public boolean isGender() {
                return gender;
            }

            public void setGender(boolean gender) {
                this.gender = gender;
            }

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }

            public String getHeartWord() {
                return heartWord;
            }

            public void setHeartWord(String heartWord) {
                this.heartWord = heartWord;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
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
        }
    }
}
