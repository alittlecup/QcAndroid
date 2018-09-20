package cn.qingchengfit.saascommon.widget.bubble;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.qingchengfit.utils.PreferenceUtils;

/**
 * Created by Bob Du on 2018/9/13 15:38
 */
public class BubbleViewUtil {
    private BubblePopupView bubblePopupView;

    public BubbleViewUtil(Context context) {
        bubblePopupView = new BubblePopupView(context);
    }

    public void showBubbleOnce(View view, String text, String flag, int gravity, float bubbleOffset, int identity) {
        boolean isFirst = PreferenceUtils.getPrefBoolean(view.getContext(), flag, true);
        if (isFirst) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    bubblePopupView.show(view, text, gravity, bubbleOffset, identity);
                    PreferenceUtils.setPrefBoolean(view.getContext(), flag, false);
                }
            });
        }
    }

    public void showBubbleOnceDefaultToolbar(Toolbar view, String text, String flag, int identity) {
        showBubbleOnce(view,text,flag,10, 390, identity);
    }

    public void closeBubble() {
        if(bubblePopupView != null) {
            bubblePopupView.close();
        }
    }
}
