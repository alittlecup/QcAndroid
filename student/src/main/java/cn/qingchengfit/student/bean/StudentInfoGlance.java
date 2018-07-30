package cn.qingchengfit.student.bean;

import java.util.List;

public class StudentInfoGlance {

  private int new_register_users_count = 0;
  private int new_member_users_count = 0;
  private int new_follow_users_count = 0;
  private int new_follow_member_users_count = 0;
  private int all_users_count = 0;
  private int registered_users_count = 0;
  private int following_users_count = 0;
  private int member_users_count = 0;

  public List<String> getToday_birthday_users() {
    return today_birthday_users;
  }

  private List<String> today_birthday_users;

  public StatData getInactive_registered() {
    return inactive_registered;
  }

  public void setInactive_registered(StatData inactive_registered) {
    this.inactive_registered = inactive_registered;
  }

  public StatData getInactive_following() {
    return inactive_following;
  }

  public void setInactive_following(StatData inactive_following) {
    this.inactive_following = inactive_following;
  }

  public StatData getInactive_member() {
    return inactive_member;
  }

  public void setInactive_member(StatData inactive_member) {
    this.inactive_member = inactive_member;
  }

  private  StatData inactive_registered;
  private  StatData inactive_following;
  private  StatData inactive_member;

  public void setToday_birthday_users(List<String> today_birthday_users) {
    this.today_birthday_users = today_birthday_users;
  }


  public int getNew_register_users_count() {
    return new_register_users_count;
  }

  public void setNew_register_users_count(int new_register_users_count) {
    this.new_register_users_count = new_register_users_count;
  }

  public int getNew_member_users_count() {
    return new_member_users_count;
  }

  public void setNew_member_users_count(int new_member_users_count) {
    this.new_member_users_count = new_member_users_count;
  }

  public int getNew_follow_users_count() {
    return new_follow_users_count;
  }

  public void setNew_follow_users_count(int new_follow_users_count) {
    this.new_follow_users_count = new_follow_users_count;
  }

  public int getNew_follow_member_users_count() {
    return new_follow_member_users_count;
  }

  public void setNew_follow_member_users_count(int new_follow_member_users_count) {
    this.new_follow_member_users_count = new_follow_member_users_count;
  }

  public int getAll_users_count() {
    return all_users_count;
  }

  public void setAll_users_count(int all_users_count) {
    this.all_users_count = all_users_count;
  }

  public int getRegistered_users_count() {
    return registered_users_count;
  }

  public void setRegistered_users_count(int registered_users_count) {
    this.registered_users_count = registered_users_count;
  }

  public int getFollowing_users_count() {
    return following_users_count;
  }

  public void setFollowing_users_count(int following_users_count) {
    this.following_users_count = following_users_count;
  }

  public int getMember_users_count() {
    return member_users_count;
  }

  public void setMember_users_count(int member_users_count) {
    this.member_users_count = member_users_count;
  }
}
