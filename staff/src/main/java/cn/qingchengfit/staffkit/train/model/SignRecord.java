package cn.qingchengfit.staffkit.train.model;

import cn.qingchengfit.model.responese.QcResponsePage;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by fb on 2017/3/28.
 */

public class SignRecord extends QcResponsePage {
    @SerializedName("users") public List<SignPersonalBean> users;
    @SerializedName("total_price") public String totalPrice;
}
