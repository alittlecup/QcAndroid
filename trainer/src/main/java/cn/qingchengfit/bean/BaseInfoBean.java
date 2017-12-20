package cn.qingchengfit.bean;

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
 * Created by Paper on 15/8/10 2015.
 */
public class BaseInfoBean {
    public int icon;
    public String label;
    public String content;

    public BaseInfoBean(int icon, String label, String content) {
        this.icon = icon;
        this.label = label;
        this.content = content;
    }
}
