package cn.qingchengfit.saasbase.course.course.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;
import java.util.List;

public class CourseTeacher implements Parcelable {
    public static final Creator<CourseTeacher> CREATOR = new Creator<CourseTeacher>() {
        @Override public CourseTeacher createFromParcel(Parcel source) {
            return new CourseTeacher(source);
        }

        @Override public CourseTeacher[] newArray(int size) {
            return new CourseTeacher[size];
        }
    };
    private List<TeacherImpression> impressions;
    private Staff user;
    private float course_score;
    private float teacher_score;
    private float service_score;
    private Long id;

    public CourseTeacher() {
    }

    protected CourseTeacher(Parcel in) {
        this.impressions = in.createTypedArrayList(TeacherImpression.CREATOR);
        this.user = in.readParcelable(Staff.class.getClassLoader());
        this.course_score = in.readFloat();
        this.teacher_score = in.readFloat();
        this.service_score = in.readFloat();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public List<TeacherImpression> getImpressions() {
        return impressions;
    }

    public void setImpressions(List<TeacherImpression> impressions) {
        this.impressions = impressions;
    }

    public Staff getUser() {
        return user;
    }

    public void setUser(Staff user) {
        this.user = user;
    }

    public float getCourse_score() {
        return course_score;
    }

    public void setCourse_score(float course_score) {
        this.course_score = course_score;
    }

    public float getTeacher_score() {
        return teacher_score;
    }

    public void setTeacher_score(float teacher_score) {
        this.teacher_score = teacher_score;
    }

    public float getService_score() {
        return service_score;
    }

    public void setService_score(float service_score) {
        this.service_score = service_score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.impressions);
        dest.writeParcelable(this.user, flags);
        dest.writeFloat(this.course_score);
        dest.writeFloat(this.teacher_score);
        dest.writeFloat(this.service_score);
        dest.writeValue(this.id);
    }
}