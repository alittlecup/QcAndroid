package cn.qingchengfit.staffkit.views.student.bodytest;

import cn.qingchengfit.model.body.BodyTestBody;
import cn.qingchengfit.model.responese.BodyTestExtra;
import cn.qingchengfit.model.responese.BodyTestMeasure;
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
 * Created by Paper on 16/3/21 2016.
 */
public interface BodyTestView extends PView {
    void onShowPics(boolean ishow, List<BodyTestBody.Photo> pics);

    void onExtras(List<BodyTestExtra> extras);

    void onMeasure(BodyTestMeasure mMeasure);
}
