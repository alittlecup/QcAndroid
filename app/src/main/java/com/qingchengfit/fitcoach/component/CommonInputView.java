package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;

import cn.qingchengfit.widgets.utils.AppUtils;
import cn.qingchengfit.widgets.utils.MeasureUtils;

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
    private ImageView rightview;
    private int textColor = 0xff999999;
    private String str_hint;

    public boolean isNum() {
        return isNum;
    }

    public void setIsNum(boolean isNum) {
        this.isNum = isNum;
    }

    private boolean isNum;
    private boolean canClick;
    private boolean canBeNull;

    private boolean showDivier;
    private boolean showRight;
    private CharSequence str_content;
    @DrawableRes
    private int rightDrawable;
    int maxLength = 100;

    public CommonInputView(Context context) {
        super(context);
        showDivier = true;
        inflate(context, R.layout.layout_commoninput, this);
        onFinishInflate();
    }

    public CommonInputView(Context context, boolean isNum, String content) {
        super(context);
        str_content = content;
        showDivier = true;
        this.setIsNum(isNum);
        inflate(context, R.layout.layout_commoninput, this);
        onFinishInflate();
    }

    public CommonInputView(Context context, boolean isNum, boolean canBeNull, String content) {
        super(context);
        str_content = content;
        showDivier = true;
        this.canBeNull = canBeNull;
        this.setIsNum(isNum);
        inflate(context, R.layout.layout_commoninput, this);
        onFinishInflate();
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
        str_content = ta.getString(R.styleable.CommonInputView_civ_content);
        str_hint = ta.getString(R.styleable.CommonInputView_civ_hint);
        isNum = ta.getBoolean(R.styleable.CommonInputView_civ_inputnum, false);
        canClick = ta.getBoolean(R.styleable.CommonInputView_civ_clickable, false);
        canBeNull = ta.getBoolean(R.styleable.CommonInputView_civ_nonnull, false);
        showDivier = ta.getBoolean(R.styleable.CommonInputView_civ_showdivier, true);
        showRight = ta.getBoolean(R.styleable.CommonInputView_civ_showright, false);
        rightDrawable = ta.getResourceId(R.styleable.CommonInputView_civ_right_icon, R.drawable.ic_arrow_right);
        maxLength = ta.getResourceId(R.styleable.CommonInputView_civ_max_length, 100);
        textColor = ta.getColor(R.styleable.CommonInputView_civ_text_color, textColor);

        ta.recycle();
    }

    public boolean isShowRight() {
        return showRight;
    }

    public void setShowRight(boolean showRight) {
        this.showRight = showRight;
        rightview.setVisibility(showRight ? VISIBLE : GONE);
    }

    public void toggle() {
        setShowRight(!isShowRight());
    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState savedState = new SavedState(superState);
//        if (edit != null)
//            savedState.content = edit.getText().toString();
//        return savedState;
//    }

//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        SavedState savedState = (SavedState) state;
//        super.onRestoreInstanceState(savedState.getSuperState());
//
//        if (edit != null)
//            edit.setText(savedState.content);
//
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        label = (TextView) findViewById(R.id.commoninput_lable);
        edit = (EditText) findViewById(R.id.commoninput_edit);
        View divider = findViewById(R.id.commoninput_divider);
        rightview = (ImageView) findViewById(R.id.commoninput_righticon);
        if (!canBeNull)
            label.setText(str_label);
        else {
            SpannableString s = new SpannableString(str_label + " *");
            int l = s.length();
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), l - 1, l, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            label.setText(s);
        }
        label.setTextColor(textColor);

        if (canClick) {
//            edit.setClickable(false);
            edit.setFocusable(false);
            edit.setFocusableInTouchMode(false);

        } else {
//            edit.setClickable(true);
            edit.setFocusableInTouchMode(true);
            edit.setFocusable(true);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit.setClickable(true);
                    edit.requestFocus();
                    AppUtils.showKeyboard(getContext(), edit);
//                edit.performClick();
                }
            });

        }


        if (showDivier) {
            divider.setVisibility(VISIBLE);
        } else {
            divider.setVisibility(GONE);
        }
        rightview.setImageResource(rightDrawable);
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        if (showRight) {
            rightview.setVisibility(VISIBLE);
            edit.setPadding(edit.getPaddingLeft(), edit.getPaddingTop(), MeasureUtils.dpToPx(20f, getResources()), edit.getPaddingBottom());
        } else {
            rightview.setVisibility(GONE);
            edit.setPadding(edit.getPaddingLeft(), edit.getPaddingTop(), MeasureUtils.dpToPx(0f, getResources()), edit.getPaddingBottom());
        }
        if (isNum) {
            edit.setInputType(InputType.TYPE_CLASS_PHONE);
        } else {
            edit.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        edit.setText(str_content);
        edit.setHint(str_hint);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
        if (canClick) {
            edit.setClickable(false);
        } else {
            edit.setClickable(true);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit.setClickable(true);
                    edit.requestFocus();
                    AppUtils.showKeyboard(getContext(), edit);
                }
            });
        }
    }

    public void addTextWatcher(TextWatcher watcher) {
        edit.addTextChangedListener(watcher);
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

    public EditText getEditText() {
        return edit;
    }

    public String getLable() {
        return label.getText().toString();
    }


    public String getContent() {
//        if (TextUtils.isEmpty(edit.getText())) {
//            if (TextUtils.isEmpty(edit.getHint()))
//                return "";
//            else
//                return edit.getHint().toString();
//        } else
        return edit.getText().toString().trim();
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(edit.getText());
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public class SavedState extends BaseSavedState {
        String content;
        public final Creator<CommonInputView.SavedState> CREATOR = new Creator<CommonInputView.SavedState>() {
            @Override
            public CommonInputView.SavedState createFromParcel(Parcel in) {
                return new CommonInputView.SavedState(in);
            }

            @Override
            public CommonInputView.SavedState[] newArray(int size) {
                return new CommonInputView.SavedState[size];
            }
        };

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            content = in.readString();
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(content);
        }

    }

}
