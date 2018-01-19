package cn.qingchengfit.saasbase.cards.network.response;

import cn.qingchengfit.saasbase.cards.bean.NotifyIsOpen;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NotityIsOpenConfigs {
    @SerializedName("configs") public List<NotifyIsOpen> notifyIsOpens;
}