package com.qingchengfit.fitcoach.http.bean;

import android.os.Parcel;
import android.os.Parcelable;

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

        public static class CertificatesEntity implements Parcelable {
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
            private boolean is_hidden; //是否隐藏
            private boolean will_expired; //是否有有效期


            public boolean is_hidden() {
                return is_hidden;
            }

            public void setIs_hidden(boolean is_hidden) {
                this.is_hidden = is_hidden;
            }

            public boolean isWill_expired() {
                return will_expired;
            }

            public void setWill_expired(boolean will_expired) {
                this.will_expired = will_expired;
            }

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

            public static class CoachEntity implements Parcelable {
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

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.id);
                }

                public CoachEntity() {
                }

                protected CoachEntity(Parcel in) {
                    this.id = in.readInt();
                }

                public static final Creator<CoachEntity> CREATOR = new Creator<CoachEntity>() {
                    @Override
                    public CoachEntity createFromParcel(Parcel source) {
                        return new CoachEntity(source);
                    }

                    @Override
                    public CoachEntity[] newArray(int size) {
                        return new CoachEntity[size];
                    }
                };
            }

            public static class OrganizationEntity implements Parcelable {
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

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.name);
                    dest.writeString(this.photo);
                    dest.writeString(this.contact);
                    dest.writeInt(this.id);
                    dest.writeByte(isAuth ? (byte) 1 : (byte) 0);
                }

                public OrganizationEntity() {
                }

                protected OrganizationEntity(Parcel in) {
                    this.name = in.readString();
                    this.photo = in.readString();
                    this.contact = in.readString();
                    this.id = in.readInt();
                    this.isAuth = in.readByte() != 0;
                }

                public static final Creator<OrganizationEntity> CREATOR = new Creator<OrganizationEntity>() {
                    @Override
                    public OrganizationEntity createFromParcel(Parcel source) {
                        return new OrganizationEntity(source);
                    }

                    @Override
                    public OrganizationEntity[] newArray(int size) {
                        return new OrganizationEntity[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.date_of_issue);
                dest.writeParcelable(this.coach, flags);
                dest.writeString(this.name);
                dest.writeString(this.photo);
                dest.writeString(this.grade);
                dest.writeParcelable(this.organization, flags);
                dest.writeString(this.created_at);
                dest.writeInt(this.type);
                dest.writeInt(this.id);
                dest.writeByte(is_authenticated ? (byte) 1 : (byte) 0);
                dest.writeString(this.start);
                dest.writeString(this.end);
                dest.writeString(this.certificate_name);
                dest.writeByte(is_hidden ? (byte) 1 : (byte) 0);
                dest.writeByte(will_expired ? (byte) 1 : (byte) 0);
            }

            public CertificatesEntity() {
            }

            protected CertificatesEntity(Parcel in) {
                this.date_of_issue = in.readString();
                this.coach = in.readParcelable(CoachEntity.class.getClassLoader());
                this.name = in.readString();
                this.photo = in.readString();
                this.grade = in.readString();
                this.organization = in.readParcelable(OrganizationEntity.class.getClassLoader());
                this.created_at = in.readString();
                this.type = in.readInt();
                this.id = in.readInt();
                this.is_authenticated = in.readByte() != 0;
                this.start = in.readString();
                this.end = in.readString();
                this.certificate_name = in.readString();
                this.is_hidden = in.readByte() != 0;
                this.will_expired = in.readByte() != 0;
            }

            public static final Parcelable.Creator<CertificatesEntity> CREATOR = new Parcelable.Creator<CertificatesEntity>() {
                @Override
                public CertificatesEntity createFromParcel(Parcel source) {
                    return new CertificatesEntity(source);
                }

                @Override
                public CertificatesEntity[] newArray(int size) {
                    return new CertificatesEntity[size];
                }
            };
        }
    }
}
