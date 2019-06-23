package cn.qingchengfit.model.responese;

import cn.qingchengfit.model.base.Space;
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
 * 某个健身房的场地列表
 * <p>
 * Created by Paper on 16/3/22 2016.
 */
public class GymSites {
    @SerializedName("spaces") public List<Space> spaces;
}
