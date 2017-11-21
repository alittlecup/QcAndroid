package cn.qingchengfit.model.body;

import cn.qingchengfit.model.responese.CardTplOption;
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
 * Created by Paper on 16/4/12 2016.
 */
public class CardtplBody {
    public String name;
    public String id;
    public int type;
    public String description;
    public boolean is_limit;
    //    public int count;
    public int pre_times;//可提前上课
    public int day_times;//
    public int week_times;//
    public int month_times;//
    public int buy_limit;
    public String shops;//支持场馆 ,隔开
    public List<CardTplOption> options;
    public boolean is_open_service_term;

    public CardtplBody clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {

        }
        CardtplBody b = new CardtplBody();
        b.name = this.name;
        b.id = this.id;
        b.type = this.type;
        b.description = this.description;
        b.is_limit = this.is_limit;
        b.pre_times = this.pre_times;
        b.day_times = this.day_times;
        b.week_times = this.week_times;
        b.month_times = this.month_times;
        b.shops = this.shops;
        b.buy_limit = this.buy_limit;
        return b;
    }
}
