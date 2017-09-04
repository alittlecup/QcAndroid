package cn.qingchengfit.widgets;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/10/20 2015.
 */
@Deprecated
public class AlphabetView extends LinearLayout {
    public static String[] alphabetStrings = new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V"
            , "W", "X", "Y", "Z", "#"
    };
    public static String Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public int height;
    public int cellHeight;
    private TextView alphaDialog;
    private OnAlphabetChange onAlphabetChange;

    public AlphabetView(Context context) {
        super(context);
        init();
    }

    public AlphabetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlphabetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnAlphabetChange(OnAlphabetChange onAlphabetChange) {
        this.onAlphabetChange = onAlphabetChange;
    }

    private void init() {
        this.setOrientation(VERTICAL);
        this.setBackgroundResource(R.color.transparent);
        requestDisallowInterceptTouchEvent(true);
        //        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)getLayoutParams();

        for (int i = 0; i < alphabetStrings.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(alphabetStrings[i]);
            textView.setTextSize(12);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
//            textView.setPadding(10, 0, 10, 0);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 0, 5, 0);
            addView(textView, layoutParams);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        height = getMeasuredHeight() - getPaddingTop();
        cellHeight = height / alphabetStrings.length;
    }

    public void setAlphaDialog(TextView alphaDialog) {
        this.alphaDialog = alphaDialog;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float y = event.getY() - getPaddingTop();
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
//                onAlphabetChange.isShowTialog(GONE, "");
//                return true;
//            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                int position = (int) y / cellHeight;
                if (position < 0)
                    position = 0;
                if (position < alphabetStrings.length && onAlphabetChange != null) {
                    onAlphabetChange.onChange(position, alphabetStrings[position]);
                    if (alphaDialog != null) {
                        alphaDialog.setVisibility(VISIBLE);
                        alphaDialog.setText(alphabetStrings[position]);
                    }
                }
                return true;

            case MotionEvent.ACTION_UP:
                if (alphaDialog != null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alphaDialog.setVisibility(GONE);

                        }
                    }, 300);
                }
                return true;
        }


        return super.onTouchEvent(event);

    }

    public interface OnAlphabetChange {
        void onChange(int position, String s);
    }

}
