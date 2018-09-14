package cn.qingchengfit.saascommon.widget.bubble;

import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.qingchengfit.utils.PreferenceUtils;

/**
 * Created by Bob Du on 2018/9/13 15:38
 */
public class BubbleViewUtil {
    public static void showBubbleOnce(View view, String text, String flag, int gravity, float bubbleOffset, int identity) {
        boolean isFirst = PreferenceUtils.getPrefBoolean(view.getContext(), flag, true);
        if (isFirst) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    BubblePopupView bubblePopupView = new BubblePopupView(view.getContext());
                    bubblePopupView.show(view, text, gravity, bubbleOffset, identity);
                    PreferenceUtils.setPrefBoolean(view.getContext(), flag, false);
                }
            });
        }
    }

    public static void showBubbleOnceDefaultToolbar(Toolbar view, String text, String flag, int identity) {
        showBubbleOnce(view,text,flag,75, 390, identity);
    }
}
