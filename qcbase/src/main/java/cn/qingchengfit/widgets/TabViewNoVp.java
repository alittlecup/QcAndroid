package cn.qingchengfit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.utils.AppUtils;
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
 * Created by Paper on 16/2/16 2016.
 */
public class TabViewNoVp extends LinearLayout implements View.OnClickListener {

  private int mChildSize;
  private List<TabItem> mTabItems;

  private int mTextSize = 10;
  private int mTextColorSelect = 0x0db14b;
  private int mTextColorNormal = 0xbbbbbb;
  private int mPadding = 10;
  private int[] resDrawableOn;
  private int[] resDrawableOff;
  private String[] resTxt;
  private TabSelectListener listener;

  public TabViewNoVp(Context context) {
    this(context, null);
  }

  public TabViewNoVp(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TabViewNoVp(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mTextColorSelect = AppUtils.getPrimaryColor(context);
    TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.TabView);
    int N = typedArray.getIndexCount();
    for (int i = 0; i < N; i++) {
      int i1 = typedArray.getIndex(i);
      if (i1 == R.styleable.TabView_text_size) {
        mTextSize = (int) typedArray.getDimension(i,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize,
                getResources().getDisplayMetrics()));
      } else if (i1 == R.styleable.TabView_text_normal_color) {
        mTextColorNormal = typedArray.getColor(i, mTextColorNormal);
      } else if (i1 == R.styleable.TabView_text_select_color) {
        mTextColorSelect = typedArray.getColor(i, mTextColorSelect);
      } else if (i1 == R.styleable.TabView_item_padding) {
        mPadding = (int) typedArray.getDimension(i,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPadding,
                getResources().getDisplayMetrics()));
      } else if (i1 == R.styleable.TabView_tvn_txt) {
        int txt = typedArray.getResourceId(i1, -1);
        if (txt > 0) {
          resTxt = context.getResources().getStringArray(txt);
        }
      }
    }
    if (resTxt != null) {
      mChildSize = resTxt.length;
    }
    typedArray.recycle();
    mTabItems = new ArrayList<>();
  }

  public void setupTabView(int[] resOn, int[] resOff) {
    resDrawableOn = resOn;
    resDrawableOff = resOff;
    initItem();
    mTabItems.get(0).setTabAlpha(1);
  }

  public void setBottomText(String[] textOn) {
    this.resTxt = textOn;
    mChildSize = resTxt.length;
  }

  private void initItem() {
    for (int i = 0; i < mChildSize; i++) {
      TabItem tabItem = new TabItem(getContext());
      LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
      tabItem.setPadding(mPadding, mPadding, mPadding, mPadding);
      int icon[] = new int[2];
      icon[0] = resDrawableOn[i];
      icon[1] = resDrawableOff[i];
      if (mChildSize == 5 && i == 2&&!resTxt[i].equals("发现")) {
        tabItem.setIconText(icon, new String[] { "" ,resTxt[i]});
      } else {
        tabItem.setIconText(icon, new String[] { resTxt[i], resTxt[i] });
      }
      tabItem.setTextSize(mTextSize);
      tabItem.setTextColorNormal(mTextColorNormal);
      tabItem.setTextColorSelect(mTextColorSelect);
      tabItem.setLayoutParams(params);
      tabItem.setTag(i);
      tabItem.setOnClickListener(this);
      mTabItems.add(tabItem);
      addView(tabItem);
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override public void onClick(View v) {
    int position = (Integer) v.getTag();
    setCurrentItem(position);
  }

  public void setCurrentItem(int pos) {
    for (TabItem tabItem : mTabItems) {
      tabItem.setTabAlpha(0);
    }
    mTabItems.get(pos).setTabAlpha(1);
    if (listener != null) listener.onTabClick(pos);
  }

  /**
   * 设置某位置是否显示小红点
   *
   * @param position 位置
   * @param isShow true--show；false--dismiss
   */
  public void setPointStatu(int position, boolean isShow) {
    if (position >= 0 && position < mTabItems.size()) {
      mTabItems.get(position).setPoint(isShow ? 1 : 0);
    }
  }

  /*set red point */
  public void setPoint(int position) {
    if (position > 0 && position < mTabItems.size()) mTabItems.get(position).setPoint(1);
  }

  /*clear red point */
  public void clearPoint(int position) {
    if (position > 0 && position < mTabItems.size()) mTabItems.get(position).setPoint(0);
  }

  public TabSelectListener getListener() {
    return listener;
  }

  public void setListener(TabSelectListener listener) {
    this.listener = listener;
  }

  public interface TabSelectListener {
    void onTabClick(int pos);
  }
}
