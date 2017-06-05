package cn.qingchengfit.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by peggy on 16/5/24.
 */

public class CompatUtils {
    public static boolean less21(){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    public static int getColor(Context context, @ColorRes int res) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(res, null);
        }else return context.getResources().getColor(res);
    }

    public static void setBg(View view, Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else view.setBackgroundDrawable(drawable);
    }

    public static void removeGlobalLayout(ViewTreeObserver viewTreeObserver,ViewTreeObserver.OnGlobalLayoutListener layoutListener){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeOnGlobalLayoutListener(layoutListener);
        }else {
            viewTreeObserver.removeGlobalOnLayoutListener(layoutListener);
        }

    }
    public static void setCompoundDrawables(TextView view,Drawable left,Drawable top,Drawable right,Drawable bottom){
        if (Build.VERSION.SDK_INT >= 17) {
            view.setCompoundDrawablesRelative(left,top,right,bottom);
        }else {
            view.setCompoundDrawablesWithIntrinsicBounds(left,top,right,bottom);
        }
    }




}
