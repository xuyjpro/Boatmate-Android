package com.example.downtoearth.toget.bean;

import java.util.List;

public class Blog {

    /**
     * code : 200
     * data : [{"descrip":"posted @ 2019-04-01 15:12 徐磕碜 阅读(7) 评论(0)  ","id":1,"summary":"该文被密码保护。<\/div><\/div>\r\n\t\t\t<div class=\"clear\"><\/div>\r\n\t\t\t<div class=\"postDesc\">posted @ 2018-10-12 11","title":"[置顶]Secret","url":"https://www.cnblogs.com/xuyj/p/9777228.html"},{"descrip":"posted @ 2019-02-28 12:36 徐磕碜 阅读(6) 评论(0)  ","id":2,"summary":"摘要: 总结：HashMap的实现原理： 利用key的hashCode重新hash计算出当前对象的元素在数组中的下标 存储时，如果出现hash值相同的key，此时有两种情况。(1)如果key相同，则","title":"HashCode","url":"https://www.cnblogs.com/xuyj/p/10449616.html"},{"descrip":"posted @ 2019-01-13 13:54 徐磕碜 阅读(5) 评论(0)  ","id":3,"summary":"摘要: Android \u2014\u2014PopupWindow 基本用法 1、创建PopupWindow对象实例； 2、设置背景、注册事件监听器和添加动画； 3、显示PopupWindow。 例子： // 用于","title":"Android-PopupWindow","url":"https://www.cnblogs.com/xuyj/p/10262434.html"},{"descrip":"posted @ 2019-01-13 13:39 徐磕碜 阅读(17) 评论(0)  ","id":4,"summary":"摘要: Android搜索框输入内容点击键盘的搜索按钮进行搜索 1、在布局XML中的EditView 加入 2、监听调用 EditView.setOnEditorActionListener(new","title":"Android\u2014\u2014输入法搜索","url":"https://www.cnblogs.com/xuyj/p/10262403.html"},{"descrip":"posted @ 2019-01-10 16:59 徐磕碜 阅读(11) 评论(0)  ","id":5,"summary":"摘要: CSRF 跨站点请求伪造 Low 实验原理：服务端代码 ' . mysql_error() . '' ); // Feedback for the user echo \"Password C","title":"CSRF 跨站点请求伪造","url":"https://www.cnblogs.com/xuyj/p/10251057.html"},{"descrip":"posted @ 2019-01-07 12:10 徐磕碜 阅读(10) 评论(0)  ","id":6,"summary":"摘要: Android 基本面试题 一、java 核心思想 1、八种基本数据类型和封装类 基本类型|大小（字节） | byte|1 short|2 int|4 long|8 float|4 doub","title":"Android 基本面试题","url":"https://www.cnblogs.com/xuyj/p/10232518.html"},{"descrip":"posted @ 2018-12-11 17:44 徐磕碜 阅读(4) 评论(0)  ","id":7,"summary":"摘要: 1、switch哪些类型 switch支持byte,short，char，int四种整型类型，java1.7后支持枚举类和String。之所以使用几种基本类型的包装类不报错的原因是，java","title":"java-基础笔记","url":"https://www.cnblogs.com/xuyj/p/10103728.html"},{"descrip":"posted @ 2018-11-19 11:53 徐磕碜 阅读(3) 评论(0)  ","id":8,"summary":"摘要: 一、rpm 1、查找某个包 rpm -qa|grep -i [包名] 2、删除某个包 rpm -e [完整包名]","title":"centos\u2014\u2014命令笔记","url":"https://www.cnblogs.com/xuyj/p/9982359.html"},{"descrip":"posted @ 2018-11-17 11:14 徐磕碜 阅读(12) 评论(0)  ","id":9,"summary":"摘要: 1、URL装Bitmap 2、EditView 默认不弹出软键盘 android:focusable=&quot;true&quot;android:focusableInTouchMode","title":"Android\u2014\u2014学习笔记","url":"https://www.cnblogs.com/xuyj/p/9973231.html"},{"descrip":"posted @ 2018-11-16 13:12 徐磕碜 阅读(3) 评论(0)  ","id":10,"summary":"摘要: 1、指定用户身份 [root@yelhali learngit]# git config --global user.name &quot;xuyj&quot;[root@yelhali l","title":"Git\u2014\u2014学习笔记","url":"https://www.cnblogs.com/xuyj/p/9968726.html"}]
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
         * descrip : posted @ 2019-04-01 15:12 徐磕碜 阅读(7) 评论(0)
         * id : 1
         * summary : 该文被密码保护。</div></div>
         <div class="clear"></div>
         <div class="postDesc">posted @ 2018-10-12 11
         * title : [置顶]Secret
         * url : https://www.cnblogs.com/xuyj/p/9777228.html
         */

        private String descrip;
        private int id;
        private String summary;
        private String title;
        private String url;

        public String getDescrip() {
            return descrip;
        }

        public void setDescrip(String descrip) {
            this.descrip = descrip;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
