package com.qingchengfit.fitcoach.http.bean;

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
 * Created by Paper on 15/9/18 2015.
 */
public class QcSerachGymRepsonse extends QcResponse {


    /**
     * data : {"total_count":4,"gym":[{"contact":"123456","id":2,"username":"测试"},{"contact":"123456","id":3,"username":"测试"},{"contact":"123456","id":4,"username":"测试2"},{"contact":"123456","id":5,"username":"测试3"}],"current_page":1,"pages":1}
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
         * total_count : 4
         * gym : [{"contact":"123456","id":2,"username":"测试"},{"contact":"123456","id":3,"username":"测试"},{"contact":"123456","id":4,"username":"测试2"},{"contact":"123456","id":5,"username":"测试3"}]
         * current_page : 1
         * pages : 1
         */

        private int total_count;
        private int current_page;
        private int pages;
        private List<AddGymBean> gym;

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<AddGymBean> getGym() {
            return gym;
        }

        public void setGym(List<AddGymBean> gym) {
            this.gym = gym;
        }

//        public static class GymEntity {
//            /**
//             * contact : 123456
//             * id : 2
//             * username : 测试
//             */
//
//            private String contact;
//            private int id;
//            private String username;
//
//            public void setContact(String contact) {
//                this.contact = contact;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public void setName(String username) {
//                this.username = username;
//            }
//
//            public String getContact() {
//                return contact;
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public String getName() {
//                return username;
//            }
//        }
    }
}
