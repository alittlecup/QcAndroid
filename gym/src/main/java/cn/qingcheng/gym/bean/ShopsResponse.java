package cn.qingcheng.gym.bean;

import cn.qingchengfit.saascommon.bean.Shop;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ShopsResponse {
  @SerializedName("shops") public List<Shop> shops;

}
