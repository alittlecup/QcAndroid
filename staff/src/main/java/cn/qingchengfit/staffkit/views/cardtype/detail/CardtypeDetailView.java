package cn.qingchengfit.staffkit.views.cardtype.detail;

import cn.qingchengfit.model.responese.CardStandard;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.staffkit.mvpbase.CView;
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
 * Created by Paper on 16/3/16 2016.
 */
public interface CardtypeDetailView extends CView {

    void onGetCardTypeInfo(CardTpl card_tpl);

    void onGetStandards(List<CardStandard> cardStandards);

    void onDelSucceess();

    void onDelFailed(String s);

    void onResumeOk();

    void onFailed(String s);
}
