package com.example.downtoearth.toget.bean;

import java.util.List;

public class Stuff {

    /**
     * code : 200
     * data : [{"headPic":"1552361103972","nickname":"爱丽丝","stuff":{"category":0,"content":"快点来吧丢了钥匙！！！","hot":0,"id":2,"keyword":null,"picture1":"1552369433321","picture2":null,"time":1552369433317,"uid":1000}},{"headPic":"1552361103972","nickname":"爱丽丝","stuff":{"category":0,"content":"快点来吧丢了钥匙！！！","hot":0,"id":1,"keyword":null,"picture1":null,"picture2":null,"time":1552369300015,"uid":1000}}]
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
         * headPic : 1552361103972
         * nickname : 爱丽丝
         * stuff : {"category":0,"content":"快点来吧丢了钥匙！！！","hot":0,"id":2,"keyword":null,"picture1":"1552369433321","picture2":null,"time":1552369433317,"uid":1000}
         */

        private String headPic;
        private String nickname;
        private StuffBean stuff;
        private String heartWord;

        public String getHeartWord() {
            return heartWord;
        }
        public void setHeartWord(String heartWord) {
            this.heartWord = heartWord;
        }
        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public StuffBean getStuff() {
            return stuff;
        }

        public void setStuff(StuffBean stuff) {
            this.stuff = stuff;
        }

        public static class StuffBean {
            /**
             * category : 0
             * content : 快点来吧丢了钥匙！！！
             * hot : 0
             * id : 2
             * keyword : null
             * picture1 : 1552369433321
             * picture2 : null
             * time : 1552369433317
             * uid : 1000
             */

            private int category;
            private String content;
            private int hot;
            private int id;
            private String keyword;
            private String picture1;
            private String picture2;
            private long time;
            private int uid;
            private String title;
            private float price;

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getCategory() {
                return category;
            }

            public void setCategory(int category) {
                this.category = category;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getHot() {
                return hot;
            }

            public void setHot(int hot) {
                this.hot = hot;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public String getPicture1() {
                return picture1;
            }

            public void setPicture1(String picture1) {
                this.picture1 = picture1;
            }

            public String getPicture2() {
                return picture2;
            }

            public void setPicture2(String picture2) {
                this.picture2 = picture2;
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
}
