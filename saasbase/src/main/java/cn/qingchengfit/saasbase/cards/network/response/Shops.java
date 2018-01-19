package cn.qingchengfit.saasbase.cards.network.response;

import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.saasbase.network.model.Shop;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by fb on 2017/12/19.
 */

public class Shops {
  @SerializedName("shops") public List<Shop> shops;
  @SerializedName("brand") public Brand brand;
}
