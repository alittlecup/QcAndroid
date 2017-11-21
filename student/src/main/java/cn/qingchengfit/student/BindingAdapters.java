package cn.qingchengfit.student;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.MenuRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.interfaces.PBEKey;

import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.saasbase.student.widget.LineCharDate;
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
        List mainItems = ((CommonFlexAdapter) adapter).getMainItems();
        if(mainItems.isEmpty()){
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
}
