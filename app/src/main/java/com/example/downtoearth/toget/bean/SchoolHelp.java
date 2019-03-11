package com.example.downtoearth.toget.bean;

import java.util.List;

public class SchoolHelp {


    /**
     * code : 200
     * data : [{"bounty":0,"browser":null,"content":"这道题太难了，我不会！我不会！","headPic":"1552309234504","heartWord":null,"id":2,"nickname":"爱丽丝","picture":null,"postUid":0,"status":0,"time":1552297188235,"title":"高等数学","uid":1000},{"bounty":0,"browser":null,"content":"这道题太难了，我不会！我不会！","headPic":"1552309234504","heartWord":null,"id":1,"nickname":"爱丽丝","picture":null,"postUid":0,"status":0,"time":1552295307052,"title":"大学物理","uid":1000}]
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
         * bounty : 0
         * browser : null
         * content : 这道题太难了，我不会！我不会！
         * headPic : 1552309234504
         * heartWord : null
         * id : 2
         * nickname : 爱丽丝
         * picture : null
         * postUid : 0
         * status : 0
         * time : 1552297188235
         * title : 高等数学
         * uid : 1000
         */

        private int bounty;
        private int browser;
        private String content;
        private String headPic;
        private String heartWord;
        private int id;
        private String nickname;
        private String picture;
        private int postUid;
        private int status;
        private long time;
        private String title;
        private int uid;

        public int getBounty() {
            return bounty;
        }

        public void setBounty(int bounty) {
            this.bounty = bounty;
        }

        public int getBrowser() {
            return browser;
        }

        public void setBrowser(int browser) {
            this.browser = browser;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public Object getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public int getPostUid() {
            return postUid;
        }

        public void setPostUid(int postUid) {
            this.postUid = postUid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
