package com.qingchengfit.fitcoach.http.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.DistrictEntity;
import cn.qingchengfit.network.response.QcResponse;
import java.util.ArrayList;
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
 * Created by Paper on 15/9/12 2015.
 */
public class QcMyhomeResponse extends QcResponse {

    /**
     * data : {"coach":{"username":"小碗","city":"北京","description":"","tags":{"tags":[{"count":"1.2k","username":"减脂"},{"count":"902","username":"增肌"}]},"weixin":"","evaluate":{"evaluate":{"course_score":4.7,"total_count":3658,"coach_score":4.8}},"phone":"13501200175","short_description":"我是一名健身教练","id":5}}
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
         * coach : {"username":"小碗","city":"北京","description":"","tags":{"tags":[{"count":"1.2k","username":"减脂"},{"count":"902","username":"增肌"}]},"weixin":"","evaluate":{"evaluate":{"course_score":4.7,"total_count":3658,"coach_score":4.8}},"phone":"13501200175","short_description":"我是一名健身教练","id":5}
         */

        private CoachEntity coach;

        public CoachEntity getCoach() {
            return coach;
        }

        public void setCoach(CoachEntity coach) {
            this.coach = coach;
        }

        public static class CoachEntity {
            /**
             * username : 小碗
             * city : 北京
             * description :
             * tags : {"tags":[{"count":"1.2k","username":"减脂"},{"count":"902","username":"增肌"}]}
             * weixin :
             * evaluate : {"evaluate":{"course_score":4.7,"total_count":3658,"coach_score":4.8}}
             * phone : 13501200175
             * short_description : 我是一名健身教练
             * id : 5
             */

            private String username;
            private String city;
            private String description;
            private DistrictEntity district;
            private List<TagsEntity> tags;
            private String weixin;
            private EvaluateEntity evaluate;
            private String phone;
            private int gender;
            private String short_description;
            private int id;
            private String avatar;

            public List<TagsEntity> getTags() {
                return tags;
            }

            public void setTags(List<TagsEntity> tags) {
                this.tags = tags;
            }

            public String[] getTagArray() {
                List<String> list = new ArrayList<>();
                for (TagsEntity tag : tags) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(tag.getName());
                    sb.append("  (");
                    sb.append(tag.getCount());
                    sb.append(")");
                    list.add(sb.toString());
                }
                String[] ret = list.toArray(new String[list.size()]);
                return ret;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public DistrictEntity getDistrict() {
                return district;
            }

            public void setDistrict(DistrictEntity district) {
                this.district = district;
            }

            public String getDistrictStr() {
                if (district != null && district.province != null && district.city != null) {
                    if (district.province.name.equalsIgnoreCase(district.city.name)) {
                        return district.city.name;
                    } else {
                        return district.province.name + district.city.name;
                    }
                } else {
                    return "";
                }
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getWeixin() {
                return weixin;
            }

            public void setWeixin(String weixin) {
                this.weixin = weixin;
            }

            public EvaluateEntity getEvaluate() {
                return evaluate;
            }

            public void setEvaluate(EvaluateEntity evaluate) {
                this.evaluate = evaluate;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getShort_description() {
                return short_description;
            }

            public void setShort_description(String short_description) {
                this.short_description = short_description;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }



            public static class EvaluateEntity implements Parcelable {
                public static final Parcelable.Creator<EvaluateEntity> CREATOR = new Parcelable.Creator<EvaluateEntity>() {
                    public EvaluateEntity createFromParcel(Parcel source) {
                        return new EvaluateEntity(source);
                    }

                    public EvaluateEntity[] newArray(int size) {
                        return new EvaluateEntity[size];
                    }
                };
                /**
                 * course_score : 4.7
                 * total_count : 3658
                 * coach_score : 4.8
                 */

                private double course_score;
                private int total_count;
                private double coach_score;

                public EvaluateEntity() {
                }

                protected EvaluateEntity(Parcel in) {
                    this.course_score = in.readDouble();
                    this.total_count = in.readInt();
                    this.coach_score = in.readDouble();
                }

                public double getCourse_score() {
                    return course_score;
                }

                public void setCourse_score(double course_score) {
                    this.course_score = course_score;
                }

                public int getTotal_count() {
                    return total_count;
                }

                public void setTotal_count(int total_count) {
                    this.total_count = total_count;
                }

                public double getCoach_score() {
                    return coach_score;
                }

                public void setCoach_score(double coach_score) {
                    this.coach_score = coach_score;
                }

                @Override public int describeContents() {
                    return 0;
                }

                @Override public void writeToParcel(Parcel dest, int flags) {
                    dest.writeDouble(this.course_score);
                    dest.writeInt(this.total_count);
                    dest.writeDouble(this.coach_score);
                }
            }

            public static class TagsEntity implements Parcelable {
                public static final Parcelable.Creator<TagsEntity> CREATOR = new Parcelable.Creator<TagsEntity>() {
                    public TagsEntity createFromParcel(Parcel source) {
                        return new TagsEntity(source);
                    }

                    public TagsEntity[] newArray(int size) {
                        return new TagsEntity[size];
                    }
                };
                /**
                 * count : 1.2k
                 * name : 减脂
                 */

                private String count;
                private String name;

                public TagsEntity() {
                }

                protected TagsEntity(Parcel in) {
                    this.count = in.readString();
                    this.name = in.readString();
                }

                public String getCount() {
                    return count;
                }

                public void setCount(String count) {
                    this.count = count;
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
                    dest.writeString(this.count);
                    dest.writeString(this.name);
                }
            }

            //            public static class TagsEntitys implements Parcelable {
            //                public static final Parcelable.Creator<TagsEntitys> CREATOR = new Parcelable.Creator<TagsEntitys>() {
            //                    public TagsEntitys createFromParcel(Parcel source) {
            //                        return new TagsEntitys(source);
            //                    }
            //
            //                    public TagsEntitys[] newArray(int size) {
            //                        return new TagsEntitys[size];
            //                    }
            //                };
            //                /**
            //                 * tags : [{"count":"1.2k","username":"减脂"},{"count":"902","username":"增肌"}]
            //                 */
            //
            //                private List<TagsEntity> tags;
            //
            //                public TagsEntitys() {
            //                }
            //
            //                protected TagsEntitys(Parcel in) {
            //                    this.tags = new ArrayList<TagsEntity>();
            //                    in.readList(this.tags, List.class.getClassLoader());
            //                }
            //
            //                public List<TagsEntity> getTags() {
            //                    return tags;
            //                }
            //
            //                public void setTags(List<TagsEntity> tags) {
            //                    this.tags = tags;
            //                }
            //
            //
            //
            //                @Override
            //                public int describeContents() {
            //                    return 0;
            //                }
            //
            //                @Override
            //                public void writeToParcel(Parcel dest, int flags) {
            //                    dest.writeList(this.tags);
            //                }
            //
            //
            //            }
        }
    }
}
