package cn.qingchengfit.gym.bean;

import cn.qingchengfit.model.base.Shop;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ShopsResponse {
  @SerializedName("shops") public List<Shop> shops;

}
