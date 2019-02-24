package com.example.downtoearth.toget.bean;

import java.util.List;

public class Comment {

    /**
     * code : 200
     * data : [{"awesome":0,"comment":0,"content":"不行也","headPic":"1549712657930","id":31,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547715080009,"uid":1000},{"awesome":0,"comment":0,"content":"不行也","headPic":"1549712657930","id":30,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714803203,"uid":1000},{"awesome":0,"comment":0,"content":"不行也","headPic":"1549712657930","id":29,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714800486,"uid":1000},{"awesome":0,"comment":0,"content":"不行也","headPic":"1549712657930","id":28,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714789823,"uid":1000},{"awesome":0,"comment":0,"content":"不行也","headPic":"1549712657930","id":27,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714783451,"uid":1000},{"awesome":0,"comment":0,"content":"可以也","headPic":"1549712657930","id":26,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714683773,"uid":1000},{"awesome":0,"comment":0,"content":"可以也","headPic":"1549712657930","id":25,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714665834,"uid":1000},{"awesome":0,"comment":0,"content":"可以也","headPic":"1549712657930","id":24,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714615161,"uid":1000},{"awesome":0,"comment":0,"content":"可以也","headPic":"1549712657930","id":23,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714599357,"uid":1000},{"awesome":0,"comment":0,"content":"可以也","headPic":"1549712657930","id":22,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714570804,"uid":1000},{"awesome":0,"comment":0,"content":"可以也","headPic":"1549712657930","id":21,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714562417,"uid":1000},{"awesome":0,"comment":0,"content":"可以也","headPic":"1549712657930","id":20,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714554944,"uid":1000},{"awesome":0,"comment":0,"content":"可以也","headPic":"1549712657930","id":19,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714446399,"uid":1000},{"awesome":0,"comment":0,"content":"可以也","headPic":"1549712657930","id":16,"like":false,"nickname":"杨凯歌","parentId":5,"time":1547714380152,"uid":1000}]
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
         * awesome : 0
         * comment : 0
         * content : 不行也
         * headPic : 1549712657930
         * id : 31
         * like : false
         * nickname : 杨凯歌
         * parentId : 5
         * time : 1547715080009
         * uid : 1000
         */

        private int awesome;
        private int comment;
        private String content;
        private String headPic;
        private int id;
        private boolean like;
        private String nickname;
        private int parentId;
        private long time;
        private int uid;
        private String toNickname;

        public String getToNickname() {
            return toNickname;
        }

        public void setToNickname(String toNickname) {
            this.toNickname = toNickname;
        }

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

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
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
