package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import cn.qingchengfit.widgets.utils.MeasureUtils;
import com.qingchengfit.fitcoach.R;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2016/11/28.
 */

public class CalenderPopWindow {
    private View viewContent;
    public PopupWindow mPopupWindow;
    private Context mContext;
    private View.OnClickListener r;
    private View.OnClickListener g;
    private View.OnClickListener p;


    private CalenderPopWindow(Builder builder) {
        mContext = builder.mContext;
        this.r = builder.rest;
        this.g = builder.group;
        this.p = builder.privat;
        viewContent = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_schedule, null);
        if (r != null) viewContent.findViewById(R.id.layout_rest).setOnClickListener(r);
        if (g != null) viewContent.findViewById(R.id.layout_group).setOnClickListener(g);
        if (p != null) viewContent.findViewById(R.id.layout_private).setOnClickListener(p);
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(viewContent);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
    }

    public void show(View view, int x, int y) {
        int vw = view.getMeasuredWidth();
        if (mPopupWindow != null){
            viewContent.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int xoffset = 0;
            if (x-150 < vw/2)
                viewContent.setBackgroundResource(R.drawable.bg_timeline_card);
            else {
                viewContent.setBackgroundResource(R.drawable.bg_timeline_card_right);
                xoffset = MeasureUtils.dpToPx(150f,mContext.getResources());
            }

            mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY,x-xoffset, y+ MeasureUtils.dpToPx(52f,mContext.getResources()));

        }
    }
    public void setDismiss(PopupWindow.OnDismissListener dismiss){
        if (mPopupWindow != null){
            mPopupWindow.setOnDismissListener(dismiss);
        }
    }

    public static final class Builder {
        private Context mContext;
        private View.OnClickListener rest;
        private View.OnClickListener group;
        private View.OnClickListener privat;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder rest(View.OnClickListener listener) {
            this.rest = listener;
            return this;
        }

        public Builder group(View.OnClickListener listener) {
            this.group = listener;
            return this;
        }

        public Builder privat(View.OnClickListener listener) {
            this.privat = listener;
            return this;
        }

        public CalenderPopWindow build() {
            return new CalenderPopWindow(this);
        }
    }
}
