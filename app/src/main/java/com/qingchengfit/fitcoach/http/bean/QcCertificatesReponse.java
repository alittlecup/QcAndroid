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
 * Created by Paper on 15/9/16 2015.
 */
public class QcCertificatesReponse extends QcResponse {


    /**
     * data : {"certificates":[{"date_of_issue":"2015-09-16T15:08:00","coach":{"id":6},"username":"青橙科技","photo":"http://zoneke-img.b0.upaiyun.com/21d3bcb5600f8b2a005cdd40c57d0c4d.png","grade":"100","organization":{},"created_at":"2015-09-16T15:08:00","type":1,"id":1,"is_authenticated":true}],"total_count":1,"current_page":1,"pages":1}
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
         * certificates : [{"date_of_issue":"2015-09-16T15:08:00","coach":{"id":6},"username":"青橙科技","photo":"http://zoneke-img.b0.upaiyun.com/21d3bcb5600f8b2a005cdd40c57d0c4d.png","grade":"100","organization":{},"created_at":"2015-09-16T15:08:00","type":1,"id":1,"is_authenticated":true}]
         * total_count : 1
         * current_page : 1
         * pages : 1
         */

        private int total_count;
        private int current_page;
        private int pages;
        private List<CertificatesEntity> certificates;

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

        public List<CertificatesEntity> getCertificates() {
            return certificates;
        }

        public void setCertificates(List<CertificatesEntity> certificates) {
            this.certificates = certificates;
        }

        public static class CertificatesEntity {
            /**
             * date_of_issue : 2015-09-16T15:08:00
             * coach : {"id":6}
             * username : 青橙科技
             * photo : http://zoneke-img.b0.upaiyun.com/21d3bcb5600f8b2a005cdd40c57d0c4d.png
             * grade : 100
             * organization : {}
             * created_at : 2015-09-16T15:08:00
             * type : 1
             * id : 1
             * is_authenticated : true
             */

            private String date_of_issue;
            private CoachEntity coach;
            private String name;
            private String photo;
            private String grade;
            private OrganizationEntity organization;
            private String created_at;
            private int type;
            private int id;
            private boolean is_authenticated;
            private String start;
            private String end;
            private String certificate_name;

            public boolean is_authenticated() {
                return is_authenticated;
            }

            public String getCertificate_name() {
                return certificate_name;
            }

            public void setCertificate_name(String certificate_name) {
                this.certificate_name = certificate_name;
            }

            public String getStart() {
                return start;
            }

            public void setStart(String start) {
                this.start = start;
            }

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }

            public String getDate_of_issue() {
                return date_of_issue;
            }

            public void setDate_of_issue(String date_of_issue) {
                this.date_of_issue = date_of_issue;
            }

            public CoachEntity getCoach() {
                return coach;
            }

            public void setCoach(CoachEntity coach) {
                this.coach = coach;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public OrganizationEntity getOrganization() {
                return organization;
            }

            public void setOrganization(OrganizationEntity organization) {
                this.organization = organization;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public boolean getIs_authenticated() {
                return is_authenticated;
            }

            public void setIs_authenticated(boolean is_authenticated) {
                this.is_authenticated = is_authenticated;
            }

            public static class CoachEntity {
                /**
                 * id : 6
                 */

                private int id;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }
            }

            public static class OrganizationEntity {
                String name;
                String photo;
                String contact;
                int id;
                boolean isAuth;


                public String getContact() {
                    return contact;
                }

                public void setContact(String contact) {
                    this.contact = contact;
                }

                public boolean isAuth() {
                    return isAuth;
                }

                public void setIsAuth(boolean isAuth) {
                    this.isAuth = isAuth;
                }

                public String getPhoto() {
                    return photo;
                }

                public void setPhoto(String photo) {
                    this.photo = photo;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
