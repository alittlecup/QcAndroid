package com.qingchengfit.fitcoach.adapter;

import android.support.annotation.DrawableRes;

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
 * Created by Paper on 16/1/8 2016.
 */
public class ImageIconBean {
    public String content;
    @DrawableRes public int icon;
    public boolean showIcon;
    public String id;

    public ImageIconBean(String content, int icon) {
        this.content = content;
        this.icon = icon;
        this.showIcon = true;
    }

    public ImageIconBean(String content) {
        this.content = content;
        this.icon = 0;
        this.showIcon = false;
    }
}
