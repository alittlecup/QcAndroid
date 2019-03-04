package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.saascommon.bean.Shop;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by peggy on 16/5/28.
 */

public class Shops {

    @SerializedName("shops") public List<Shop> shops;
    @SerializedName("brand") public Brand brand;
}
