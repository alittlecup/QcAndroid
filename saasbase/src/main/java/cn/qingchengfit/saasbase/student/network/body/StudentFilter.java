package cn.qingchengfit.saasbase.student.network.body;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StudentReferrerBean;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/5.
 */

/**
 * 0 # 新注册
 * 1 # 跟进中
 * 2 # 会员
 * 3 # 非会员
 */
public class StudentFilter implements Parcelable {

    public static final Creator<StudentFilter> CREATOR = new Creator<StudentFilter>() {
        @Override public StudentFilter createFromParcel(Parcel source) {
            return new StudentFilter(source);
        }

        @Override public StudentFilter[] newArray(int size) {
            return new StudentFilter[size];
        }
    };
    public String status;
    public String registerTimeStart;
    public String registerTimeEnd;
    //public String latestFollowupTimeStart;
    //public String latestFollowupTimeEnd;
    public StudentReferrerBean referrerBean;
    public String referrerId;
    public StudentSourceBean sourceBean;
    public String sourceId;
    public Staff sale;
    public String dayOff;
    public String birthdayStart;
    public String birthdayEnd;
    public String gender;
    public int timePos;

    private StudentFilter(Builder builder) {
        status = builder.status;
        registerTimeStart = builder.registerTimeStart;
        registerTimeEnd = builder.registerTimeEnd;
        //latestFollowupTimeStart = builder.latestFollowupTimeStart;
        //latestFollowupTimeEnd = builder.latestFollowupTimeEnd;
        referrerBean = builder.referrerBean;
        referrerId = builder.referrerId;
        sourceBean = builder.sourceBean;
        sourceId = builder.sourceId;
        sale = builder.sale;
        dayOff = builder.dayOff;
        birthdayStart = builder.birthdayStart;
        birthdayEnd = builder.birthdayEnd;
        gender = builder.gender;
    }

    public StudentFilter() {
    }

    protected StudentFilter(Parcel in) {
        this.status = in.readString();
        this.registerTimeStart = in.readString();
        this.registerTimeEnd = in.readString();
        //this.latestFollowupTimeStart = in.readString();
        //this.latestFollowupTimeEnd = in.readString();
        this.referrerBean = in.readParcelable(StudentReferrerBean.class.getClassLoader());
        this.referrerId = in.readString();
        this.sourceBean = in.readParcelable(StudentSourceBean.class.getClassLoader());
        this.sourceId = in.readString();
        this.sale = in.readParcelable(Staff.class.getClassLoader());
        this.dayOff = in.readString();
        this.birthdayStart = in.readString();
        this.birthdayEnd = in.readString();
        this.gender = in.readString();
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(status)
            && TextUtils.isEmpty(referrerId)
            && TextUtils.isEmpty(sourceId)
            && sourceBean == null
            && TextUtils.isEmpty(dayOff)
            && TextUtils.isEmpty(registerTimeEnd)
            && TextUtils.isEmpty(registerTimeStart)
            && TextUtils.isEmpty(birthdayStart)
            && TextUtils.isEmpty(birthdayEnd);
        //&& StringUtils.isEmpty(latestFollowupTimeStart)
        //&& StringUtils.isEmpty(latestFollowupTimeEnd);
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.registerTimeStart);
        dest.writeString(this.registerTimeEnd);
        //dest.writeString(this.latestFollowupTimeStart);
        //dest.writeString(this.latestFollowupTimeEnd);
        dest.writeParcelable(this.referrerBean, flags);
        dest.writeString(this.referrerId);
        dest.writeParcelable(this.sourceBean, flags);
        dest.writeString(this.sourceId);
        dest.writeParcelable(this.sale, flags);
        dest.writeString(this.dayOff);
        dest.writeString(this.birthdayStart);
        dest.writeString(this.birthdayEnd);
        dest.writeString(this.gender);
    }

    public static final class Builder {
        private String status;
        private String registerTimeStart;
        private String registerTimeEnd;
        private String latestFollowupTimeStart;
        private String latestFollowupTimeEnd;
        private StudentReferrerBean referrerBean;
        private String referrerId;
        private StudentSourceBean sourceBean;
        private String sourceId;
        private Staff sale;
        private String dayOff;
        private String birthdayStart;
        private String birthdayEnd;
        private String gender;

        public Builder() {
        }

        public Builder status(String val) {
            status = val;
            return this;
        }

        public Builder registerTimeStart(String val) {
            registerTimeStart = val;
            return this;
        }

        public Builder registerTimeEnd(String val) {
            registerTimeEnd = val;
            return this;
        }

        public Builder latestFollowupTimeStart(String val) {
            latestFollowupTimeStart = val;
            return this;
        }

        public Builder latestFollowupTimeEnd(String val) {
            latestFollowupTimeEnd = val;
            return this;
        }

        public Builder referrerBean(StudentReferrerBean val) {
            referrerBean = val;
            return this;
        }

        public Builder referrerId(String val) {
            referrerId = val;
            return this;
        }

        public Builder sourceBean(StudentSourceBean val) {
            sourceBean = val;
            return this;
        }

        public Builder sourceId(String val) {
            sourceId = val;
            return this;
        }

        public Builder sale(Staff val) {
            sale = val;
            return this;
        }

        public Builder dayOff(String val) {
            dayOff = val;
            return this;
        }

        public Builder birthdayStart(String val) {
            birthdayStart = val;
            return this;
        }

        public Builder birthdayEnd(String val) {
            birthdayEnd = val;
            return this;
        }

        public Builder gender(String val) {
            gender = val;
            return this;
        }

        public StudentFilter build() {
            return new StudentFilter(this);
        }
    }
}
