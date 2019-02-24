package com.example.downtoearth.toget.bean;

import java.util.List;

/**
 * Created by DownToEarth on 2018/10/20.
 */

public class SubDynamicBean {

    /**
     * message : success
     * object : {"currentPage":1,"object":[{"commentCount":null,"content":"巴基","id":14,"level":null,"likeCount":null,"parentId":21,"subIds":null,"time":"1540044794621","uid":2,"userInfo":{"headPic":"1540043542552","nickName":"ykg"}}],"pages":0}
     */

    private String message;
    private ObjectBeanX object;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ObjectBeanX getObject() {
        return object;
    }

    public void setObject(ObjectBeanX object) {
        this.object = object;
    }

    public static class ObjectBeanX {
        /**
         * currentPage : 1
         * object : [{"commentCount":null,"content":"巴基","id":14,"level":null,"likeCount":null,"parentId":21,"subIds":null,"time":"1540044794621","uid":2,"userInfo":{"headPic":"1540043542552","nickName":"ykg"}}]
         * pages : 0
         */

        private int currentPage;
        private int pages;
        private List<ObjectBean> object;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<ObjectBean> getObject() {
            return object;
        }

        public void setObject(List<ObjectBean> object) {
            this.object = object;
        }

        public static class ObjectBean {
            /**
             * commentCount : null
             * content : 巴基
             * id : 14
             * level : null
             * likeCount : null
             * parentId : 21
             * subIds : null
             * time : 1540044794621
             * uid : 2
             * userInfo : {"headPic":"1540043542552","nickName":"ykg"}
             */

            private Integer commentCount;
            private String content;
            private int id;
            private Integer level;
            private Integer likeCount;
            private int parentId;
            private Object subIds;
            private String time;
            private int uid;
            private UserInfoBean userInfo;
            private int toUid;
            private String toNickname;

            public int getToUid() {
                return toUid;
            }

            public void setToUid(int toUid) {
                this.toUid = toUid;
            }

            public String getToNickname() {
                return toNickname;
            }

            public void setToNickname(String toNickname) {
                this.toNickname = toNickname;
            }

            public Integer getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(Integer commentCount) {
                this.commentCount = commentCount;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getLevel() {
                return level;
            }

            public void setLevel(Integer level) {
                this.level = level;
            }

            public Integer getLikeCount() {
                return likeCount;
            }

            public void setLikeCount(Integer likeCount) {
                this.likeCount = likeCount;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public Object getSubIds() {
                return subIds;
            }

            public void setSubIds(Object subIds) {
                this.subIds = subIds;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public UserInfoBean getUserInfo() {
                return userInfo;
            }

            public void setUserInfo(UserInfoBean userInfo) {
                this.userInfo = userInfo;
            }

            public static class UserInfoBean {
                /**
                 * headPic : 1540043542552
                 * nickName : ykg
                 */

                private String headPic;
                private String nickName;

                public String getHeadPic() {
                    return headPic;
                }

                public void setHeadPic(String headPic) {
                    this.headPic = headPic;
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }
            }
        }
    }
}
