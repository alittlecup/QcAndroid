//package cn.qingchengfit.staffkit.allocate.coach.model;
//
//import android.os.Parcel;
//import cn.qingchengfit.model.base.Staff;
//
///**
// * Created by fb on 2017/5/4.
// */
//
//public class CoachBean extends Staff {
//
//  @Override public int describeContents() {
//    return 0;
//  }
//
//  @Override public void writeToParcel(Parcel dest, int flags) {
//    super.writeToParcel(dest, flags);
//  }
//
//  public CoachBean() {
//  }
//
//  protected CoachBean(Parcel in) {
//    super(in);
//  }
//
//  public static final Creator<CoachBean> CREATOR = new Creator<CoachBean>() {
//    @Override public CoachBean createFromParcel(Parcel source) {
//      return new CoachBean(source);
//    }
//
//    @Override public CoachBean[] newArray(int size) {
//      return new CoachBean[size];
//    }
//  };
//}
