package cn.qingchengfit.saasbase.course.batch.network.response;

import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCourse;
import cn.qingchengfit.saasbase.course.batch.items.BatchItem;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QcResponsePrivateDetail extends QcResponse {

  @SerializedName("coach") public PrivateCoach coach;
  @SerializedName("batches") public List<PrivateBatch> batches;

  public class PrivateCoach implements BatchItem.BatchItemModel{
    @SerializedName("username") public String username;
    @SerializedName("user_id") public String user_id;
    @SerializedName("courses_count") public String course_count;
    @SerializedName("users_count") public String users_count;
    @SerializedName("avatar") public String avatar;
    @SerializedName("id") public String id;
    public int gender;

    @Override public String getId() {
      return id;
    }

    @Override public String getAvatar() {
      return avatar;
    }

    @Override public String getTitle() {
      return username;
    }

    @Override public String getText() {
      return "累计"+course_count+"节, 服务"+users_count+"人次";
    }

    public int getGender(){
      return gender;
    }

    @Override public boolean equals(Object o) {
      return o instanceof PrivateCoach && getId().equalsIgnoreCase(((PrivateCoach) o).getId());
    }
  }

  public class PrivateBatch implements BatchItem.BatchItemModel {
    @SerializedName("course") public BatchCourse course;
    @SerializedName("from_date") public String from_date;
    @SerializedName("to_date") public String to_date;
    @SerializedName("id") public String id;

    @Override public String getId() {
      return id;
    }

    @Override public String getAvatar() {

      return course == null ? "" : course.getPhoto();
    }

    @Override public String getTitle() {
      return from_date+"-"+to_date;
    }

    @Override public String getText() {
      return course == null ? "":course.getName();
    }

    @Override public boolean equals(Object o) {
      return o instanceof PrivateBatch && getId().equalsIgnoreCase(((PrivateBatch) o).getId());
    }
  }
}