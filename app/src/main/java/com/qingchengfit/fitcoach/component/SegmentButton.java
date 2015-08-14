package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RadioButton;

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
 * Created by Paper on 15/8/8 2015.
 */
public class SegmentButton extends RadioButton {

    OnCheckedChangeListener mListener;
    private int mTextColor = Color.BLACK;

    public SegmentButton(Context context) {
        super(context);
        mTextColor = getResources().getColor(R.color.text_black);
        setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                SegmentButton.this.setTextColor(getResources().getColor(R.color.primary));
            } else SegmentButton.this.setTextColor(mTextColor);
        });
    }

    public SegmentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedArray array = attrs.getAttributeIntValue()
        init(attrs);
    }

    public SegmentButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mTextColor = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textColor", Color.BLACK);
        if (isChecked()) {
            setTextColor(getResources().getColor(R.color.primary));
        }
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SegmentButton.this.setTextColor(getResources().getColor(R.color.primary));
                } else SegmentButton.this.setTextColor(mTextColor);
            }
        });
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mListener = listener;
        super.setOnCheckedChangeListener(new SegmentChangeListener());
    }

    class SegmentChangeListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                SegmentButton.this.setTextColor(getResources().getColor(R.color.primary));
            } else SegmentButton.this.setTextColor(mTextColor);
            if (mListener != null)
                mListener.onCheckedChanged(compoundButton, b);
        }
    }

}
