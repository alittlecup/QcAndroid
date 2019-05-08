package cn.qingchengfit.staffkit.views.signin.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserCheckinOrderResponse {
  @SerializedName("checkin")
  public List<UserCheckInOrder> orders;
}
