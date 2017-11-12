package cn.qingchengfit.saasbase.student.network.body;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.Trainer;

/**
 * Created by huangbaole on 2017/10/27.
 */

public class AllotDataResponse implements Parcelable {
    private Staff seller;
    private Trainer coach;
    private int count;
    private List<String> user = new ArrayList<>();

    public Staff getSeller() {
        return seller;
    }

    public void setSeller(Staff seller) {
        this.seller = seller;
    }

    public Trainer getCoach() {
        return coach;
    }

    public void setCoach(Trainer coach) {
        this.coach = coach;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getUser() {
        return user;
    }

    public void setUser(List<String> user) {
        this.user = user;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.seller, flags);
        dest.writeParcelable(this.coach, flags);
        dest.writeInt(this.count);
        dest.writeStringList(this.user);
    }

    public AllotDataResponse() {
    }

    protected AllotDataResponse(Parcel in) {
        this.seller = in.readParcelable(Staff.class.getClassLoader());
        this.coach = in.readParcelable(Trainer.class.getClassLoader());
        this.count = in.readInt();
        this.user = in.createStringArrayList();
    }

    public static final Parcelable.Creator<AllotDataResponse> CREATOR = new Parcelable.Creator<AllotDataResponse>() {
        @Override
        public AllotDataResponse createFromParcel(Parcel source) {
            return new AllotDataResponse(source);
        }

        @Override
        public AllotDataResponse[] newArray(int size) {
            return new AllotDataResponse[size];
        }
    };
}
