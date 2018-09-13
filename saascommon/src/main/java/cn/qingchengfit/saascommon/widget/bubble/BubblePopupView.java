package cn.qingchengfit.saascommon.widget.bubble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.qingchengfit.saascommon.R;

/**
 * Created by Bob Du on 2018/9/11 13:51
 */
public class BubblePopupView {

    private LayoutInflater inflater;
    private TextView guideText;
    private BubblePopupWindow bubblePopupWindow;

    public BubblePopupView(Context context) {
        inflater = LayoutInflater.from(context);
        bubblePopupWindow = new BubblePopupWindow(context);
    }

    public void show(View view, String content, int gravity, float bubbleOffset, int identity) {
        View bubbleView = inflater.inflate(R.layout.view_popup_bubble, null);
        guideText = bubbleView.findViewById(R.id.guide_text);
        guideText.setText(content);
        bubblePopupWindow.setBubbleView(bubbleView, identity);
        bubblePopupWindow.show(view, gravity, bubbleOffset);
    }
}
