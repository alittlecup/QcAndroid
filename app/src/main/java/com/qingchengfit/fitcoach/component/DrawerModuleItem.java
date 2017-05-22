package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
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
 * Created by Paper on 15/8/17 2015.
 */
public class DrawerModuleItem extends FrameLayout {

    TextView title;
    TextView count;

    public DrawerModuleItem(Context context) {
        super(context);
    }

    public DrawerModuleItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerModuleItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.drawer_module_title);
        count = (TextView) findViewById(R.id.drawer_module_count);
    }

    public void setTitle(String t) {
        title.setText(t);
    }

    public void setCount(String c) {
        count.setText(c);
    }
}
