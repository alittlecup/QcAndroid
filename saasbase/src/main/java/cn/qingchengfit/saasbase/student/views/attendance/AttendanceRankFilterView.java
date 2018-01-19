package cn.qingchengfit.saasbase.student.views.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saasbase.student.bean.FilterSortBean;
import cn.qingchengfit.saasbase.student.items.FilterSortItem;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterTimesFragment;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import cn.qingchengfit.views.fragments.FilterFragment;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Action3;

/**
 * Created by huangbaole on 2017/11/14.
 */

public class AttendanceRankFilterView extends BaseFilterFragment {
    FilterTimesFragment filterTimesFragment;
    FilterFragment sortFilterFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SaasbaseParamsInjector.inject(this);
        initFragment();
    }

    @Override
    public void dismiss() {
        if (dimissAction != null) dimissAction.call();
    }

    private void initFragment() {
        filterTimesFragment = FilterTimesFragment.getInstance(1, -1);
        filterTimesFragment.setSelectDayAction(selectAction);

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
            sortList.add(new FilterSortItem(filterSortBean, (pos ,revert) -> {
                sortAction.call(pos, revert);
                for(FilterSortBean bean:dataList){
                    bean.isLowToHigh=false;
                    bean.isHighToLow=false;
                }
                dataList.get(pos).isHighToLow=revert;
                dataList.get(pos).isLowToHigh=!revert;
                sortFilterFragment.refresh();
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

    private Action0 dimissAction;

    public void setDismissAction(Action0 action) {
        this.dimissAction = action;
    }

    private Action3<String, String, String> selectAction;

    public void setSelectDayAction(Action3<String, String, String> action) {
        this.selectAction = action;
    }

    private Action2<Integer, Boolean> sortAction;

    public void setSortAction(Action2<Integer, Boolean> action) {
        this.sortAction = action;
    }


}
