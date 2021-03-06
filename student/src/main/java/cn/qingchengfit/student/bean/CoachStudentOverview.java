package cn.qingchengfit.student.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CoachStudentOverview {

  private int new_registered_users_count;
  private int following_users_count;
  private int registered_users_count;
  private int member_users_count;
  private ShopPtagStat shop_ptag_stat;
  private int all_users_count;

  public int getNew_registered_users_count() {
    return new_registered_users_count;
  }

  public int getFollowing_users_count() {
    return following_users_count;
  }

  public int getRegistered_users_count() {
    return registered_users_count;
  }

  public int getMember_users_count() {
    return member_users_count;
  }

  public int getAll_user_count() {
    return all_users_count;
  }

  public ShopPtagStat getShop_ptag_stat() {
    return shop_ptag_stat;
  }

  @SerializedName(value = "attendance_stat") private AttendenceStat attendenceStat;

  public AttendenceStat getAttendenceStat() {
    return attendenceStat;
  }

  public void setAttendenceStat(AttendenceStat attendenceStat) {
    this.attendenceStat = attendenceStat;
  }

  public class CountAndAvatars {
    public int count;
    public List<String> avatars;
  }

  public class AttendenceStat {
    public CountAndAvatars absence;
    public CountAndAvatars attendance;
  }
}
