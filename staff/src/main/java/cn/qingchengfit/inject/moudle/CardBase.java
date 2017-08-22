package cn.qingchengfit.inject.moudle;

import cn.qingchengfit.staffkit.constant.Configs;

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
 * Created by Paper on 16/3/23 2016.
 */
public class CardBase {

    public String cardid;
    public int cardtype = Configs.CATEGORY_VALUE;
    public String gymid;
    public String gymModel;

    public CardBase(String cardid, int cardtype, String gymid, String gymModel) {
        this.cardid = cardid;
        this.cardtype = cardtype;
        this.gymid = gymid;
        this.gymModel = gymModel;
    }
}
