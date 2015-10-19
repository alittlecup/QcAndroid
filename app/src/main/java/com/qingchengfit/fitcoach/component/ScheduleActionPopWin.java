package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.qingchengfit.fitcoach.R;

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
 * Created by Paper on 15/10/17 2015.
 */
public class ScheduleActionPopWin {

    private final View view;
    PopupWindow popupWindow;


    public ScheduleActionPopWin(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_schedule_action, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);


    }

    public void setActionCallback(View.OnClickListener rest, View.OnClickListener group, View.OnClickListener privat) {
        view.findViewById(R.id.schedule_private_btn).setOnClickListener(privat);
        view.findViewById(R.id.schedule_rest_btn).setOnClickListener(rest);
        view.findViewById(R.id.schedule_group_btn).setOnClickListener(group);
    }


    public void show(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v.getRootView(), Gravity.NO_GRAVITY, location[0] - 200, location[1] - popupWindow.getHeight() - 300);
//        popupWindow.showAsDropDown(v,0,-300);
        popupWindow.update();
//        popupWindow.showAtLocation(v,Gravity.BOTTOM,0,30);
//        if (Build.VERSION.SDK_INT <19)
//            popupWindow.showAsDropDown(v,0,30);
//        else popupWindow.showAsDropDown(v,0,30, Gravity.TOP);
    }

    public void setOnDismissListenser(PopupWindow.OnDismissListener listenser) {
        popupWindow.setOnDismissListener(listenser);
    }

    public void dismiss() {
        popupWindow.dismiss();

    }


}
