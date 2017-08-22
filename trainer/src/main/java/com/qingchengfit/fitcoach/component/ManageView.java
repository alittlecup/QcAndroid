package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
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
 * Created by Paper on 16/1/6 2016.
 */
public class ManageView extends LinearLayout {

    public ManageView(Context context) {
        super(context);
        init(context);
    }

    public ManageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ManageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_manage, this, true);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        TextView textView1 = (TextView) findViewById(R.id.text1);
    }
}
