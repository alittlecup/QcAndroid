package cn.qingchengfit.gym.bean;

import cn.qingchengfit.network.response.QcListData;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GymApplyOrderResponses extends QcListData {
  @SerializedName("shop_staff_enter_applications") public List<GymApplyOrder> gymApplyOrderResponses;
}

