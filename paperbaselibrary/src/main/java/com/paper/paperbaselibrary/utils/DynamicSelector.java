package com.paper.paperbaselibrary.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

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
 * Created by Paper on 15/8/14 2015.
 */
public class DynamicSelector {


    /**
     * 动态返回selector
     *
     * @param drawableNormal
     * @param drawableChecked
     * @return
     */
    public static StateListDrawable getSelector(Drawable drawableNormal, Drawable drawableChecked) {
        StateListDrawable bg = new StateListDrawable();
        drawableNormal.getBounds();
        bg.addState(new int[]{android.R.attr.state_checked}, drawableChecked);
        bg.addState(new int[]{}, drawableNormal);
        return bg;
    }
}
