package cn.qingchengfit.saasbase.network.body;

import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.network.model.Batch;
import cn.qingchengfit.saasbase.network.model.CourseTypeSample;
import cn.qingchengfit.saasbase.network.model.Shop;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
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
 * Created by Paper on 16/2/23 2016.
 */
public class SystemInitBody {
    @SerializedName("shop") public Shop shop;
    @SerializedName("spaces") public List<Space> spaces = new ArrayList<>();
    @SerializedName("courses") public List<CourseTypeSample> courses;
    @SerializedName("teachers") public List<Staff> teachers = new ArrayList<>();
    @SerializedName("card_tpls") public List<CardTpl> card_tpls;
    @SerializedName("batches") public List<Batch> batches;
    @SerializedName("brand_id") public String brand_id;
    public boolean auto_trial;
}
