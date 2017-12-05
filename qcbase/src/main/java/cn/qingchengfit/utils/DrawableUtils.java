package cn.qingchengfit.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by fb on 2017/11/14.
 */

public class DrawableUtils {
  public  static Drawable generateBg(float radius, int[] colors){
    GradientDrawable
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
    gradientDrawable.setCornerRadius(radius);
    return gradientDrawable;
  }
}
