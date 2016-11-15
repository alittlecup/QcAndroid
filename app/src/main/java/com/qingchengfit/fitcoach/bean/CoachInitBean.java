package com.qingchengfit.fitcoach.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.qingchengfit.fitcoach.bean.base.Course;
import com.qingchengfit.fitcoach.bean.base.InitBatch;
import com.qingchengfit.fitcoach.bean.base.Shop;
import com.qingchengfit.fitcoach.bean.base.User;

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
 * Created by Paper on 16/11/15.
 */

public class CoachInitBean implements Parcelable {

    public String brand_id;
    public Shop shop;
    public List<User> teachers;
    public List<Course> courses;
    public List<InitBatch> batches;

    private CoachInitBean(Builder builder) {
        brand_id = builder.brand_id;
        shop = builder.shop;
        teachers = builder.teachers;
        courses = builder.courses;
        batches = builder.batches;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brand_id);
        dest.writeParcelable(this.shop, flags);
        dest.writeTypedList(this.teachers);
        dest.writeTypedList(this.courses);
        dest.writeTypedList(this.batches);
    }

    public CoachInitBean() {
    }

    protected CoachInitBean(Parcel in) {
        this.brand_id = in.readString();
        this.shop = in.readParcelable(Shop.class.getClassLoader());
        this.teachers = in.createTypedArrayList(User.CREATOR);
        this.courses = in.createTypedArrayList(Course.CREATOR);
        this.batches = in.createTypedArrayList(InitBatch.CREATOR);
    }

    public static final Creator<CoachInitBean> CREATOR = new Creator<CoachInitBean>() {
        @Override
        public CoachInitBean createFromParcel(Parcel source) {
            return new CoachInitBean(source);
        }

        @Override
        public CoachInitBean[] newArray(int size) {
            return new CoachInitBean[size];
        }
    };

    public static final class Builder {
        private String brand_id;
        private Shop shop;
        private List<User> teachers;
        private List<Course> courses;
        private List<InitBatch> batches;

        public Builder() {
        }

        public Builder brand_id(String val) {
            brand_id = val;
            return this;
        }

        public Builder shop(Shop val) {
            shop = val;
            return this;
        }

        public Builder teachers(List<User> val) {
            teachers = val;
            return this;
        }

        public Builder courses(List<Course> val) {
            courses = val;
            return this;
        }

        public Builder batches(List<InitBatch> val) {
            batches = val;
            return this;
        }

        public CoachInitBean build() {
            return new CoachInitBean(this);
        }
    }
}


