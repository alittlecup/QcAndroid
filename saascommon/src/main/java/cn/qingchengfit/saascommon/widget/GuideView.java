package cn.qingchengfit.saascommon.widget;

import android.app.Activity;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.utils.PreferenceUtils;

/**
 * Created by Bob Du on 2018/10/9 15:27
 */
public class GuideView {
  private static final String TAG = "GuideView";
  private static GuideView instance = null;
  private Activity activity;
  private String flag;
  private LayoutInflater inflater;
  private LinearLayout guideContainer;
  private View guideView;
  private View nextView;
  private View contentView;
  private Button guideButton;
  private QcScrollView guideParent;
  private ScrollView scrollView;
  private int[] guidesRes;
  private int guideRes;
  private int guideCount;
  private boolean isFirst;

  public static GuideView getInstance() {
    synchronized (GuideView.class) {
      if (instance == null) {
        instance = new GuideView();
      }
    }
    return instance;
  }

  public void initGuide(Activity activity, String flag) {
    this.activity = activity;
    this.flag = flag;
    inflater = LayoutInflater.from(activity);
    isFirst = PreferenceUtils.getPrefBoolean(activity, flag, true);
    ViewGroup.LayoutParams layoutParams =
        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    guideView = inflater.inflate(R.layout.view_crm, null);
    guideContainer = guideView.findViewById(R.id.guide_container);
    activity.addContentView(guideView, layoutParams);
  }

  public void initGuide(Activity activity, String flag, ScrollView scrollView) {
    this.scrollView = scrollView;
    initGuide(activity, flag);
  }

  public void addGuide(int guideRes) {
    this.guideRes = guideRes;
    beginGuide();
  }

  public void addGuide(int[] guidesRes) {
    this.guidesRes = guidesRes;
    guideCount = guidesRes.length;
    Log.i(TAG, "guideCount: " + guideCount);
    beginGuide(0);
  }

  public void setNoScroll() {
    if (guideParent != null) {
      guideParent.setOnTouchListener(new View.OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
          return true;
        }
      });
    }
  }

  private void beginGuide() {
    if (isFirst == true) {
      contentView = inflater.inflate(guideRes, null);
      guideContainer.addView(contentView);
      guideButton = contentView.findViewById(R.id.guide_btn);
      guideParent = contentView.findViewById(R.id.guide_parent);
      guideButton.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          endGuide();
        }
      });
    }
  }

  private void beginGuide(int index) {
    if (isFirst == true) {
      Log.i(TAG, "index: " + index);
      if (index == 0) {
        contentView = inflater.inflate(guidesRes[index], null);
        guideContainer.addView(contentView);
      } else {
        contentView = nextView;
      }
      guideButton = contentView.findViewById(R.id.guide_btn);
      guideParent = contentView.findViewById(R.id.guide_parent);
      setGuideClickListener(guideButton, index);

      guideParent.setOnScrollListener(new QcScrollView.OnScrollListener() {
        @Override
        public void onScroll(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
          if (scrollView != null) {
            scrollView.scrollTo(scrollX, scrollY);
          }
        }
      });
    }
  }

  private void changeGuide(int index) {
    nextView = inflater.inflate(guidesRes[index + 1], null);
    guideContainer.removeView(contentView);
    guideContainer.addView(nextView);
    beginGuide(index + 1);
  }

  private void endGuide() {
    guideContainer.removeView(contentView);
    PreferenceUtils.setPrefBoolean(activity, flag, false);
  }

  private void setGuideClickListener(Button button, final int index) {
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (index == guideCount - 1) {
          endGuide();
        } else {
          changeGuide(index);
        }
      }
    });
  }
}