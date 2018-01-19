//package com.qingchengfit.fitcoach.http.bean;
//
//import cn.qingchengfit.network.response.QcResponse;
//import java.util.List;
//
///**
// * power by
// * <p>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p>
// * <p>
// * Created by Paper on 15/9/20 2015.
// */
//public class QcSearchOrganResponse extends QcResponse {
//
//    /**
//     * data : {"organizations":[{"contact":"1616671212","id":2,"username":"中国健身大会"}],"current_page":1,"pages":1,"total_count":1}
//     */
//
//    private DataEntity data;
//
//    public DataEntity getData() {
//        return data;
//    }
//
//    public void setData(DataEntity data) {
//        this.data = data;
//    }
//
//    public static class DataEntity {
//        /**
//         * organizations : [{"contact":"1616671212","id":2,"username":"中国健身大会"}]
//         * current_page : 1
//         * pages : 1
//         * total_count : 1
//         */
//
//        private int current_page;
//        private int pages;
//        private int total_count;
//        private List<OrganizationsEntity> organizations;
//
//        public int getCurrent_page() {
//            return current_page;
//        }
//
//        public void setCurrent_page(int current_page) {
//            this.current_page = current_page;
//        }
//
//        public int getPages() {
//            return pages;
//        }
//
//        public void setPages(int pages) {
//            this.pages = pages;
//        }
//
//        public int getTotal_count() {
//            return total_count;
//        }
//
//        public void setTotal_count(int total_count) {
//            this.total_count = total_count;
//        }
//
//        public List<OrganizationsEntity> getOrganizations() {
//            return organizations;
//        }
//
//        public void setOrganizations(List<OrganizationsEntity> organizations) {
//            this.organizations = organizations;
//        }
//
//        public static class OrganizationsEntity {
//            /**
//             * contact : 1616671212
//             * id : 2
//             * username : 中国健身大会
//             */
//
//            private String contact;
//            private int id;
//            private String name;
//            private String photo;
//            private boolean is_authenticated;
//
//            public String getPhoto() {
//                return photo;
//            }
//
//            public void setPhoto(String photo) {
//                this.photo = photo;
//            }
//
//            public boolean is_authenticated() {
//                return is_authenticated;
//            }
//
//            public void setIs_authenticated(boolean is_authenticated) {
//                this.is_authenticated = is_authenticated;
//            }
//
//            public String getContact() {
//                return contact;
//            }
//
//            public void setContact(String contact) {
//                this.contact = contact;
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//        }
//    }
//}
