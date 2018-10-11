package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.CompatUtils;

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
 * Created by Paper on 16/3/28 2016.
 */
public class SwitcherLayout extends LinearLayout {

  private String sw_lable;
  private boolean isShowDivier;
  private int expendLayout;
  private int textColor = 0x666666;
  private int background = 0xffffffff;
  private View expendView;
  private boolean open;
  private TextView lable;
  private SwitchCompat switchCompat;
  private LinearLayout linearLayout;
  private View divider;

  public SwitcherLayout(Context context) {
    super(context);
    textColor = CompatUtils.getColor(context, R.color.text_dark);
  }

  public SwitcherLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    textColor = CompatUtils.getColor(context, R.color.text_dark);
    init(context, attrs);
  }

  public SwitcherLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    textColor = CompatUtils.getColor(context, R.color.text_dark);
    init(context, attrs);
  }

  @RequiresApi(api = Build.VERSION_CODES.M) @Override protected void onFinishInflate() {
    super.onFinishInflate();
    lable = (TextView) findViewById(R.id.lable);
    divider = findViewById(R.id.divider);
    linearLayout = (LinearLayout) findViewById(R.id.root);
    switchCompat = (SwitchCompat) findViewById(R.id.switcher);
    linearLayout.setBackgroundColor(background);
    lable.setText(sw_lable);
    lable.setTextColor(textColor);
    divider.setVisibility(isShowDivier ? VISIBLE : GONE);
    switchCompat.setChecked(open);
    if (expendLayout != 0) {
      expendView = LayoutInflater.from(getContext()).inflate(expendLayout, this, false);
      linearLayout.addView(expendView);
      expendView.setVisibility(GONE);
    }
    switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (expendView != null) {
          //ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
          //if (isChecked) {
          //  animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          //    @Override public void onAnimationUpdate(ValueAnimator animation) {
          //      int height = expendView.getHeight();
          //      float animatedValue = (float) animation.getAnimatedValue();
          //      ViewGroup.LayoutParams layoutParams = expendView.getLayoutParams();
          //      layoutParams.height = (int) (height * animatedValue);
          //      expendView.setLayoutParams(layoutParams);
          //      linearLayout.requestLayout();
          //    }
          //  });
          //} else {
          //  animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          //    @Override public void onAnimationUpdate(ValueAnimator animation) {
          //      int height = expendView.getHeight();
          //      float animatedValue = (float) animation.getAnimatedValue();
          //      ViewGroup.LayoutParams layoutParams = expendView.getLayoutParams();
          //      layoutParams.height = (int) (height * (1 - animatedValue));
          //      expendView.setLayoutParams(layoutParams);
          //      linearLayout.requestLayout();
          //    }
          //  });
          //}
          //animator.setDuration(500);
          //animator.start();
          if (isChecked) {
            expendView.setVisibility(VISIBLE);
          } else {
            expendView.setVisibility(GONE);
          }
        }
      }
    });
    this.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        switchCompat.toggle();
      }
    });

  }

  public void init(Context context, AttributeSet attrs) {
    LayoutInflater.from(context).inflate(R.layout.layout_switch, this, true);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LayoutSwitcher);
    sw_lable = ta.getString(R.styleable.LayoutSwitcher_sw_lable);
    expendLayout = ta.getResourceId(R.styleable.LayoutSwitcher_sw_layout, 0);
    textColor = ta.getColor(R.styleable.LayoutSwitcher_sw_text_color, textColor);
    background = ta.getColor(R.styleable.LayoutSwitcher_sw_background, background);
    isShowDivier = ta.getBoolean(R.styleable.LayoutSwitcher_sw_showdivier, false);
    open = ta.getBoolean(R.styleable.LayoutSwitcher_sw_open, false);
    ta.recycle();
  }

  public void setOnCheckListener(CompoundButton.OnCheckedChangeListener listener) {
    switchCompat.setOnCheckedChangeListener(listener);
  }

  @Override public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    switchCompat.setEnabled(enabled);
    lable.setEnabled(enabled);
  }

  public boolean isOpen() {
    return switchCompat.isChecked();
  }

  public void setOpen(boolean open) {
    switchCompat.setChecked(open);
  }

  public void setShowDivier(boolean divier) {
    this.isShowDivier = divier;
    divider.setVisibility(isShowDivier ? VISIBLE : GONE);
  }

  public void setLabel(String lable) {
    this.lable.setText(lable);
  }

  public void setClickable(boolean clicable) {
    switchCompat.setClickable(clicable);
  }
}
