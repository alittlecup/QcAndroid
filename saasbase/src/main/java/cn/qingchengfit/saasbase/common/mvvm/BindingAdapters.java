package cn.qingchengfit.saasbase.common.mvvm;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.mikephil.charting.data.LineData;

import java.util.Calendar;
import java.util.List;

import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.saasbase.BuildConfig;
import cn.qingchengfit.saasbase.student.widget.LineCharDate;
import cn.qingchengfit.saasbase.student.widget.ModifiedFastScroller;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.FunnelTwoView;
import cn.qingchengfit.widgets.QcRadioGroup;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

import static android.view.Gravity.CENTER;

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
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter(value = "markViewUnit")
    public static void setLineDataUnit(LineCharDate lineCharDate, String unit) {
        lineCharDate.setMarkViewUnit(unit);
    }

    @BindingAdapter(value = "datas")
    public static void setLineDataUnit(LineCharDate lineCharDate, LineData data) {
        lineCharDate.setData(data);
    }

    @BindingAdapter(value = "afterTextChanged")
    public static void afterTextChanged(EditText view, AfterTextChangeListener listener) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
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
     *
     * @param recyclerView
     * @param items
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = "items")
    public static <T extends IFlexible> void setAdapter(RecyclerView recyclerView, List<T> items) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter instanceof FlexibleAdapter) {
            ((FlexibleAdapter<T>) adapter).updateDataSet(items);
        } else {
            adapter = new CommonFlexAdapter(items);
            recyclerView.setAdapter(adapter);
        }
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        List mainItems = ((CommonFlexAdapter) adapter).getMainItems();
        if (mainItems.isEmpty()) {
            mainItems.add(new SimpleTextItemItem("暂无数据", CENTER));
            ((CommonFlexAdapter) adapter).updateDataSet(mainItems);

        }
    }

    @BindingAdapter(value = {"revert", "status"})
    public static void setAdapterTag(RecyclerView recyclerView, boolean revert, int pos) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter instanceof CommonFlexAdapter) {
            ((CommonFlexAdapter) adapter).setStatus(pos);
            ((CommonFlexAdapter) adapter).setTag("revert", revert);
        }
    }

    @BindingAdapter(value = {"itemClickListenter"})
    public static void setAdapterTag(RecyclerView recyclerView, FlexibleAdapter.OnItemClickListener listener) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter instanceof CommonFlexAdapter) {
            ((CommonFlexAdapter) adapter).addListener(listener);
        }
    }


    @BindingAdapter(value = {"emptyDrawable", "emptyTitle", "emptyMsg"})
    public static void setEmptyItem(RecyclerView recyclerView, Drawable emptyDrawable, String title, String msg) {
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
    @BindingAdapter(value = "clearCheck")
    public static void setQcRadioGroupClear(QcRadioGroup group, boolean clear) {
        if (clear) group.clearCheck();
    }

    /**
     * FunnelTwoView 设置数据
     */
    @BindingAdapter(value = "datas")
    public static void setFunnelTwoViewDatas(FunnelTwoView view, List<Float> datas) {
        if (datas != null && !datas.isEmpty()) {
            view.setData(datas);
            view.animateY();

        }
    }

    /**
     * FastScroller 设置显示或隐藏
     */
    @BindingAdapter(value = "enable")
    public static void setFastScrollerEnable(ModifiedFastScroller fastScroller, boolean enable) {
        fastScroller.setEnabled(enable);
    }

    @BindingAdapter({"android:onClick"})
    public static void setOnClickListener(View view, View.OnClickListener clickListener) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag(v.getId());
                long latestTime = tag == null ? 0 : (long) tag;

                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - latestTime > 1000) {//过滤掉600毫秒内的连续点击
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
