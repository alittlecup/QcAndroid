package cn.qingchengfit.staffkit.rxbus.event;

import cn.qingchengfit.model.responese.CardTpl;
import java.util.List;

/**
 * Created by peggy on 16/6/7.
 */

public class RxCardTypeEvent {
    public List<CardTpl> datas;
    public int cardtype;

    public RxCardTypeEvent(List<CardTpl> datas, int cardtype) {
        this.datas = datas;
        this.cardtype = cardtype;
    }
}
