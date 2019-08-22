package cn.qingchengfit.student.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ClassRecords {
  public Stat stat;
  public List<AttendanceRecord> attendances;
  public List<Shop> shops;
  @SerializedName("request_user_id") public String requestUserId;

  public static class Stat {
    public int group;
    public int checkin;
    public int private_count;
    public int days;
  }

  /**
   * "photo": "https://img.qingchengfit.cn/3917f661232461874b8a43a3e437a1da.jpg",
   * "has_staff_permission": true,
   * "has_teacher_permission": true,
   * "name": "测试测试",
   * "id": 1
   * },
   */
  public static class Shop {
    public String photo;
    public boolean has_teacher_permission;
    public boolean has_staff_permission;
    public String name;
    public String id;
  }
}