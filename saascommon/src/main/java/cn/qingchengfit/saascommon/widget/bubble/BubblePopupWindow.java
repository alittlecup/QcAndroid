package cn.qingchengfit.saascommon.widget.bubble;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.bigkoo.pickerview.lib.DensityUtil;

import cn.qingchengfit.utils.PreferenceUtils;

/**
 * Created by Bob Du on 2018/9/10 17:28
 */
public class BubblePopupWindow extends PopupWindow {
    private BubbleRelativeLayout bubbleView;
    private Context context;

    public BubblePopupWindow(Context context) {
        this.context = context;
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(false);
        setOutsideTouchable(false);
        setClippingEnabled(false);
        ColorDrawable dw = new ColorDrawable(0);
        setBackgroundDrawable(dw);
    }

    public void setBubbleView(View view, int identity) {
        bubbleView = new BubbleRelativeLayout(context, identity);
        bubbleView.setBackgroundColor(Color.TRANSPARENT);
        bubbleView.addView(view);
        setContentView(bubbleView);
    }

    public void setParam(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void show(View parent) {
        show(parent, Gravity.TOP, getMeasureWidth() / 2);
    }

    public void show(View parent, int gravity) {
        show(parent, gravity, getMeasureWidth() / 2);
    }

    public void show(View parent, int gravity, float bubbleOffset) {
        BubbleRelativeLayout.BubbleLegOrientation orientation = BubbleRelativeLayout.BubbleLegOrientation.LEFT;
        if (!this.isShowing()) {
            switch (gravity) {
                case Gravity.BOTTOM:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.TOP;
                    break;
                case Gravity.TOP:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.BOTTOM;
                    break;
                case Gravity.RIGHT:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.LEFT;
                    break;
                case Gravity.LEFT:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.RIGHT;
                    break;
                default:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.TOP;
                    break;
            }

            int screenWidth = PreferenceUtils.getPrefInt(context, "Screen Width", 0);
            int screenHeight = PreferenceUtils.getPrefInt(context, "Screen Height", 0);

            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            if(screenWidth <= 720) {
                bubbleView.setBubbleParams(orientation, screenWidth - bubbleOffset + 250);
            } else {
                bubbleView.setBubbleParams(orientation, screenWidth - bubbleOffset);
            }

            switch (gravity) {
                case Gravity.BOTTOM:
                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - parent.getWidth() - 80, location[1] + getMeasureHeight());
                    break;
                case Gravity.TOP:
                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - (parent.getWidth() / 2), location[1] - getMeasureHeight());
                    break;
                case Gravity.RIGHT:
                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] + parent.getWidth(), location[1]);
                    break;
                case Gravity.LEFT:
                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - getMeasureWidth(), location[1]);
                    break;
                default:
                    if(screenWidth <= 720) {
                        showAtLocation(parent, Gravity.NO_GRAVITY, screenWidth -20 - getMeasureWidth() + DensityUtil.px2dip(context,  gravity), location[1] + getMeasureHeight());
                    } else {
                        showAtLocation(parent, Gravity.NO_GRAVITY, screenWidth - getMeasureWidth() + DensityUtil.px2dip(context,  gravity), location[1] + getMeasureHeight());
                    }
                    break;
            }
        } else {
            this.dismiss();
        }
    }

    public int getMeasureHeight() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popHeight = getContentView().getMeasuredHeight();
        return popHeight;
    }

    public int getMeasureWidth() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popWidth = getContentView().getMeasuredWidth();
        return popWidth;
    }
}
