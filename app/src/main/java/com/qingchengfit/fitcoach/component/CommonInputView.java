package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
 * Created by Paper on 15/9/8 2015.
 */
public class CommonInputView extends RelativeLayout {

    private TextView label;
    private EditText edit;

    private String str_label;

    public CommonInputView(Context context) {
        super(context);
    }

    public CommonInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommonInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_commoninput, this, true);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonInputView);

        str_label = ta.getString(R.styleable.CommonInputView_civ_lable);
        ta.recycle();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        label = (TextView) findViewById(R.id.commoninput_lable);
        edit = (EditText) findViewById(R.id.commoninput_edit);
        label.setText(str_label);

    }

    public String getContent() {
        return edit.getText().toString().trim();
    }

    public void setContent(String c) {
        edit.setText(c);
        edit.setSelection(c.length());
    }

//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        edit.setFocusable(false);
//        super.setOnClickListener(l);
//    }
}
