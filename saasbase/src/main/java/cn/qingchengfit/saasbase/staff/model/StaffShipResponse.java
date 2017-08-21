package cn.qingchengfit.saasbase.staff.model;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.network.response.QcResponseData;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/5/6 2016.
 */
public class StaffShipResponse extends QcResponseData {

    @SerializedName("ships") public List<StaffShip> ships;
}
