package com.qingchengfit.fitcoach.adapter;

import android.support.annotation.DrawableRes;

import java.util.HashMap;

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
 * Created by Paper on 15/12/28 2015.
 */
public class ImageTwoTextBean {
    public String imgUrl;
    public String text1;
    public String text2;
    public boolean showIcon;
    public boolean showRight;
    public int type = 0;//默认为0
    @DrawableRes
    public int rightIcon;
    public HashMap<String,String> tags = new HashMap<>();
    public ImageTwoTextBean(String imgUrl, String text1, String text2) {
        this.imgUrl = imgUrl;
        this.text1 = text1;
        this.text2 = text2;
        this.showIcon = false;
        this.showRight = false;
    }

    public ImageTwoTextBean(String imgUrl, String text1, String text2, boolean showIcon, boolean showRight) {
        this.imgUrl = imgUrl;
        this.text1 = text1;
        this.text2 = text2;
        this.showIcon = showIcon;
        this.showRight = showRight;
    }

    public ImageTwoTextBean(String imgUrl, String text1, String text2, boolean showIcon, boolean showRight, int rightIcon) {
        this.imgUrl = imgUrl;
        this.text1 = text1;
        this.text2 = text2;
        this.showIcon = showIcon;
        this.showRight = showRight;
        this.rightIcon = rightIcon;
    }
}
