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
public class QcExperienceResponse extends QcResponse {


    /**
     * data : {"total_count":1,"current_page":1,"experiences":[{"coach":{"id":6},"description":"无","is_authenticated":false,"position":"教练","private_course":421,"city":"北京","end":"2015-11-16T15:29:00","name":"中美引力工作室","group_user":123,"sale":10000000,"start":"2015-09-16T15:29:00","group_course":100,"private_user":568}],"pages":1}
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
         * experiences : [{"coach":{"id":6},"description":"无","is_authenticated":false,"position":"教练","private_course":421,"city":"北京","end":"2015-11-16T15:29:00","name":"中美引力工作室","group_user":123,"sale":10000000,"start":"2015-09-16T15:29:00","group_course":100,"private_user":568}]
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
            public static final Parcelable.Creator<ExperiencesEntity> CREATOR = new Parcelable.Creator<ExperiencesEntity>() {
                public ExperiencesEntity createFromParcel(Parcel source) {
                    return new ExperiencesEntity(source);
                }

                public ExperiencesEntity[] newArray(int size) {
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
             * name : 中美引力工作室
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
            private String city;
            private String end;
            private String name;
            private int group_user;
            private int sale;
            private String start;
            private int group_course;
            private int private_user;

            public ExperiencesEntity() {
            }

            protected ExperiencesEntity(Parcel in) {
                this.coach = in.readParcelable(CoachEntity.class.getClassLoader());
                this.description = in.readString();
                this.is_authenticated = in.readByte() != 0;
                this.position = in.readString();
                this.private_course = in.readInt();
                this.city = in.readString();
                this.end = in.readString();
                this.name = in.readString();
                this.group_user = in.readInt();
                this.sale = in.readInt();
                this.start = in.readString();
                this.group_course = in.readInt();
                this.private_user = in.readInt();
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

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
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

            public int getSale() {
                return sale;
            }

            public void setSale(int sale) {
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeParcelable(this.coach, flags);
                dest.writeString(this.description);
                dest.writeByte(is_authenticated ? (byte) 1 : (byte) 0);
                dest.writeString(this.position);
                dest.writeInt(this.private_course);
                dest.writeString(this.city);
                dest.writeString(this.end);
                dest.writeString(this.name);
                dest.writeInt(this.group_user);
                dest.writeInt(this.sale);
                dest.writeString(this.start);
                dest.writeInt(this.group_course);
                dest.writeInt(this.private_user);
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

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.id);
                }
            }
        }
    }
}
