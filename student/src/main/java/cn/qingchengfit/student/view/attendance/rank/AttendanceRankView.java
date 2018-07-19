package cn.qingchengfit.student.view.attendance.rank;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;

import cn.qingchengfit.student.bean.FilterSortBean;
import cn.qingchengfit.student.item.FilterSortItem;
import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.saascommon.filtertime.FilterTimesFragment;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import cn.qingchengfit.views.fragments.FilterFragment;
import rx.functions.Action2;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AttendanceRankView extends BaseFilterFragment {
    FilterTimesFragment filterTimesFragment;
    FilterFragment sortFilterFragment;

    AttendanceRankViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getParentFragment()).get(AttendanceRankViewModel.class);
        initFragment();
    }

    @Override
    public void dismiss() {
        viewModel.filterVisible.set(false);
        viewModel.qcFilterCheck.set(false);
    }

    private void initFragment() {
        filterTimesFragment = FilterTimesFragment.getInstance(1, -1);
        filterTimesFragment.setSelectDayAction((start, end, title) -> {
            AttendanceRankLoadData loadData = viewModel.getLoadData();
            loadData.setDay(new Pair<>(start, end));
            viewModel.loadSource(loadData);
            viewModel.filterText.set(title);
            viewModel.filterVisible.set(false);
            viewModel.qcFilterCheck.set(false);
        });

        sortFilterFragment = new FilterFragment();
        sortFilterFragment.setItemList(initListItem());
    }

    private List<FilterSortItem> initListItem() {
        List<FilterSortItem> sortList = new ArrayList<>();
        List<FilterSortBean> dataList = new ArrayList<>();
        dataList.add(new FilterSortBean("出勤天数", true, false));
        dataList.add(new FilterSortBean("签到次数", false, false));
        dataList.add(new FilterSortBean("团课节数", false, false));
        dataList.add(new FilterSortBean("私教节数", false, false));

        for (FilterSortBean filterSortBean : dataList) {
            sortList.add(new FilterSortItem(filterSortBean, new Action2<Integer, Boolean>() {
                @Override public void call(Integer pos, Boolean revert) {
                    setSortType(pos,revert);
                    viewModel.sortText.set(dataList.get(pos).name);
                    viewModel.filterVisible.set(false);
                    viewModel.qcFilterCheck.set(false);
                    viewModel.pos.set(pos);
                    for (FilterSortBean bean : dataList) {
                        bean.isLowToHigh = false;
                        bean.isHighToLow = false;
                    }
                    dataList.get(pos).isHighToLow = revert;
                    dataList.get(pos).isLowToHigh = !revert;
                    sortFilterFragment.refresh();
                }
            }));
        }
        return sortList;
    }


    @Override
    protected String[] getTags() {
        return new String[]{"days", "sort"};
    }

    @Override
    protected Fragment getFragmentByTag(String tag) {
        if (tag.equalsIgnoreCase(getTags()[0])) {
            return filterTimesFragment;
        } else if (tag.equalsIgnoreCase(getTags()[1])) {
            return sortFilterFragment;
        }
        return new EmptyFragment();
    }


    public void setSortType(Integer sortType,boolean revert) {
        AttendanceRankLoadData loadData = viewModel.getLoadData();
        loadData.setRevert(revert);
        switch (sortType) {
            case 0:
                loadData.setSortType("days");
                break;
            case 1:
                loadData.setSortType("checkin");
                break;
            case 2:
                loadData.setSortType("group");
                break;
            case 3:
                loadData.setSortType("private");
                break;
        }
        viewModel.loadSource(loadData);
    }
}
