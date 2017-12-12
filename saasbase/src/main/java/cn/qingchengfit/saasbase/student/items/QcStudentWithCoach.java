package cn.qingchengfit.saasbase.student.items;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Trainer;

/**
 * Created by huangbaole on 2017/11/1.
 */

public class QcStudentWithCoach extends QcStudentBean implements Parcelable{

    public List<Trainer> coaches;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.coaches);
    }

    public QcStudentWithCoach() {
    }

    protected QcStudentWithCoach(Parcel in) {
        super(in);
        this.coaches = in.createTypedArrayList(Trainer.CREATOR);
    }

    public static final Parcelable.Creator<QcStudentWithCoach> CREATOR = new Parcelable.Creator<QcStudentWithCoach>() {
        @Override
        public QcStudentWithCoach createFromParcel(Parcel source) {
            return new QcStudentWithCoach(source);
        }

        @Override
        public QcStudentWithCoach[] newArray(int size) {
            return new QcStudentWithCoach[size];
        }
    };
}
