package cn.qingchengfit.staffkit.views.gym;

import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.mvpbase.PView;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/1/28 2016.
 */
public interface ISetGymView extends PView {
    void setBrand();

    void setHeader(String args);

    void setAddress(String args);

    void setPhone(String args);

    void setDesc(String args);

    void onBrandList(List<Brand> brand);

    void onCreatGym();

    void onSuccess(CoachService coachService);

    void onFailed();

    void onUpdateGym();
}
