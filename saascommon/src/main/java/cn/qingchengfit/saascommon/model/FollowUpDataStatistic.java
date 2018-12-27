package cn.qingchengfit.saascommon.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

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
 * //Created by yangming on 16/12/13.
 */

public class FollowUpDataStatistic {

    /**
     * new_create_users : {"count":1,"date_counts":[{"date":"2016-12-09","count":1}]}
     * new_following_users : {"count":2,"date_counts":[{"date":"2016-12-09","count":1}]}
     * new_member_users : {"count":3,"date_counts":[{"date":"2016-12-09","count":1}]}
     */

    public NewCreateUsersBean new_create_users;
    public NewCreateUsersBean new_following_users;
    public NewCreateUsersBean new_member_users;

    public static class NewCreateUsersBean implements Parcelable,IChartData<List<DateCountsBean>> {
        public static final Parcelable.Creator<NewCreateUsersBean> CREATOR = new Parcelable.Creator<NewCreateUsersBean>() {
            @Override public NewCreateUsersBean createFromParcel(Parcel source) {
                return new NewCreateUsersBean(source);
            }

            @Override public NewCreateUsersBean[] newArray(int size) {
                return new NewCreateUsersBean[size];
            }
        };
        /**
         * count : 1
         * date_counts : [{"date":"2016-12-09","count":1}]
         */

        public String count;
        public int today_count;
        public int week_count;
        public int month_count;
        public ArrayList<DateCountsBean> date_counts = new ArrayList<>();

        public NewCreateUsersBean() {
        }

        protected NewCreateUsersBean(Parcel in) {
            this.count = in.readString();
            this.today_count = in.readInt();
            this.week_count = in.readInt();
            this.month_count = in.readInt();
            this.date_counts = in.createTypedArrayList(DateCountsBean.CREATOR);
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.count);
            dest.writeInt(this.today_count);
            dest.writeInt(this.week_count);
            dest.writeInt(this.month_count);
            dest.writeTypedList(this.date_counts);
        }

        @Override public List<DateCountsBean> getData() {
            return date_counts;
        }
    }

    public static class DateCountsBean implements Parcelable {

        public static final Creator<DateCountsBean> CREATOR = new Creator<DateCountsBean>() {
            @Override public DateCountsBean createFromParcel(Parcel source) {
                return new DateCountsBean(source);
            }

            @Override public DateCountsBean[] newArray(int size) {
                return new DateCountsBean[size];
            }
        };
        /**
         * date : 2016-12-09
         * count : 1
         */

        public String date;
        public String count;

        public DateCountsBean(String date, String count) {
            this.date = date;
            this.count = count;
        }

        public DateCountsBean() {
        }

        protected DateCountsBean(Parcel in) {
            this.date = in.readString();
            this.count = in.readString();
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.date);
            dest.writeString(this.count);
        }
    }

    public static class NewFollowingUsersBean {
        /**
         * count : 2
         * date_counts : [{"date":"2016-12-09","count":1}]
         */

        public String count;
        public List<DateCountsBean> date_counts;
    }

    public static class NewMemberUsersBean {
        /**
         * count : 3
         * date_counts : [{"date":"2016-12-09","count":1}]
         */

        public String count;
        public List<DateCountsBean> date_counts;
    }
}
