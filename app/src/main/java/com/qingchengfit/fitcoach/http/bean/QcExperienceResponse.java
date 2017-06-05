package com.qingchengfit.fitcoach.http.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import cn.qingchengfit.model.base.DistrictEntity;
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
 * Created by Paper on 15/9/16 2015.
 */
public class QcExperienceResponse extends QcResponse {

    /**
     * data : {"total_count":1,"current_page":1,"experiences":[{"coach":{"id":6},"description":"无","is_authenticated":false,"position":"教练","private_course":421,"city":"北京","end":"2015-11-16T15:29:00","username":"中美引力工作室","group_user":123,"sale":10000000,"start":"2015-09-16T15:29:00","group_course":100,"private_user":568}],"pages":1}
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
         * total_count : 1
         * current_page : 1
         * experiences : [{"coach":{"id":6},"description":"无","is_authenticated":false,"position":"教练","private_course":421,"city":"北京","end":"2015-11-16T15:29:00","username":"中美引力工作室","group_user":123,"sale":10000000,"start":"2015-09-16T15:29:00","group_course":100,"private_user":568}]
         * pages : 1
         */

        private int total_count;
        private int current_page;
        private int pages;
        private List<ExperiencesEntity> experiences;

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

        public List<ExperiencesEntity> getExperiences() {
            return experiences;
        }

        public void setExperiences(List<ExperiencesEntity> experiences) {
            this.experiences = experiences;
        }

        public static class ExperiencesEntity implements Parcelable {
            public static final Creator<ExperiencesEntity> CREATOR = new Creator<ExperiencesEntity>() {
                @Override public ExperiencesEntity createFromParcel(Parcel source) {
                    return new ExperiencesEntity(source);
                }

                @Override public ExperiencesEntity[] newArray(int size) {
                    return new ExperiencesEntity[size];
                }
            };
            /**
             * coach : {"id":6}
             * description : 无
             * is_authenticated : false
             * position : 教练
             * private_course : 421
             * city : 北京
             * end : 2015-11-16T15:29:00
             * username : 中美引力工作室
             * group_user : 123
             * sale : 10000000
             * start : 2015-09-16T15:29:00
             * group_course : 100
             * private_user : 568
             */

            private CoachEntity coach;
            private String description;
            private boolean is_authenticated;
            private String position;
            private int private_course;
            private String end;
            private String name;
            private int id;
            private int group_user;
            private String sale;
            private String start;
            private int group_course;
            private int private_user;
            private GymEntity gym;
            private boolean is_hidden;
            private boolean group_is_hidden;
            private boolean private_is_hidden;
            private boolean sale_is_hidden;

            public ExperiencesEntity() {
            }

            protected ExperiencesEntity(Parcel in) {
                this.coach = in.readParcelable(CoachEntity.class.getClassLoader());
                this.description = in.readString();
                this.is_authenticated = in.readByte() != 0;
                this.position = in.readString();
                this.private_course = in.readInt();
                this.end = in.readString();
                this.name = in.readString();
                this.id = in.readInt();
                this.group_user = in.readInt();
                this.sale = in.readString();
                this.start = in.readString();
                this.group_course = in.readInt();
                this.private_user = in.readInt();
                this.gym = in.readParcelable(GymEntity.class.getClassLoader());
                this.is_hidden = in.readByte() != 0;
                this.group_is_hidden = in.readByte() != 0;
                this.private_is_hidden = in.readByte() != 0;
                this.sale_is_hidden = in.readByte() != 0;
            }

            public boolean is_hidden() {
                return is_hidden;
            }

            public void setIs_hidden(boolean is_hidden) {
                this.is_hidden = is_hidden;
            }

