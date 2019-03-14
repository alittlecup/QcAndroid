package cn.qingcheng.gym.bean;

import cn.qingchengfit.model.base.Shop;
import com.google.gson.annotations.SerializedName;

public class ShopCreateBody {
  @SerializedName("shop") public Shop shop;
  @SerializedName("brand_id") public String brand_id;
  public boolean auto_trial;
}
