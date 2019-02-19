package debug;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saascommon.BuildConfig;
import cn.qingchengfit.saascommon.utils.SpanUtils;
import cn.qingchengfit.saascommon.widget.LineCharDate;
import cn.qingchengfit.saascommon.widget.ModifiedFastScroller;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.FunnelTwoView;
import cn.qingchengfit.widgets.QcRadioGroup;
import com.github.mikephil.charting.data.LineData;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

/**
 * Created by huangbaole on 2017/11/15.
 */
public class BindingAdapters {

  /**
   * 自定义 DataBinding 控制 View 的显示和隐藏
   *
   * @param view View
   * @param show 是否显示
   */
  @BindingAdapter(value = "visibleGone") public static void showHide(View view, boolean show) {
    view.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  @BindingAdapter(value = "onRefresh")
  public static void onRefresh(SwipeRefreshLayout view,SwipeRefreshLayout.OnRefreshListener listener) {
    view.setOnRefreshListener(listener);
  }
  @BindingAdapter(value = "markViewUnit")
  public static void setLineDataUnit(LineCharDate lineCharDate, String unit) {
    lineCharDate.setMarkViewUnit(unit);
  }

  @BindingAdapter(value = { "datas", "highlightValue" }, requireAll = false)
  public static void setLineDataUnit(LineCharDate lineCharDate, LineData data, Integer pos) {
    if (pos == null) {
      lineCharDate.addData(data);
    } else {
      lineCharDate.addData(data, pos);
    }
  }

  @BindingAdapter(value = { "birDay", "birCount" }, requireAll = true)
  public static void setBirthContent(TextView textView, String day, String count) {
    if(TextUtils.isEmpty(day)||TextUtils.isEmpty(count))return;
    textView.setText(new SpanUtils().append(day)
        .setForegroundColor(textView.getResources().getColor(R.color.text_black))
        .append(count)
        .setForegroundColor(textView.getResources().getColor(R.color.colorPrimary))
        .append("人")
        .setForegroundColor(textView.getResources().getColor(R.color.text_black))
        .create());
  }

  @BindingAdapter(value = "afterTextChanged")
  public static void afterTextChanged(EditText view, AfterTextChangeListener listener) {
    view.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (s != null && s.length() > 0) {
          listener.afterTextChange(s.toString());
        } else {
          listener.afterTextChange("");
        }
      }
    });
  }

  public interface AfterTextChangeListener {
    void afterTextChange(String s);
  }

  /**
   * recycler的数据集合
   */

  @BindingAdapter(value = { "revert", "status" }) public static void setAdapterTag(
      RecyclerView recyclerView, boolean revert, int pos) {
    RecyclerView.Adapter adapter = recyclerView.getAdapter();
    if (adapter != null && adapter instanceof CommonFlexAdapter) {
      ((CommonFlexAdapter) adapter).setStatus(pos);
      ((CommonFlexAdapter) adapter).setTag("revert", revert);
    }
  }

  @BindingAdapter(value = { "itemClickListenter" })
  public static void setAdapterTag(RecyclerView recyclerView,
      FlexibleAdapter.OnItemClickListener listener) {
    RecyclerView.Adapter adapter = recyclerView.getAdapter();
    if (adapter != null && adapter instanceof CommonFlexAdapter) {
      ((CommonFlexAdapter) adapter).addListener(listener);
    }
  }

  @BindingAdapter(value = { "emptyDrawable", "emptyTitle", "emptyMsg" }, requireAll = false)
  public static void setEmptyItem(RecyclerView recyclerView, Drawable emptyDrawable, String title,
      String msg) {
    RecyclerView.Adapter adapter = recyclerView.getAdapter();
    if (adapter != null && adapter instanceof CommonFlexAdapter) {
      List mainItems = ((CommonFlexAdapter) adapter).getMainItems();
      if (mainItems.isEmpty()) {
        mainItems.add(new CommonNoDataItem(emptyDrawable, title, msg));
      }
      ((CommonFlexAdapter) adapter).updateDataSet(mainItems);
    }
  }

  /**
   * QcRadioGroup 清除选中
   */
  @BindingAdapter(value = "clearCheck") public static void setQcRadioGroupClear(QcRadioGroup group,
      boolean clear) {
    if (clear) group.clearCheck();
  }

  /**
   * FunnelTwoView 设置数据
   */
  @BindingAdapter(value = "datas") public static void setFunnelTwoViewDatas(FunnelTwoView view,
      List<Float> datas) {
    if (datas != null && !datas.isEmpty()) {
      view.setData(datas);
      view.animateY();
    }
  }

  /**
   * FastScroller 设置显示或隐藏
   */
  @BindingAdapter(value = "enable") public static void setFastScrollerEnable(
      ModifiedFastScroller fastScroller, boolean enable) {
    fastScroller.setEnabled(enable);
  }

  @BindingAdapter({ "android:onClick" })
  public static void setOnClickListener(View view, View.OnClickListener clickListener) {
    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Object tag = v.getTag(v.getId());
        long latestTime = tag == null ? 0 : (long) tag;

        long currentTime = System.currentTimeMillis();
        if (currentTime - latestTime > 600) {
          v.setTag(v.getId(), currentTime);
          if (BuildConfig.DEBUG) {
            Log.d("TAG", "currentTime:" + (currentTime - latestTime));
          }
          clickListener.onClick(v);
        } else {
          if (BuildConfig.DEBUG) {
            Log.d("TAG", "currentTime:" + (currentTime - latestTime));
          }
        }
      }
    });
  }
}
