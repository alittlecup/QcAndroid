package cn.qingchengfit.widgets;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.MeasureUtils;
import java.util.ArrayList;
import java.util.List;

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
public class AlphabetLessView extends LinearLayout {
    public static String Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#";
    public int height;
    public int cellHeight;
    private TextView alphaDialog;
    private List<String> alphabetStrings = new ArrayList<>();
    private List<Integer> rvPos = new ArrayList<>();
    private OnAlphabetChange onAlphabetChange;

    public AlphabetLessView(Context context) {
        super(context);
    }

    public AlphabetLessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlphabetLessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnAlphabetChange(OnAlphabetChange onAlphabetChange) {
        this.onAlphabetChange = onAlphabetChange;
    }

    public void addElement(String s,int x) {
        if (Alphabet.contains(s) && !alphabetStrings.contains(s)) {
            alphabetStrings.add(s);
            rvPos.add(x);
        }
    }

    public void clearElement() {
        alphabetStrings.clear();
        rvPos.clear();
    }

    public void init() {
        //this.cellHeight = 20;
        this.setOrientation(VERTICAL);
        this.setBackgroundResource(R.color.transparent);
        requestDisallowInterceptTouchEvent(true);
        //        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)getLayoutParams();
        removeAllViews();
        for (int i = 0; i < alphabetStrings.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(alphabetStrings.get(i).toUpperCase());
            textView.setTextSize(12);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER_VERTICAL | GravityCompat.END);
            //            textView.setPadding(10, 0, 10, 0);

            LayoutParams layoutParams = new LayoutParams(MeasureUtils.dpToPx(30f, getResources()), ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 0, 5, 0);
            addView(textView, layoutParams);
        }
        invalidate();
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        height = getMeasuredHeight() - getPaddingTop();
        if (alphabetStrings.size() > 0) cellHeight = height / alphabetStrings.size();
    }

    public void setAlphaDialog(TextView alphaDialog) {
        this.alphaDialog = alphaDialog;
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {

        float y = event.getY() - getPaddingTop();
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int position = (int) y / cellHeight;
                if (position < 0) position = 0;
                if (position < alphabetStrings.size() && onAlphabetChange != null) {
                    onAlphabetChange.onChange(position, alphabetStrings.get(position),rvPos.get(position));
                    if (alphaDialog != null) {
                        alphaDialog.setVisibility(VISIBLE);
                        alphaDialog.setText(alphabetStrings.get(position));
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (alphaDialog != null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            alphaDialog.setVisibility(GONE);
                        }
                    }, 300);
                }
                return true;
            default:
        }

        return true;
    }

    public interface OnAlphabetChange {
        void onChange(int position, String s,int RvPos);
    }

}
