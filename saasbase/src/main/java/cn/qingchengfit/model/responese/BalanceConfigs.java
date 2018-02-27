package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by fb on 2017/2/24.
 */

public class BalanceConfigs {
    @SerializedName("configs") public List<BalanceDetail> balances;
}
