package cn.qingchengfit.model.responese;

import android.os.Parcel;
import cn.qingchengfit.saascommon.constant.Configs;
import java.util.ArrayList;
import java.util.List;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/7/29.
 * 课程种类详情
 */
public class CourseType extends CourseTypeSample implements Cloneable {

    public static final Creator<CourseType> CREATOR = new Creator<CourseType>() {
        @Override public CourseType createFromParcel(Parcel source) {
            return new CourseType(source);
        }

        @Override public CourseType[] newArray(int size) {
            return new CourseType[size];
        }
    };
    private List<String> photos;
    private int min_users;
    private List<CourseDetailTeacher> teachers;
    private List<Shop> shops;
    private Float course_score;
    private Float service_score;
    private Float teacher_score;
    private int capacity;// 单节可约人数
    private String description;
    private List<TeacherImpression> impressions;
    private CoursePlan plan;
    private int permission;//1 全部权限,2部分权限 3没有权限
    private int schedule_count;
    private String edit_url;
    private boolean random_show_photos;

    public CourseType() {
    }

    protected CourseType(Parcel in) {
        super(in);
        this.photos = in.createStringArrayList();
        this.min_users = in.readInt();
        this.teachers = in.createTypedArrayList(CourseDetailTeacher.CREATOR);
        this.shops = in.createTypedArrayList(Shop.CREATOR);
        this.course_score = (Float) in.readValue(Float.class.getClassLoader());
        this.service_score = (Float) in.readValue(Float.class.getClassLoader());
        this.teacher_score = (Float) in.readValue(Float.class.getClassLoader());
        this.capacity = in.readInt();
        this.description = in.readString();
        this.impressions = in.createTypedArrayList(TeacherImpression.CREATOR);
        this.plan = in.readParcelable(CoursePlan.class.getClassLoader());
        this.permission = in.readInt();
        this.schedule_count = in.readInt();
        this.edit_url = in.readString();
        this.random_show_photos = in.readByte() != 0;
    }

    public boolean isRandom_show_photos() {
        return random_show_photos;
    }

    public void setRandom_show_photos(boolean random_show_photos) {
        this.random_show_photos = random_show_photos;
    }

    public String getShopStr() {
        String ret = "";
        if (shops != null && shops.size() > 0) {
            for (int i = 0; i < shops.size(); i++) {
                ret = ret.concat(shops.get(i).name).concat(i == shops.size() - 1 ? "" : Configs.SEPARATOR);
            }
            return ret;
        } else {
            return "";
        }
    }

    public String getShopId() {
        String ret = "";
        if (shops != null && shops.size() > 0) {
            for (int i = 0; i < shops.size(); i++) {
                ret = ret.concat(shops.get(i).id).concat(i == shops.size() - 1 ? "" : ",");
            }
            return ret;
        } else {
            return "";
        }
    }

    public List<String> getShopIdList() {
        List<String> ret = new ArrayList<>();
        if (shops != null && shops.size() > 0) {
            for (int i = 0; i < shops.size(); i++) {
                ret.add(shops.get(i).id);
            }
        }
        return ret;
    }

    public String getEdit_url() {
        return edit_url;
    }

    public void setEdit_url(String edit_url) {
        this.edit_url = edit_url;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public CoursePlan getPlan() {
        return plan;
    }

    public void setPlan(CoursePlan plan) {
        this.plan = plan;
    }

    public Float getCourse_score() {
        return course_score;
    }

    public void setCourse_score(Float course_score) {
        this.course_score = course_score;
    }

    public int getSchedule_count() {
        return schedule_count;
    }

    public void setSchedule_count(int schedule_count) {
        this.schedule_count = schedule_count;
    }

    public Float getService_score() {
        return service_score;
    }

    public void setService_score(Float service_score) {
        this.service_score = service_score;
    }

    public Float getTeacher_score() {
        return teacher_score;
    }

    public void setTeacher_score(Float teacher_score) {
        this.teacher_score = teacher_score;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TeacherImpression> getImpressions() {
        return impressions;
    }

    public void setImpressions(List<TeacherImpression> impressions) {
        this.impressions = impressions;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public int getMin_users() {
        return min_users;
    }

    public void setMin_users(int min_users) {
        this.min_users = min_users;
    }

    public List<CourseDetailTeacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<CourseDetailTeacher> teachers) {
        this.teachers = teachers;
    }

    public List<Shop> getShops() {
        if (shops == null) return new ArrayList<>();
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeStringList(this.photos);
        dest.writeInt(this.min_users);
        dest.writeTypedList(this.teachers);
        dest.writeTypedList(this.shops);
        dest.writeValue(this.course_score);
        dest.writeValue(this.service_score);
        dest.writeValue(this.teacher_score);
        dest.writeInt(this.capacity);
        dest.writeString(this.description);
        dest.writeTypedList(this.impressions);
        dest.writeParcelable(this.plan, flags);
        dest.writeInt(this.permission);
        dest.writeInt(this.schedule_count);
        dest.writeString(this.edit_url);
        dest.writeByte(this.random_show_photos ? (byte) 1 : (byte) 0);
    }
}
