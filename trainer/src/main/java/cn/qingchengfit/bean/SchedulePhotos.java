package cn.qingchengfit.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.qingchengfit.fitcoach.http.bean.QcScheduleBean;
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
 * Created by Paper on 16/8/3.
 */
public class SchedulePhotos implements Parcelable {
    public static final Creator<SchedulePhotos> CREATOR = new Creator<SchedulePhotos>() {
        @Override public SchedulePhotos createFromParcel(Parcel source) {
            return new SchedulePhotos(source);
        }

        @Override public SchedulePhotos[] newArray(int size) {
            return new SchedulePhotos[size];
        }
    };
    private String course_name;
    private String start;
    private String end;
    private String url;
    private Coach teacher;
    private QcScheduleBean.Shop shop;
    private List<SchedulePhoto> photos;
    private Long id;

    public SchedulePhotos() {
    }

    protected SchedulePhotos(Parcel in) {
        this.course_name = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.url = in.readString();
        this.teacher = in.readParcelable(Coach.class.getClassLoader());
        this.shop = in.readParcelable(QcScheduleBean.Shop.class.getClassLoader());
        this.photos = in.createTypedArrayList(SchedulePhoto.CREATOR);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public QcScheduleBean.Shop getShop() {
        return shop;
    }

    public void setShop(QcScheduleBean.Shop shop) {
        this.shop = shop;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Coach getTeacher() {
        return teacher;
    }

    public void setTeacher(Coach teacher) {
        this.teacher = teacher;
    }

    public List<SchedulePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<SchedulePhoto> photos) {
        this.photos = photos;
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
        dest.writeString(this.course_name);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeString(this.url);
        dest.writeParcelable(this.teacher, flags);
        dest.writeParcelable(this.shop, flags);
        dest.writeTypedList(this.photos);
        dest.writeValue(this.id);
    }
}
