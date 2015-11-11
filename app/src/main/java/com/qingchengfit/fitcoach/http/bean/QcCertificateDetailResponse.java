package com.qingchengfit.fitcoach.http.bean;

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
public class QcCertificateDetailResponse {

    /**
     * status : 200
     * info :
     * level : success
     * error_code :
     * msg : ok
     * data : {"certificate":{"date_of_issue":"2015-09-16T15:08:00","coach":{"id":6},"username":"青橙科技","photo":"http://zoneke-img.b0.upaiyun.com/21d3bcb5600f8b2a005cdd40c57d0c4d.png","grade":"100","organization":{},"created_at":"2015-09-16T15:08:00","type":1,"id":1,"is_authenticated":true}}
     */

    private int status;
    private String info;
    private String level;
    private String error_code;
    private String msg;
    private DataEntity data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * certificate : {"date_of_issue":"2015-09-16T15:08:00","coach":{"id":6},"username":"青橙科技","photo":"http://zoneke-img.b0.upaiyun.com/21d3bcb5600f8b2a005cdd40c57d0c4d.png","grade":"100","organization":{},"created_at":"2015-09-16T15:08:00","type":1,"id":1,"is_authenticated":true}
         */

        private CertificateEntity certificate;

        public CertificateEntity getCertificate() {
            return certificate;
        }

        public void setCertificate(CertificateEntity certificate) {
            this.certificate = certificate;
        }

        public static class CertificateEntity {
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
            private String created_at;
            private String start;
            private String end;
            private OrganizationEntity organization;
            private int type;
            private int id;
            private boolean is_authenticated;

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }

            public OrganizationEntity getOrganization() {
                return organization;
            }

            public void setOrganization(OrganizationEntity organization) {
                this.organization = organization;
            }

            public boolean is_authenticated() {
                return is_authenticated;
            }

            public String getDate_of_issue() {
                return date_of_issue;
            }

            public void setDate_of_issue(String date_of_issue) {
                this.date_of_issue = date_of_issue;
            }

            public String getStart() {
                return start;
            }

            public void setStart(String start) {
                this.start = start;
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

            public static class OrganizationEntity {
                String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
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
        }
    }
}
