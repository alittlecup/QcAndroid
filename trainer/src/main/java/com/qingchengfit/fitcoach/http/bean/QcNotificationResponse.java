package com.qingchengfit.fitcoach.http.bean;

import cn.qingchengfit.network.response.QcResponse;
import java.util.List;

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
public class QcNotificationResponse extends QcResponse {

    /**
     * data : {"message":"ok","msgs":[{"read_at":"2015-09-14T12:38:25","sender":"系统","title":"通知测试","url":"http://www.qingchengfit.cn/","photo":"https://img.qingchengfit.cn/21d3bcb5600f8b2a005cdd40c57d0c4d.png","created_at":"2015-09-14T11:29:00","id":1}],"ret_code":0}
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
         * message : ok
         * msgs : [{"read_at":"2015-09-14T12:38:25","sender":"系统","title":"通知测试","url":"http://www.qingchengfit.cn/","photo":"https://img.qingchengfit.cn/21d3bcb5600f8b2a005cdd40c57d0c4d.png","created_at":"2015-09-14T11:29:00","id":1}]
         * ret_code : 0
         */

        private String message;
        private int ret_code;
        private List<MsgsEntity> msgs;
        private List<MsgsEntity> notifications;
        private int total_count;
        private int unread_count;
        private int pages;

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public int getUnread_count() {
            return unread_count;
        }

        public void setUnread_count(int unread_count) {
            this.unread_count = unread_count;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public List<MsgsEntity> getMsgs() {
            return msgs;
        }

        public void setMsgs(List<MsgsEntity> msgs) {
            this.msgs = msgs;
        }

        public List<MsgsEntity> getNotifications() {
            return notifications;
        }

        public void setNotifications(List<MsgsEntity> notifications) {
            this.notifications = notifications;
        }

        public static class MsgsEntity {
            /**
             * read_at : 2015-09-14T12:38:25
             * sender : 系统
             * title : 通知测试
             * url : http://www.qingchengfit.cn/
             * photo : https://img.qingchengfit.cn/21d3bcb5600f8b2a005cdd40c57d0c4d.png
             * created_at : 2015-09-14T11:29:00
             * id : 1
             */

            private String read_at;
            private String sender;
            private String title;
            private String url;
            private String photo;
            private String created_at;
            private boolean is_read;
            private String description;
            private int id;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public boolean is_read() {
                return is_read;
            }

            public void setIs_read(boolean is_read) {
                this.is_read = is_read;
            }

            public String getRead_at() {
                return read_at;
            }

            public void setRead_at(String read_at) {
                this.read_at = read_at;
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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
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
}
