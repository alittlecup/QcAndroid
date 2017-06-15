package cn.qingchengfit.staffkit.views.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.MeasureUtils;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/31.
 */
public class ExpandedLayout extends LinearLayout {
    View mContent;
    TextView mTvLable;
    SwitchCompat mSwitcher;
    Handler mHandler;
    private boolean isExpanded;
    private String label;
    // The height of the content when collapsed
    private int mCollapsedHeight = 0;
    // The full expanded height of the content (calculated)
    private int mContentHeight = 0;
    // How long the expand animation takes
    private int mAnimationDuration = 500;
    private CompoundButton.OnCheckedChangeListener mOtherListenr;
    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

            isExpanded = b;
            if (mOtherListenr != null) mOtherListenr.onCheckedChanged(compoundButton, b);

            if (b) {
                final ValueAnimator valueAnimator = ValueAnimator.ofInt(mCollapsedHeight, mContentHeight);
                valueAnimator.setDuration(mAnimationDuration);

                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override public void onAnimationUpdate(final ValueAnimator animator) {
                        ViewGroup.LayoutParams layoutParams = mContent.getLayoutParams();
                        layoutParams.height = (int) animator.getAnimatedValue();
                        mContent.setLayoutParams(layoutParams);
                        requestLayout();
                    }
                });

                valueAnimator.start();
            } else {
                final ValueAnimator valueAnimator = ValueAnimator.ofInt(mContentHeight, mCollapsedHeight);
                valueAnimator.setDuration(mAnimationDuration);

                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override public void onAnimationUpdate(final ValueAnimator animator) {
                        ViewGroup.LayoutParams layoutParams = mContent.getLayoutParams();
                        layoutParams.height = (int) animator.getAnimatedValue();
                        mContent.setLayoutParams(layoutParams);
                        requestLayout();
                    }
                });
                valueAnimator.start();
                //                mListener.onExpand(mHandle, mContent);
            }
        }
    };

    public ExpandedLayout(Context context) {
        super(context);
    }

    public ExpandedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mHandler = new Handler();
        setOrientation(VERTICAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandedLayout);
        isExpanded = ta.getBoolean(R.styleable.ExpandedLayout_el_expanded, false);
        label = ta.getString(R.styleable.ExpandedLayout_el_label);

        ta.recycle();
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener l) {
        this.mOtherListenr = l;
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        View view = inflate(getContext(), R.layout.layout_expandedlayout, null);
        mTvLable = (TextView) view.findViewById(R.id.label);
        mSwitcher = (SwitchCompat) view.findViewById(R.id.switcher);

        mSwitcher.setChecked(isExpanded);
        mTvLable.setText(label);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MeasureUtils.dpToPx(40f, getResources()));
        addView(view, 0, layoutParams);
        mContent = getChildAt(1);

        //        ViewCompat.setScaleY(mChildView,isExpanded?1:0);

        // This changes the height of the content such that it
        // starts off collapsed
        if (mContent != null) {
            android.view.ViewGroup.LayoutParams lp = mContent.getLayoutParams();
            lp.height = isExpanded ? mContentHeight : mCollapsedHeight;
            mContent.setLayoutParams(lp);
        }
        mSwitcher.setOnCheckedChangeListener(listener);
    }

    /**
     * This is where the magic happens for measuring the actual
     * (un-expanded) height of the content. If the actual height
     * is less than the collapsedHeight, the handle will be hidden.
     */
    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // First, measure how high content wants to be
        if (mContent != null) {
            mContent.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
            mContentHeight = mContent.getMeasuredHeight();
        }
        // Then let the usual thing happen
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
        mSwitcher.setChecked(expanded);
    }

    public void setLabel(String s) {
        mTvLable.setText(s);
    }
}