            public boolean is_authenticated() {
                return is_authenticated;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public CoachEntity getCoach() {
                return coach;
            }

            public void setCoach(CoachEntity coach) {
                this.coach = coach;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public GymEntity getGym() {
                return gym;
            }

            public void setGym(GymEntity gym) {
                this.gym = gym;
            }

            public boolean getIs_authenticated() {
                return is_authenticated;
            }

            public void setIs_authenticated(boolean is_authenticated) {
                this.is_authenticated = is_authenticated;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public int getPrivate_course() {
                return private_course;
            }

            public void setPrivate_course(int private_course) {
                this.private_course = private_course;
            }

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getGroup_user() {
                return group_user;
            }

            public void setGroup_user(int group_user) {
                this.group_user = group_user;
            }

            public String getSale() {
                return sale;
            }

            public void setSale(String sale) {
                this.sale = sale;
            }

            public String getStart() {
                return start;
            }

            public void setStart(String start) {
                this.start = start;
            }

            public int getGroup_course() {
                return group_course;
            }

            public void setGroup_course(int group_course) {
                this.group_course = group_course;
            }

            public int getPrivate_user() {
                return private_user;
            }

            public void setPrivate_user(int private_user) {
                this.private_user = private_user;
            }

            public boolean isGroup_is_hidden() {
                return group_is_hidden;
            }

            public void setGroup_is_hidden(boolean group_is_hidden) {
                this.group_is_hidden = group_is_hidden;
            }

            public boolean isPrivate_is_hidden() {
                return private_is_hidden;
            }

            public void setPrivate_is_hidden(boolean private_is_hidden) {
                this.private_is_hidden = private_is_hidden;
            }

            public boolean isSale_is_hidden() {
                return sale_is_hidden;
            }

            public void setSale_is_hidden(boolean sale_is_hidden) {
                this.sale_is_hidden = sale_is_hidden;
            }

            @Override public int describeContents() {
                return 0;
            }

            @Override public void writeToParcel(Parcel dest, int flags) {
                dest.writeParcelable(this.coach, flags);
                dest.writeString(this.description);
                dest.writeByte(this.is_authenticated ? (byte) 1 : (byte) 0);
                dest.writeString(this.position);
                dest.writeInt(this.private_course);
                dest.writeString(this.end);
                dest.writeString(this.name);
                dest.writeInt(this.id);
                dest.writeInt(this.group_user);
                dest.writeString(this.sale);
                dest.writeString(this.start);
                dest.writeInt(this.group_course);
                dest.writeInt(this.private_user);
                dest.writeParcelable(this.gym, flags);
                dest.writeByte(this.is_hidden ? (byte) 1 : (byte) 0);
                dest.writeByte(this.group_is_hidden ? (byte) 1 : (byte) 0);
                dest.writeByte(this.private_is_hidden ? (byte) 1 : (byte) 0);
                dest.writeByte(this.sale_is_hidden ? (byte) 1 : (byte) 0);
            }

            public static class GymEntity implements Parcelable {
                public static final Creator<GymEntity> CREATOR = new Creator<GymEntity>() {
                    @Override public GymEntity createFromParcel(Parcel source) {
                        return new GymEntity(source);
                    }

                    @Override public GymEntity[] newArray(int size) {
                        return new GymEntity[size];
                    }
                };
                private long id;
                private String name;
                private String photo;
                private DistrictEntity district;
                private boolean is_authenticated;
                private String address;
                private String brand_name;

                public GymEntity() {
                }

                protected GymEntity(Parcel in) {
                    this.id = in.readLong();
                    this.name = in.readString();
                    this.photo = in.readString();
                    this.district = in.readParcelable(DistrictEntity.class.getClassLoader());
                    this.is_authenticated = in.readByte() != 0;
                    this.address = in.readString();
                    this.brand_name = in.readString();
                }

                public String getAddress() {
                    if (!TextUtils.isEmpty(address)) return address;
                    StringBuffer sb = new StringBuffer();
                    if (district != null && district.city != null) {

                        if (!TextUtils.isEmpty(district.city.name)) {
                            sb.append(district.city.name).append("    ");
                        }
                    }
                    sb.append(brand_name);
                    return sb.toString();
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getBrand_name() {
                    return brand_name;
                }

                public void setBrand_name(String brand_name) {
                    this.brand_name = brand_name;
                }

                public DistrictEntity getDistrict() {
                    return district;
                }

                public void setDistrict(DistrictEntity district) {
                    this.district = district;
                }

                public String getPhoto() {
                    return photo;
                }

                public void setPhoto(String photo) {
                    this.photo = photo;
                }

                public boolean is_authenticated() {
                    return is_authenticated;
                }

                public void setIs_authenticated(boolean is_authenticated) {
                    this.is_authenticated = is_authenticated;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                @Override public int describeContents() {
                    return 0;
                }

                @Override public void writeToParcel(Parcel dest, int flags) {
                    dest.writeLong(this.id);
                    dest.writeString(this.name);
                    dest.writeString(this.photo);
                    dest.writeParcelable(this.district, flags);
                    dest.writeByte(this.is_authenticated ? (byte) 1 : (byte) 0);
                    dest.writeString(this.address);
                    dest.writeString(this.brand_name);
                }
            }

            public static class CoachEntity implements Parcelable {
                public static final Creator<CoachEntity> CREATOR = new Creator<CoachEntity>() {
                    public CoachEntity createFromParcel(Parcel source) {
                        return new CoachEntity(source);
                    }

                    public CoachEntity[] newArray(int size) {
                        return new CoachEntity[size];
                    }
                };
                /**
                 * id : 6
                 */

                private int id;

                public CoachEntity() {
                }

                protected CoachEntity(Parcel in) {
                    this.id = in.readInt();
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                @Override public int describeContents() {
                    return 0;
                }

                @Override public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.id);
                }
            }
        }
    }
}
