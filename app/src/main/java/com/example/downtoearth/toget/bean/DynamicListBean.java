package com.example.downtoearth.toget.bean;

import java.util.List;

/**
 * Created by DownToEarth on 2018/10/20.
 */

public class DynamicListBean {


    /**
     * code : 200
     * data : [{"awesome":1,"comment":14,"content":"你好呀","headPic":"1548654986572","id":5,"like":false,"nickname":"杨凯歌","time":1547710846470,"uid":1000},{"awesome":1,"comment":3,"content":"快来呀。","headPic":"1548654986572","id":4,"like":true,"nickname":"杨凯歌","time":1547638382218,"uid":1000}]
     * message : success
     */

    private int code;
    private String message;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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
         * awesome : 1
         * comment : 14
         * content : 你好呀
         * headPic : 1548654986572
         * id : 5
         * like : false
         * nickname : 杨凯歌
         * time : 1547710846470
         * uid : 1000
         */

        private int awesome;
        private int comment;
        private String content;
        private String headPic;
        private int id;
        private boolean like;
        private String nickname;
        private long time;
        private int uid;

        public int getAwesome() {
            return awesome;
        }

        public void setAwesome(int awesome) {
            this.awesome = awesome;
        }

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isLike() {
            return like;
        }

        public void setLike(boolean like) {
            this.like = like;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
