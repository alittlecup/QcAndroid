package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.network.response.QcResponse;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/14 2015.
 */
public class QcNotiDetailResponse extends QcResponse {

    /**
     * data : {"read_at":"2015-09-14T12:38:25","sender":"系统","title":"通知测试","photo":"","created_at":"2015-09-14T11:29:00","id":1}
     */

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * read_at : 2015-09-14T12:38:25
         * sender : 系统
         * title : 通知测试
         * photo :
         * created_at : 2015-09-14T11:29:00
         * id : 1
         */

        private String read_at;
        private String sender;
        private String title;
        private String photo;
        private String created_at;
        private String url;
        private int id;

        public String getRead_at() {
            return read_at;
        }

        public void setRead_at(String read_at) {
            this.read_at = read_at;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
