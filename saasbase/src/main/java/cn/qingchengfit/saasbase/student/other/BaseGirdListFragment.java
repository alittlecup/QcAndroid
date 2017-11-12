package cn.qingchengfit.saasbase.student.other;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.student.items.ChooseStaffItem;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponse;
import cn.qingchengfit.views.fragments.BaseListFragment;
import cn.qingchengfit.widgets.SpacesItemDecoration;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/11/2.
 */

public class BaseGirdListFragment extends BaseListFragment {
    @Override
    public int getNoDataIconRes() {
        return R.drawable.vd_img_empty_universe;
    }

    @Override
    public String getNoDataStr() {
        return "";
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        GridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(getContext(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        rv.setLayoutManager(gridLayoutManager);
        rv.setItemViewCacheSize(4);
        srl.setEnabled(false);
        rv.setBackgroundColor(Color.WHITE);
        commonFlexAdapter.setMode(SelectableAdapter.MODE_MULTI);
    }

    @Override
    protected void setAnimation() {
        commonFlexAdapter.setAnimationOnScrolling(true)
                .setAnimationInitialDelay(50L)
                .setAnimationInterpolator(new DecelerateInterpolator())
                .setAnimationDelay(100L)
                .setAnimationStartPosition(0);

    }

    @Override
    protected void addDivider() {
        rv.addItemDecoration(new SpaceItemDecoration(20, getContext()));

    }

    public void setStaffs(List<Staff> list) {
        if (commonFlexAdapter != null) {
            datas.clear();
            if (list != null) {
                for (Staff item : list) {
                    datas.add(generateItem(item));
                }
            }
            commonFlexAdapter.notifyDataSetChanged();
        }
    }

    public AbstractFlexibleItem generateItem(Staff item) {
        return new ChooseStaffItem(item);
    }


    public void toggleSelection(int position) {
        commonFlexAdapter.toggleSelection(position);
        commonFlexAdapter.notifyItemChanged(position);
    }

    public List<String> getSelectedItemIds() {
        List<Integer> integers = get();
        List<String> strings = new ArrayList<>();
        for (Integer pos : integers) {
            strings.add(((ChooseStaffItem)datas.get(pos)).getStaff().getId());
        }
        return strings;
    }
    public List<String> getSelectedItemNames() {
        List<Integer> integers = get();
        List<String> strings = new ArrayList<>();
        for (Integer pos : integers) {
            strings.add(((ChooseStaffItem)datas.get(pos)).getStaff().getUsername());
        }
        return strings;
    }

    public void notifyDataSetChanged() {
        commonFlexAdapter.notifyDataSetChanged();
    }

    public List<Integer> get() {
        return commonFlexAdapter.getSelectedPositions();
    }


}
