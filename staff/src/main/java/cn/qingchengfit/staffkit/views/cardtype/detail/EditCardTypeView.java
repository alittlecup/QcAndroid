package cn.qingchengfit.staffkit.views.cardtype.detail;

import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.staffkit.mvpbase.PView;

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
public interface EditCardTypeView extends PView {
    void onEditSucceess();

    void onEditFailed(String s);

    /**
     * @param name 卡名称
     * @param id 卡id
     * @param content 卡的用途 如充值12000元
     * @param lim 卡限制
     * @param desc 卡介绍
     * @param t 卡类型
     * @param valid 卡有效期
     * @param charge 卡支持的充值方式
     * @param in 实际收入
     */
    void onGetCardTypeInfo(String name, String id, String content, String lim, String desc, String t, String valid, String charge,
        String in);

    void onCardTpl(CardTpl card_tpl);

    void onSuccessShops();
}
