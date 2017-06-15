package cn.qingchengfit.staffkit.views.gym;

import cn.qingchengfit.model.responese.CreatBrand;
import cn.qingchengfit.staffkit.mvpbase.PView;

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
public interface AddBrandView extends PView {
    void onSucceed(CreatBrand id);

    void onFailed(String s);
}
