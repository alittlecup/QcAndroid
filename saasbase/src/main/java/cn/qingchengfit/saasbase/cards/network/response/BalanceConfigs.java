package cn.qingchengfit.saasbase.cards.network.response;

import cn.qingchengfit.saasbase.cards.bean.BalanceDetail;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BalanceConfigs {
    @SerializedName("configs") public List<BalanceDetail> balances;
}