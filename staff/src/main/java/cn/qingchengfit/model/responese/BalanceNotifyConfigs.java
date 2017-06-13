package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by fb on 2017/2/26.
 */

public class BalanceNotifyConfigs {
    @SerializedName("configs") public List<BalanceNotify> balanceNotifies;
}
