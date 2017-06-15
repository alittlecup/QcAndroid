package cn.qingchengfit.staffkit.views.main;

import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.Banner;
import cn.qingchengfit.model.responese.HomeStatement;
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
 * Created by Paper on 16/3/3 2016.
 */
public interface HomeView extends PView {
    void setBanner(List<Banner> strings);

    void setSpecialPoint(int count);

    void onBrands(List<Brand> brands);

    void goGuide();

    void estGyms(List<CoachService> coachServiceList);

    void setInfo(HomeStatement stats);
}
