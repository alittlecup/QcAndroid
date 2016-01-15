package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.AppUtils;
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
 *
 */
public class CommonInputView extends RelativeLayout {

    private TextView label;
    private EditText edit;
    private String str_label;
    private boolean isNum;
    private boolean canClick;
    private boolean canBeNull;
    private View divider;
    private boolean showDivier;

    public CommonInputView(Context context) {
        super(context);
        showDivier = true;
        inflate(context, R.layout.layout_commoninput, this);
        onFinishInflate();
    }
//    public CommonInputView(Context context,String str_label) {
//        super(context);
//        inflate(context, R.layout.layout_commoninput, this);
//        this.str_label = str_label;
//        onFinishInflate();
//    }
//
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
        isNum = ta.getBoolean(R.styleable.CommonInputView_civ_inputnum, false);
        canClick = ta.getBoolean(R.styleable.CommonInputView_civ_clickable, false);
        canBeNull = ta.getBoolean(R.styleable.CommonInputView_civ_nonnull, false);
        showDivier = ta.getBoolean(R.styleable.CommonInputView_civ_showdivier,true);

        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        label = (TextView) findViewById(R.id.commoninput_lable);
        edit = (EditText) findViewById(R.id.commoninput_edit);
        divider = findViewById(R.id.commoninput_divider);

        if (!canBeNull)
            label.setText(str_label);
        else {
            SpannableString s = new SpannableString(str_label + " *");
            int l = s.length();
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), l - 1, l, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            label.setText(s);
        }
        if (canClick) {
            edit.setClickable(false);
        } else {
            edit.setClickable(true);
            setOnClickListener(v -> {
                edit.setClickable(true);
                edit.performClick();
                AppUtils.showKeyboard(getContext(), edit);
                edit.requestFocus();
            });
        }
        if (showDivier){
            divider.setVisibility(VISIBLE);
        }else {
            divider.setVisibility(GONE);
        }

        if (isNum) {


            edit.setInputType(InputType.TYPE_CLASS_PHONE);
        } else {

            edit.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
        if (canClick) {
            edit.setClickable(false);
        } else {
            edit.setClickable(true);
            setOnClickListener(v -> {
                edit.setClickable(true);
                edit.performClick();
                AppUtils.showKeyboard(getContext(), edit);
                edit.requestFocus();
            });
        }
    }

    public void setLabel(String s) {
        if (!canBeNull)
            label.setText(s);
        else {
            SpannableString sj = new SpannableString(s + " *");
            int l = sj.length();
            sj.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), l - 1, l, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            label.setText(sj);
        }
    }

    public String getContent() {
        return edit.getText().toString().trim();
    }

    public void setContent(String c) {
        edit.setText(c);
        if (c != null)
            edit.setSelection(c.length() > 20 ? 20 : c.length());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canClick)
            return true;
        return super.onInterceptTouchEvent(ev);
    }

}
