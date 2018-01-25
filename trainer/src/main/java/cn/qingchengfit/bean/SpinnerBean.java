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
 * Created by Paper on 15/10/11 2015.
 */
public class SpinnerBean {
    public String color;
    public String text;
    public boolean isTitle;
    public int id;
    public String model;

    public SpinnerBean(String color, String text, boolean isTitle) {
        this.color = color;
        this.text = text;
        this.isTitle = isTitle;
        this.id = 0;
    }

    public SpinnerBean(String color, String text, int id, String model) {
        this.color = color;
        this.text = text;
        this.isTitle = false;
        this.model = model;
        this.id = id;
    }
}