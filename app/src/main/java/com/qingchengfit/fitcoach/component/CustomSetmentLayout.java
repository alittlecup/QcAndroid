package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
 * Created by Paper on 15/9/14 2015.
 */
public class CustomSetmentLayout extends LinearLayout {


    public CustomSetmentLayout(Context context) {
        super(context);
    }

    public CustomSetmentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSetmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {

    }

    @Override
    public void addView(View child) {
        super.addView(child);


    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        if (child instanceof SegmentLayout) {
            ((SegmentLayout) child).setSegmentListener(v ->
                            CustomSetmentLayout.this.onSegmentClick(v.getId())
            );
        }
    }

    //i  id
    void onSegmentClick(int id) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof onSegmentChangeListener) {
                if (v.getId() == id) {
                    ((onSegmentChangeListener) v).onCheckChange(true);
                } else ((onSegmentChangeListener) v).onCheckChange(false);
            }
        }
    }

    public void cleanCheck() {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof onSegmentChangeListener) {
                ((onSegmentChangeListener) v).onCheckChange(false);
            }
        }
    }

    public void check(int index) {
        if (getChildCount() > 0) {
            ((SegmentLayout) getChildAt(0)).onCheckChange(true);
        }
    }
    public interface onSegmentChangeListener {
        void onCheckChange(boolean isChecked);
    }


}
